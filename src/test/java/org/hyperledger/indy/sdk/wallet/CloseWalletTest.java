package org.hyperledger.indy.sdk.wallet;

import org.hyperledger.indy.sdk.IndyIntegrationTest;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class CloseWalletTest extends IndyIntegrationTest {

	@Test
	public void testCloseWalletWorks() throws Exception {
		Wallet.createWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
		Wallet wallet = Wallet.openWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();

		wallet.closeWallet().get();
	}

	@Test
	public void testAutoCloseWorks() throws Exception {
		Wallet.createWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
		try (Wallet wallet = Wallet.openWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get()) {
			assertNotNull(wallet);
		}
		Wallet wallet = Wallet.openWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
		wallet.closeWallet().get();
	}
}