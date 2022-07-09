package com.example.cryptoscanner.utils

import com.example.cryptoscanner.CryptoType

interface ICryptoHelper {
    fun isValidCryptoAddress(address: String, cryptoType: CryptoType): Boolean
    fun isValidBitcoinAddress(address: String): Boolean
    fun isValidEthereumAddress(address: String): Boolean
}