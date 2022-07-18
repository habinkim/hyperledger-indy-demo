package com.bpnsolution.hyperledgerindydemo.practice;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.anoncreds.AnoncredsResults;
import org.hyperledger.indy.sdk.did.Did;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static org.hyperledger.indy.sdk.anoncreds.Anoncreds.issuerCreateAndStoreCredentialDef;
import static org.hyperledger.indy.sdk.anoncreds.Anoncreds.issuerCreateSchema;
import static org.hyperledger.indy.sdk.ledger.Ledger.*;

@Slf4j
public class Step_03_CreateSchemaAndCredentialDefinition {

	public static void main(String[] args) throws IndyException, ExecutionException, InterruptedException {
		demo();
	}

	public static void demo() throws IndyException, ExecutionException, InterruptedException {
		String poolName = "pool";
		Pool pool = Pool.openPoolLedger(poolName, "{}").get();

		String walletConfig = new JSONObject().put("id", "habinWallet").toString();
		String walletCredentials = new JSONObject().put("key", "habin_wallet_key").toString();
		Wallet walletHandle = Wallet.openWallet(walletConfig, walletCredentials).get();

		String didList = Did.getListMyDidsWithMeta(walletHandle).get();
		JSONArray didListJson = new JSONArray(didList);
		log.info("DID list: {}", didList);
		String issuerDid = didListJson.getJSONObject(0).getString("did");

		String schemaName = "gvt";
		String schemaVersion = "1.3";
		String schemaAttributes = new JSONArray().put("name").put("age").put("sex").put("height").toString();
		AnoncredsResults.IssuerCreateSchemaResult createSchemaResult =
				issuerCreateSchema(issuerDid, schemaName, schemaVersion, schemaAttributes).get();
		String schemaJson = createSchemaResult.getSchemaJson();
		log.info("Schema: {}", schemaJson);

		String schemaRequest = buildSchemaRequest(issuerDid, schemaJson).get();
		log.info("schemaRequest : {}", schemaRequest);

		String schemaResponse = signAndSubmitRequest(pool, walletHandle, issuerDid, schemaRequest).get();
		log.info("schemaResponse : {}", schemaResponse);

		String credDefTag = "Tag1";
		String credDefConfigJson = new JSONObject().put("support_revocation", true).toString();
		AnoncredsResults.IssuerCreateAndStoreCredentialDefResult result =
				issuerCreateAndStoreCredentialDef(walletHandle, issuerDid, schemaJson, credDefTag, null, credDefConfigJson).get();
		log.info("Credential Definition: {}", result.getCredDefJson());

		String credDefRequest = buildCredDefRequest(issuerDid, result.getCredDefJson()).get();
		log.info("credDefRequest : {}", credDefRequest);
		String signAndSubmitResponse = signAndSubmitRequest(pool, walletHandle, issuerDid, credDefRequest).get();
		log.info("signAndSubmitResponse : {}", signAndSubmitResponse);
	}

}
