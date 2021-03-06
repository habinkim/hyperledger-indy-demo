package com.bpnsolution.hyperledgerindydemo.howto;

/*
Example demonstrating how to add DID with the role of Trust Anchor as Steward.
Uses seed to obtain Steward's DID which already exists on the ledger.
Then it generates new DID/Verkey pair for Trust Anchor.
Using Steward's DID, NYM transaction request is built to add Trust Anchor's DID and Verkey
on the ledger with the role of Trust Anchor.
Once the NYM is successfully written on the ledger, it generates new DID/Verkey pair that represents
a client, which are used to create GET_NYM request to query the ledger and confirm Trust Anchor's Verkey.
For the sake of simplicity, a single wallet is used. In the real world scenario, three different wallets
would be used and DIDs would be exchanged using some channel of communication
*/

import com.bpnsolution.hyperledgerindydemo.example.util.PoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.did.Did;
import org.hyperledger.indy.sdk.did.DidResults;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.bpnsolution.hyperledgerindydemo.example.util.PoolUtils.PROTOCOL_VERSION;
import static org.hyperledger.indy.sdk.ledger.Ledger.*;

@Slf4j
public class Step_01_WriteDIDAndQueryVerkey {

	public static void main(String[] args) throws IndyException, ExecutionException, InterruptedException, IOException {

		String walletName = "myWallet";
		String stewardSeed = "000000000000000000000000Steward1";
		Pool.setProtocolVersion(PROTOCOL_VERSION).get();
		
		// 1.
		System.out.println("\n1. Creating a new local pool ledger configuration that can be used later to connect pool nodes.\n");
		String poolName = PoolUtils.createPoolLedgerConfig();
		log.info("poolName: {}", poolName);

		// 2
		System.out.println("\n2. Open pool ledger and get the pool handle from libindy.\n");
		Pool pool = Pool.openPoolLedger(poolName, "{}").get();

		// 3
		System.out.println("\n3. Creates a new secure wallet\n");
		Wallet.createWallet(poolName, walletName).get();

		// 4
		System.out.println("\n4. Open wallet and get the wallet handle from libindy\n");
		Wallet walletHandle = Wallet.openWallet(walletName, null).get();

		// 5
		System.out.println("\n5. Generating and storing steward DID and Verkey\n");
		String did_json = "{\"seed\": \"" + stewardSeed + "\"}";
		DidResults.CreateAndStoreMyDidResult stewardResult = Did.createAndStoreMyDid(walletHandle, did_json).get();
		String defaultStewardDid = stewardResult.getDid();
		System.out.println("Steward DID: " + defaultStewardDid);
		System.out.println("Steward Verkey: " + stewardResult.getVerkey());

		// 6.
		System.out.println("\n6. Generating and storing Trust Anchor DID and Verkey\n");
		DidResults.CreateAndStoreMyDidResult trustAnchorResult = Did.createAndStoreMyDid(walletHandle, "{}").get();
		String trustAnchorDID = trustAnchorResult.getDid();
		String trustAnchorVerkey = trustAnchorResult.getVerkey();
		System.out.println("Trust anchor DID: " + trustAnchorDID);
		System.out.println("Trust anchor Verkey: " + trustAnchorVerkey);

		// 7
		System.out.println("\n7. Build NYM request to add Trust Anchor to the ledger\n");
		String nymRequest = buildNymRequest(defaultStewardDid, trustAnchorDID, trustAnchorVerkey, null, "TRUST_ANCHOR").get();
		System.out.println("NYM request JSON:\n" + nymRequest);

		// 8
		System.out.println("\n8. Sending the nym request to ledger\n");
		String nymResponseJson = signAndSubmitRequest(pool, walletHandle, defaultStewardDid, nymRequest).get();
		System.out.println("NYM transaction response:\n" + nymResponseJson);

		// 9
		System.out.println("\n9. Generating and storing DID and Verkey to query the ledger with\n");
		DidResults.CreateAndStoreMyDidResult clientResult = Did.createAndStoreMyDid(walletHandle, "{}").get();
		String clientDID = clientResult.getDid();
		String clientVerkey = clientResult.getVerkey();
		System.out.println("Client DID: " + clientDID);
		System.out.println("Client Verkey: " + clientVerkey);

		// 10
		System.out.println("\n10. Building the GET_NYM request to query Trust Anchor's Verkey as the Client\n");
		String getNymRequest = buildGetNymRequest(clientDID, trustAnchorDID).get();
		System.out.println("GET_NYM request json:\n" + getNymRequest);

		// 11
		System.out.println("\n11. Sending the GET_NYM request to the ledger\n");
		String getNymResponse = submitRequest(pool, getNymRequest).get();
		System.out.println("GET_NYM response json:\n" + getNymResponse);

		// 12
		System.out.println("\n12. Comparing Trust Anchor Verkey as written by Steward and as retrieved in Client's query\n");
		String responseData = new JSONObject(getNymResponse).getJSONObject("result").getString("data");
		String trustAnchorVerkeyFromLedger = new JSONObject(responseData).getString("verkey");
		System.out.println("Written by Steward: " + trustAnchorVerkey);
		System.out.println("Queried from Ledger: " + trustAnchorVerkeyFromLedger);
		System.out.println("Matching: " + trustAnchorVerkey.equals(trustAnchorVerkeyFromLedger));

		// 13
		System.out.println("\n13. Close and delete wallet\n");
		walletHandle.closeWallet().get();
		Wallet.deleteWallet(walletName, null).get();

		// 14
		System.out.println("\n14. Close pool\n");
		pool.closePoolLedger().get();

		// 15
		System.out.println("\n15. Delete pool ledger config\n");
		Pool.deletePoolLedgerConfig(poolName).get();
	}

}
