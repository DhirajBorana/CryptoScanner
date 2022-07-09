package com.example.cryptoscanner.utils

import com.example.cryptoscanner.CryptoType
import java.util.regex.Pattern

class CryptoHelper : ICryptoHelper {

    override fun isValidCryptoAddress(address: String, cryptoType: CryptoType): Boolean {
        return when (cryptoType) {
            CryptoType.ETH -> isValidEthereumAddress(address)
            CryptoType.BTC -> isValidBitcoinAddress(address)
        }
    }

    // reference link -> http://mokagio.github.io/tech-journal/2014/11/21/regex-bitcoin.html
    override fun isValidBitcoinAddress(address: String): Boolean {
        return Pattern.matches("^[1][a-km-zA-HJ-NP-Z1-9]{25,34}$", address)
    }

    // reference link -> https://regexland.com/regex-ethereum-addresses/
    override fun isValidEthereumAddress(address: String): Boolean {
        return Pattern.matches("^0x[0-9a-f]*$", address)
    }
}