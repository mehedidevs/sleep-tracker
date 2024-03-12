/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mehedi.sleep_tracker.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mehedi.sleep_tracker.R
import com.mehedi.sleep_tracker.database.SleepDatabase
import com.mehedi.sleep_tracker.databinding.FragmentSleepTrackerBinding

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    lateinit var binding: FragmentSleepTrackerBinding

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_tracker, container, false
        )

        val dataSource =
            SleepDatabase.getInstance(requireContext().applicationContext).sleepDatabaseDao
        val application = requireNotNull(this.activity).application
        val vmFactory = SleepTrackerViewModelFactory(dataSource, application)

        val viewModel: SleepTrackerViewModel by viewModels { vmFactory }
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        setNightRecyclerview(viewModel)


        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner) { night ->

            night?.let {
                findNavController().navigate(
                    SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(
                        night.nightId
                    )
                )
                viewModel.doneNavigating()
            }
        }
        viewModel.showSnackBarEvent.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT // How long to display the message.
                    ).show()
                    viewModel.doneShowingSnackbar()
                }
            }


        }

        return binding.root
    }

    private fun setNightRecyclerview(viewModel: SleepTrackerViewModel) {
        viewModel.nights.observe(viewLifecycleOwner) {

            val sleepAdapter = SleepAdapter().apply {
                submitList(it)
            }


            binding.rvSleepTracker.apply {
                adapter = sleepAdapter
                setHasFixedSize(true)

            }


        }


    }
}
