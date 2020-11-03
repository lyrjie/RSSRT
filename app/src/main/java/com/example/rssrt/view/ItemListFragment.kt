package com.example.rssrt.view

import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rssrt.viewmodel.ItemListViewModel
import com.example.rssrt.R
import com.example.rssrt.viewmodel.ViewModelFactory
import com.example.rssrt.adapters.ItemAdapter
import com.example.rssrt.adapters.ItemOnClickListener
import com.example.rssrt.databinding.FragmentItemListBinding

private const val ARG_CHANNEL_ID = "channel_id"

class ItemListFragment : Fragment() {
    private var channelId: Long = 0L

    val viewModel: ItemListViewModel by viewModels {
        ViewModelFactory(requireActivity().application, channelId)
    }

    companion object {
        @JvmStatic
        fun newInstance(channelId: Long) =
            ItemListFragment().apply {
                arguments = Bundle().apply { putLong(ARG_CHANNEL_ID, channelId) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            channelId = it.getLong(ARG_CHANNEL_ID)
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentItemListBinding.inflate(inflater)

        setupRecycler(binding)

        setupNavigation()

        return binding.root
    }

    private fun setupNavigation() {
        viewModel.navigateToItemDetails.observe(viewLifecycleOwner) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_content, ItemDetailsFragment.newInstance(it))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecycler(binding: FragmentItemListBinding) {
        val adapter = ItemAdapter(ItemOnClickListener { viewModel.onItemClicked(it) })
        binding.itemRecycler.adapter = adapter
        viewModel.items.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.refresh_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refresh) {
            viewModel.refresh()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}