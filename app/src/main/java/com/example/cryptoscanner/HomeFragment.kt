package com.example.cryptoscanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cryptoscanner.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scanBtcBtn.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavCamera(CryptoType.BTC))
        }
        binding.scanEthBtn.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavCamera(CryptoType.ETH))
        }
    }
}