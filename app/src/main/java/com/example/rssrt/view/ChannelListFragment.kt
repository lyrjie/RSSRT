package com.example.rssrt.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.rssrt.viewmodel.ChannelListViewModel
import com.example.rssrt.R
import com.example.rssrt.adapters.ChannelAdapter
import com.example.rssrt.adapters.ChannelOnClickListener
import com.example.rssrt.databinding.FragmentChannelListBinding
import kotlinx.android.synthetic.main.fragment_channel_list.*

class ChannelListFragment : Fragment() {

    val viewModel: ChannelListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChannelListBinding.inflate(inflater)

        binding.viewModel = viewModel

        connectViewModel(binding)

        return binding.root
    }

    private fun connectViewModel(binding: FragmentChannelListBinding) {
        setupRecycler(binding)

        viewModel.isSourceInputClearRequired.observe(viewLifecycleOwner) {
            binding.inputNewSource.setText("")
        }

        setupProgressBar()

        setupNavigation()

        setupToasts()
    }

    private fun setupProgressBar() {
        viewModel.updateProgress.observe(viewLifecycleOwner) {
            progress.progress = it
        }
        viewModel.isUpdateProressVisible.observe(viewLifecycleOwner) {
            progress.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecycler(binding: FragmentChannelListBinding) {
        val adapter = ChannelAdapter(
            ChannelOnClickListener(
                clickListener = { viewModel.onChannelClicked(it) },
                longClickListener = { viewModel.onChannelLongClicked(it) })
        )
        binding.channelRecycler.adapter = adapter

        viewModel.channels.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupNavigation() {
        viewModel.navigateToChannelDetails.observe(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_content, ItemListFragment.newInstance(it))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupToasts() {
        viewModel.toastMessage.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
        }
    }

}