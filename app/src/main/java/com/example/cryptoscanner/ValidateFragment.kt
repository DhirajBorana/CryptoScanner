package com.example.cryptoscanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.cryptoscanner.databinding.FragmentValidateBinding
import java.util.regex.Pattern

class ValidateFragment : Fragment() {

    private lateinit var binding: FragmentValidateBinding
    private val args: ValidateFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValidateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cryptoAddressTv.text = args.address
        binding.validateBtn.setOnClickListener { validateAddress() }
    }

    private fun validateAddress() {
        val isValid = isValidCryptoAddress(args.address)
        showValidationText(isValid)
    }

    private fun showValidationText(isValid: Boolean) {
        val textColorResId = if (isValid) R.color.valid_address else R.color.invalid_address
        binding.validationTv.apply {
            text = if (isValid) "Valid Address" else "Invalid Address"
            setTextColor(resources.getColor(textColorResId, context?.theme))
        }
    }

    private fun isValidCryptoAddress(address: String): Boolean {
        return when (args.cryptoType) {
            CryptoType.ETH -> isValidEthereumAddress(address)
            CryptoType.BTC -> isValidBitcoinAddress(address)
        }
    }

    private fun isValidBitcoinAddress(address: String): Boolean {
        return Pattern.matches("^[1][a-km-zA-HJ-NP-Z1-9]{25,34}$", address)
    }

    private fun isValidEthereumAddress(address: String): Boolean {
        return Pattern.matches("^[0x][0-9a-f]$", address)
    }
}