package com.example.cryptoscanner

import com.example.cryptoscanner.utils.CryptoHelper
import com.example.cryptoscanner.utils.ICryptoHelper
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class CryptoHelperTest {

    private lateinit var cryptoHelper: ICryptoHelper
    private val btcAddresses = listOf("1BoatSLRHtKNngkdXEeobR76b53LETtpyT", "16tRHtKNngkdXEeobR76b53LETtpyT0") // [0] -> valid address, [1] -> invalid address
    private val ethAddresses = listOf("0xa2e0dcaa182c99fdc99bbc9f0f2fc6acaca45949", "0xA2e0dCAa182c99fdc99bbc9F0F2fc6acaCA45949") // [0] -> valid address, [1] -> invalid address

    @Before
    fun setUp() {
        cryptoHelper = CryptoHelper()
    }

    @Test fun cryptoHelper_isValidBitcoinAddress_ReturnsTrue() {
        assertTrue(cryptoHelper.isValidBitcoinAddress(btcAddresses[0]))
    }

    @Test fun cryptoHelper_isValidBitcoinAddress_ReturnsFalse() {
        assertFalse(cryptoHelper.isValidBitcoinAddress(btcAddresses[1]))
    }

    @Test fun cryptoHelper_isValidEthereumAddress_ReturnsTrue() {
        assertTrue(cryptoHelper.isValidEthereumAddress(ethAddresses[0]))
    }

    @Test fun cryptoHelper_isValidEthereumAddress_ReturnsFalse() {
        assertFalse(cryptoHelper.isValidBitcoinAddress(ethAddresses[1]))
    }

    @Test fun cryptoHelper_isValidCryptoAddress_ETH_ReturnsTrue() {
        assertTrue(cryptoHelper.isValidCryptoAddress(ethAddresses[0], CryptoType.ETH))
    }

    @Test fun cryptoHelper_isValidCryptoAddress_BTC_ReturnsTrue() {
        assertTrue(cryptoHelper.isValidCryptoAddress(btcAddresses[0], CryptoType.BTC))
    }
}