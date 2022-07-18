package com.bpnsolution.hyperledgerindydemo.practice;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.anoncreds.AnoncredsResults;
import org.hyperledger.indy.sdk.blob_storage.BlobStorageWriter;
import org.hyperledger.indy.sdk.did.Did;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static com.bpnsolution.hyperledgerindydemo.example.util.EnvironmentUtils.getIndyHomePath;
import static org.hyperledger.indy.sdk.anoncreds.Anoncreds.issuerCreateAndStoreRevocReg;
import static org.hyperledger.indy.sdk.anoncreds.Anoncreds.issuerRevokeCredential;
import static org.hyperledger.indy.sdk.ledger.Ledger.*;
import static org.hyperledger.indy.sdk.wallet.Wallet.openWallet;

@Slf4j
public class Step_04_CreateVCRevocationRegistry {

	private static final String TAG = "tag1";

	public static void main(String[] args) throws IndyException, ExecutionException, InterruptedException {
		String poolName = "pool";
		Pool pool = Pool.openPoolLedger(poolName, "{}").get();

		String walletConfig = new JSONObject().put("id", "habinWallet").toString();
		String walletCredentials = new JSONObject().put("key", "habin_wallet_key").toString();
		Wallet walletHandle = openWallet(walletConfig, walletCredentials).get();

		String didList = Did.getListMyDidsWithMeta(walletHandle).get();
		JSONArray didListJson = new JSONArray(didList);
		log.info("DID list: {}", didList);
		String issuerDid = didListJson.getJSONObject(0).getString("did");

		String revRegConfig = new JSONObject("{\"issuance_type\":null,\"max_cred_num\":5}").toString();
		String tailsWriterConfig = new JSONObject(String.format("{\"base_dir\":\"%s\", \"uri_pattern\":\"\"}", getIndyHomePath("tails")).replace('\\', '/')).toString();
		BlobStorageWriter tailsWriter = BlobStorageWriter.openWriter("default", tailsWriterConfig).get();

		AnoncredsResults.IssuerCreateAndStoreRevocRegResult issuerCreateAndStoreRevocRegResult =
				issuerCreateAndStoreRevocReg(walletHandle, issuerDid, null, TAG, null, revRegConfig, tailsWriter).get();

		String revocRegDefRequest = buildRevocRegDefRequest(issuerDid, issuerCreateAndStoreRevocRegResult.getRevRegDefJson()).get();
		log.info("revocRegDefRequest: {}", revocRegDefRequest);
		String signAndSubmitResponse = signAndSubmitRequest(pool, walletHandle, issuerDid, revocRegDefRequest).get();
		log.info("signAndSubmitResponse: {}", signAndSubmitResponse);

		issuerRevokeCredential(walletHandle, 1, null, null);

		buildRevocRegEntryRequest(issuerDid, null, null, null);
	}

}
