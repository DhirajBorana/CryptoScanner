package com.example.cryptoscanner.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.cryptoscanner.utils.CryptoHelper
import com.example.cryptoscanner.utils.ICryptoHelper
import com.example.cryptoscanner.R
import com.example.cryptoscanner.databinding.FragmentValidateBinding

class ValidateFragment : Fragment() {

    private lateinit var binding: FragmentValidateBinding
    private val args: ValidateFragmentArgs by navArgs()
    private val cryptoHelper: ICryptoHelper by lazy { CryptoHelper() }

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
        binding.shareBtn.setOnClickListener { shareAddressIfValid() }
    }

    private fun shareAddressIfValid() {
        if (validateAddress()) {
            val intent = Intent(Intent.ACTION_SEND).also {
                it.type = "text/plain"
                it.putExtra(Intent.EXTRA_TEXT, args.address)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
        } else {
            Toast.makeText(context, "Invalid address cannot be shared", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateAddress(): Boolean {
        val isValid = cryptoHelper.isValidCryptoAddress(args.address, args.cryptoType)
        showValidationText(isValid)
        return isValid
    }

    private fun showValidationText(isValid: Boolean) {
        val textResId = if (isValid) R.string.valid_address else R.string.invalid_address
        val textColorResId = if (isValid) R.color.valid_address else R.color.invalid_address
        binding.validationTv.apply {
            text = getString(textResId, args.cryptoType.toString())
            setTextColor(resources.getColor(textColorResId, context?.theme))
        }
    }
}