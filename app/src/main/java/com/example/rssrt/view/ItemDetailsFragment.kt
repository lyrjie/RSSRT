package com.example.rssrt.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rssrt.viewmodel.ItemDetailsViewModel
import com.example.rssrt.databinding.FragmentItemDetailsBinding
import com.example.rssrt.model.database.Item

private const val ARG_ITEM = "item"

class ItemDetailsFragment : Fragment() {
    private lateinit var item: Item

    val viewModel: ItemDetailsViewModel by viewModels { ItemDetailsViewModelFactory(item) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = requireNotNull(it.getParcelable(ARG_ITEM))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentItemDetailsBinding.inflate(inflater)

        binding.viewModel = viewModel

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(item: Item) =
            ItemDetailsFragment().apply {
                arguments = Bundle().apply { putParcelable(ARG_ITEM, item) }
            }
    }
}

class ItemDetailsViewModelFactory(val item: Item) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemDetailsViewModel::class.java)) {
            return ItemDetailsViewModel(item) as T
        }
        throw IllegalArgumentException("Unsupported ViewModel")
    }

}