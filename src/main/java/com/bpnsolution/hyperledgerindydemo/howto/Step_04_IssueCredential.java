package com.bpnsolution.hyperledgerindydemo.howto;

import com.bpnsolution.hyperledgerindydemo.example.util.PoolUtils;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.anoncreds.Anoncreds;
import org.hyperledger.indy.sdk.anoncreds.AnoncredsResults;
import org.hyperledger.indy.sdk.did.Did;
import org.hyperledger.indy.sdk.did.DidResults;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.wallet.Wallet;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.bpnsolution.hyperledgerindydemo.example.util.PoolUtils.PROTOCOL_VERSION;
import static org.hyperledger.indy.sdk.anoncreds.Anoncreds.*;
import static org.hyperledger.indy.sdk.ledger.Ledger.*;

public class Step_04_IssueCredential {

	public static void main(String[] args) throws IndyException, ExecutionException, InterruptedException, IOException {
		String walletName = "myWallet";

		String poolName = "pool";
		String stewardSeed = "000000000000000000000000Steward1";
		Pool.setProtocolVersion(PROTOCOL_VERSION).get();

		// 1.
		System.out.println("\n1. Creating a new local pool ledger configuration that can be used later to connect pool nodes.\n");
		PoolUtils.createPoolLedgerConfig(poolName);

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
		System.out.println("Steward did: " + defaultStewardDid);

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
		System.out.println("\n9. Build the SCHEMA request to add new schema to the ledger as a Steward\n");
		String name = "gvt";
		String version = "1.0";
		String attributes = "[\"age\", \"sex\", \"height\", \"name\"]";
		String schemaDataJSON = "{\"name\":\"" + name + "\",\"version\":\"" + version + "\",\"attr_names\":" + attributes + "}";
		System.out.println("Schema: " + schemaDataJSON);
		String schemaRequest = buildSchemaRequest(defaultStewardDid, schemaDataJSON).get();
		System.out.println("Schema request:\n" + schemaRequest);

		// 10
		System.out.println("\n10. Sending the SCHEMA request to the ledger\n");
		String schemaResponse = signAndSubmitRequest(pool, walletHandle, defaultStewardDid, schemaRequest).get();
		System.out.println("Schema response:\n" + schemaResponse);

		// 11
		System.out.println("\n11. Creating and storing CLAIM DEFINITION using anoncreds as Trust Anchor, for the given Schema\n");
		String schemaJSON = "{\"seqNo\": 1, \"dest\": \"" + defaultStewardDid + "\", \"data\": " + schemaDataJSON + "}";
		System.out.println("Schema:\n" + schemaJSON);


	//		v.1.3.0
	//		String claimDef = issuerCreateAndStoreClaimDef(walletHandle, trustAnchorDID, schemaJSON, "CL", false).get();
	//		System.out.println("Claim Definition:\n" + claimDef);

		AnoncredsResults.IssuerCreateAndStoreCredentialDefResult issuerCreateAndStoreCredentialDefResult =
				issuerCreateAndStoreCredentialDef(walletHandle, trustAnchorDID, schemaJSON, "CL", null, null).get();
		String credDefJson = issuerCreateAndStoreCredentialDefResult.getCredDefJson();
		System.out.println("issuerCreateAndStoreCredentialDefResult = " + credDefJson);

		// 12
		System.out.println("\n12. Creating Prover wallet and opening it to get the handle\n");
		String proverDID = "VsKV7grR1BUE29mG2Fm2kX";
		String proverWalletName = "prover_wallet";
		Wallet.createWallet(poolName, proverWalletName);
		Wallet proverWalletHandle = Wallet.openWallet(proverWalletName, null).get();

		// 13
		System.out.println("\n13. Prover is creating Master Secret\n");
		String masterSecretId = "master_secret";
		Anoncreds.proverCreateMasterSecret(proverWalletHandle, masterSecretId).get();

		// 14

//		v.1.3.0
//		System.out.println("\n14. Issuer (Trust Anchor) is creating a Claim Offer for Prover\n");
//		String claimOfferJSON = issuerCreateClaimOffer(walletHandle, schemaJSON, trustAnchorDID, proverDID).get();
//		System.out.println("Claim Offer:\n" + claimOfferJSON);

		String credOfferJSON = issuerCreateCredentialOffer(walletHandle, schemaJSON).get();

		// 15
		System.out.println("\n15. Prover creates Credential Request\n");

//		v.1.3.0
//		String claimRequestJSON = proverCreateAndStoreClaimReq(proverWalletHandle, proverDID, claimOfferJSON,
//				claimDef, masterSecretName).get();
//		System.out.println("Claim Request:\n" + claimRequestJSON);

		AnoncredsResults.ProverCreateCredentialRequestResult proverCreateCredentialRequestResult =
				proverCreateCredentialReq(proverWalletHandle, proverDID, credOfferJSON, credDefJson, masterSecretId).get();
		String credentialRequestJson = proverCreateCredentialRequestResult.getCredentialRequestJson();
		System.out.println("Credential Request:\n" + credentialRequestJson);

		// 16
		System.out.println("\n16. Issuer (Trust Anchor) creates Claim for Claim Request\n");
		// Encoded value of non-integer attribute is SHA256 converted to decimal
		// note that encoding is not standardized by Indy except that 32-bit integers are encoded as themselves. IS-786
		String credAttribsJson = "{\n" +
				"               \"sex\":[\"male\",\"5944657099558967239210949258394887428692050081607692519917050011144233115103\"],\n" +
				"               \"name\":[\"Alex\",\"99262857098057710338306967609588410025648622308394250666849665532448612202874\"],\n" +
				"               \"height\":[\"175\",\"175\"],\n" +
				"               \"age\":[\"28\",\"28\"]\n" +
				"        }";
//		issuerCreateCredential(walletHandle, credOfferJSON, credentialRequestJson, credAttribsJson, -1);
//		AnoncredsResults.IssuerCreateClaimResult createClaimResult = issuerCreateClaim(walletHandle, claimRequestJSON,
//				credAttribsJson, - 1).get();
//		String claimJSON = createClaimResult.getClaimJson();
//		System.out.println("Claim:\n" + claimJSON);
//
//		// 17
//		System.out.println("\n17. Prover processes and stores Claim\n");
//		Anoncreds.proverStoreClaim(proverWalletHandle, claimJSON, null).get();

		// 18
		System.out.println("\n18. Close and delete wallet\n");
		walletHandle.closeWallet().get();
		Wallet.deleteWallet(walletName, null).get();

		// 19
		System.out.println("\n19. Close pool\n");
		pool.closePoolLedger().get();

		// 20
		System.out.println("\n20. Delete pool ledger config\n");
		Pool.deletePoolLedgerConfig(poolName).get();
	}

}
