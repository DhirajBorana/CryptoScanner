package com.example.cryptoscanner

import java.util.regex.Pattern

class CryptoHelper : ICryptoHelper {

    override fun isValidCryptoAddress(address: String, cryptoType: CryptoType): Boolean {
        return when (cryptoType) {
            CryptoType.ETH -> isValidEthereumAddress(address)
            CryptoType.BTC -> isValidBitcoinAddress(address)
        }
    }

    override fun isValidBitcoinAddress(address: String): Boolean {
        return Pattern.matches("^[1][a-km-zA-HJ-NP-Z1-9]{25,34}$", address)
    }

    override fun isValidEthereumAddress(address: String): Boolean {
        return Pattern.matches("^0x[0-9a-f]*$", address)
    }
}