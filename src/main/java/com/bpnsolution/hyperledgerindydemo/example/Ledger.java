package com.bpnsolution.hyperledgerindydemo.example;

import com.bpnsolution.hyperledgerindydemo.example.util.PoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.indy.sdk.did.Did;
import org.hyperledger.indy.sdk.did.DidJSONParameters;
import org.hyperledger.indy.sdk.did.DidResults.CreateAndStoreMyDidResult;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.json.JSONObject;

import java.util.Iterator;

import static com.bpnsolution.hyperledgerindydemo.example.util.PoolUtils.PROTOCOL_VERSION;
import static org.hyperledger.indy.sdk.ledger.Ledger.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class Ledger {

	public static void main(String[] args) throws Exception {
		Ledger.demo();
	}
	
	static void demo() throws Exception {
		System.out.println("Ledger sample -> started");

		String trusteeSeed = "000000000000000000000000Trustee1";

		// Set protocol version 2 to work with Indy Node 1.4
		Pool.setProtocolVersion(PROTOCOL_VERSION).get();

		// 1. Create ledger config from genesis txn file
		String poolName = PoolUtils.createPoolLedgerConfig();
		Pool pool = Pool.openPoolLedger(poolName, "{}").get();

		// 2. Create and Open My Wallet
		String myWalletConfig = new JSONObject().put("id", "myWallet").toString();
		String myWalletCredentials = new JSONObject().put("key", "my_wallet_key").toString();
		Wallet.createWallet(myWalletConfig, myWalletCredentials).get();
		Wallet myWallet = Wallet.openWallet(myWalletConfig, myWalletCredentials).get();

		// 3. Create and Open Trustee Wallet
		String trusteeWalletConfig = new JSONObject().put("id", "theirWallet").toString();
		String trusteeWalletCredentials = new JSONObject().put("key", "trustee_wallet_key").toString();
		Wallet.createWallet(trusteeWalletConfig, trusteeWalletCredentials).get();
		Wallet trusteeWallet = Wallet.openWallet(trusteeWalletConfig, trusteeWalletCredentials).get();

		// 4. Create My Did
		CreateAndStoreMyDidResult createMyDidResult = Did.createAndStoreMyDid(myWallet, "{}").get();
		String myDid = createMyDidResult.getDid();
		String myVerkey = createMyDidResult.getVerkey();

		// 5. Create Did from Trustee1 seed
		DidJSONParameters.CreateAndStoreMyDidJSONParameter theirDidJson =
				new DidJSONParameters.CreateAndStoreMyDidJSONParameter(null, trusteeSeed, null, null);

		CreateAndStoreMyDidResult createTheirDidResult = Did.createAndStoreMyDid(trusteeWallet, theirDidJson.toJson()).get();
		String trusteeDid = createTheirDidResult.getDid();

		// 6. Build Nym Request
		String taaRequest = buildGetTxnAuthorAgreementRequest(trusteeDid, null).get();
		log.info("TAA request: {}", taaRequest);
		String latestTaa = signAndSubmitRequest(pool, trusteeWallet, trusteeDid, taaRequest).get();
		log.info("Latest TAA: {}", latestTaa);

		String acceptanceMechanismRequest = buildGetAcceptanceMechanismsRequest(trusteeDid, -1, null).get();
		log.info("Acceptance Mechanism request: {}", acceptanceMechanismRequest);
		String aml = signAndSubmitRequest(pool, trusteeWallet, trusteeDid, acceptanceMechanismRequest).get();
		log.info("aml : {}", aml);

		JSONObject latestTaaJson = new JSONObject(latestTaa);
		String digest = latestTaaJson.getJSONObject("result").getJSONObject("data").getString("digest");

		JSONObject amlJson = new JSONObject(aml);
		JSONObject amlResultJson = amlJson.getJSONObject("result");
		int timestamp = amlResultJson.getInt("txnTime");
		log.info("timestamp: {}", timestamp);

		String nymRequest = buildNymRequest(trusteeDid, myDid, myVerkey, null, null).get();
		log.info(nymRequest);

		Iterator<String> keys = amlResultJson.getJSONObject("data").getJSONObject("aml").keys();
		nymRequest = appendTxnAuthorAgreementAcceptanceToRequest(nymRequest, null, null, digest,
				keys.next(), timestamp).get();
		log.info(nymRequest);

		// 7. Trustee Sign Nym Request
		String nymResponseJson = signAndSubmitRequest(pool, trusteeWallet, trusteeDid, nymRequest).get();
		log.info(nymResponseJson);

		JSONObject nymResponse = new JSONObject(nymResponseJson);

		assertEquals(myDid, nymResponse.getJSONObject("result").getJSONObject("txn").getJSONObject("data").getString("dest"));
		assertEquals(myVerkey, nymResponse.getJSONObject("result").getJSONObject("txn").getJSONObject("data").getString("verkey"));

		// 8. Close and delete My Wallet
		myWallet.closeWallet().get();
		Wallet.deleteWallet(myWalletConfig, myWalletCredentials).get();

		// 9. Close and delete Their Wallet
		trusteeWallet.closeWallet().get();
		Wallet.deleteWallet(trusteeWalletConfig, trusteeWalletCredentials).get();

		// 10. Close Pool
		pool.closePoolLedger().get();

		// 11. Delete Pool ledger config
		Pool.deletePoolLedgerConfig(poolName).get();

		System.out.println("Ledger sample -> completed");
	}
}
