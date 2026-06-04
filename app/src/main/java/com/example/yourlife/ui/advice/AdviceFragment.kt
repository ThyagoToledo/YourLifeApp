package com.example.yourlife.ui.advice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yourlife.databinding.FragmentAdviceBinding
import com.example.yourlife.util.Resource

class AdviceFragment : Fragment() {

    private var _binding: FragmentAdviceBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AdviceViewModel
    private lateinit var adviceAdapter: AdviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AdviceViewModel::class.java)

        setupRecyclerView()
        setupObservers()

        viewModel.getAdvices()
    }

    private fun setupRecyclerView() {
        adviceAdapter = AdviceAdapter(
            onUpvote = { viewModel.voteAdvice(it.id, "up") },
            onDownvote = { viewModel.voteAdvice(it.id, "down") }
        )
        binding.rvAdvice.adapter = adviceAdapter
    }

    private fun setupObservers() {
        viewModel.advices.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    adviceAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // You can show a progress bar here
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
