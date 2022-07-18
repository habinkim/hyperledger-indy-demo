package com.bpnsolution.hyperledgerindydemo.example;

import com.bpnsolution.hyperledgerindydemo.example.util.PoolUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.indy.sdk.anoncreds.AnoncredsResults;
import org.hyperledger.indy.sdk.did.Did;
import org.hyperledger.indy.sdk.did.DidJSONParameters;
import org.hyperledger.indy.sdk.did.DidResults.CreateAndStoreMyDidResult;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.hyperledger.indy.sdk.anoncreds.Anoncreds.issuerCreateSchema;
import static org.hyperledger.indy.sdk.ledger.Ledger.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@RequiredArgsConstructor
public class Endorser {

	public static void main(String[] args) throws Exception {
		Endorser.demo();
	}

	static void demo() throws Exception {

		System.out.println("Endorser sample -> started");
		String trusteeSeed = "000000000000000000000000Trustee1";

		// Set protocol version 2 to work with Indy Node 1.4
		Pool.setProtocolVersion(PoolUtils.PROTOCOL_VERSION).get();

		// 1. Create and Open Pool
		String poolName = PoolUtils.createPoolLedgerConfig();
		Pool pool = Pool.openPoolLedger(poolName, "{}").get();

		// 2. Create and Open Author Wallet
		String authorWalletConfig = new JSONObject().put("id", "authorWallet").toString();
		String authorWalletCredentials = new JSONObject().put("key", "author_wallet_key").toString();
		Wallet.createWallet(authorWalletConfig, authorWalletCredentials).get();
		Wallet authorWallet = Wallet.openWallet(authorWalletConfig, authorWalletCredentials).get();

		// 3. Create and Open Endorser Wallet
		String endorserWalletConfig = new JSONObject().put("id", "endorserWallet").toString();
		String endorserWalletCredentials = new JSONObject().put("key", "endorser_wallet_key").toString();
		Wallet.createWallet(endorserWalletConfig, endorserWalletCredentials).get();
		Wallet endorserWallet = Wallet.openWallet(endorserWalletConfig, endorserWalletCredentials).get();

		// 3. Create and Open Trustee Wallet
		String trusteeWalletConfig = new JSONObject().put("id", "trusteeWallet").toString();
		String trusteeWalletCredentials = new JSONObject().put("key", "trustee_wallet_key").toString();
		Wallet.createWallet(trusteeWalletConfig, trusteeWalletCredentials).get();
		Wallet trusteeWallet = Wallet.openWallet(trusteeWalletConfig, trusteeWalletCredentials).get();

		// 4. Create Trustee DID
		DidJSONParameters.CreateAndStoreMyDidJSONParameter theirDidJson =
				new DidJSONParameters.CreateAndStoreMyDidJSONParameter(null, trusteeSeed, null, null);
		CreateAndStoreMyDidResult createTheirDidResult = Did.createAndStoreMyDid(trusteeWallet, theirDidJson.toJson()).get();
		String trusteeDid = createTheirDidResult.getDid();
		log.info("Trustee DID: {}", trusteeDid);

		// 5. Create Author DID
		CreateAndStoreMyDidResult createMyDidResult = Did.createAndStoreMyDid(authorWallet, "{}").get();
		String authorDid = createMyDidResult.getDid();
		log.info("Author DID: {}", authorDid);
		String authorVerkey = createMyDidResult.getVerkey();
		log.info("Author Verkey: {}", authorVerkey);

		// 6. Create Endorser DID
		createMyDidResult = Did.createAndStoreMyDid(endorserWallet, "{}").get();
		String endorserDid = createMyDidResult.getDid();
		log.info("Endorser DID: {}", endorserDid);
		String endorserVerkey = createMyDidResult.getVerkey();
		log.info("Endorser Verkey: {}", endorserVerkey);

		// 7. Build Author Nym Request
		String nymRequest = buildNymRequest(trusteeDid, authorDid, authorVerkey, null, null).get();

		// 8. Trustee Sign Author Nym Request
		signAndSubmitRequest(pool, trusteeWallet, trusteeDid, nymRequest).get();

		// 9. Build Endorser Nym Request
		nymRequest = buildNymRequest(trusteeDid, endorserDid, endorserVerkey, null, "ENDORSER").get();

		// 10. Trustee Sign Endorser Nym Request
		signAndSubmitRequest(pool, trusteeWallet, trusteeDid, nymRequest).get();

		// 11. Create schema with endorser

		String schemaName = "gvt";
		String schemaVersion = "1.0";
		String schemaAttributes = new JSONArray().put("name").put("age").put("sex").put("height").toString();
		AnoncredsResults.IssuerCreateSchemaResult createSchemaResult =
				issuerCreateSchema(authorDid, schemaName, schemaVersion, schemaAttributes).get();
		String schemaJson = createSchemaResult.getSchemaJson();

		//  Transaction Author builds Schema Request
		String schemaRequest = buildSchemaRequest(authorDid, schemaJson).get();
		log.info("schemaRequest : {}", schemaRequest);

		//  Transaction Author appends Endorser's DID into the request
		String schemaRequestWithEndorser = appendRequestEndorser(schemaRequest, endorserDid).get();
		log.info("schemaRequestWithEndorser : {}", schemaRequestWithEndorser);

		//  Transaction Author signs the request with the added endorser field
		String schemaRequestWithEndorserAuthorSigned =
				multiSignRequest(authorWallet, authorDid, schemaRequestWithEndorser).get();
		log.info("schemaRequestWithEndorserAuthorSigned : {}", schemaRequestWithEndorserAuthorSigned);

		//  Transaction Endorser signs the request
		String schemaRequestWithEndorserSigned =
				multiSignRequest(endorserWallet, endorserDid, schemaRequestWithEndorserAuthorSigned).get();
		// 여기서 문제 생김, 지갑이나 원장에서 Endorser가 누락됨
		// Endorser의 Request는 누가 승인하는가?
		// Endorser가 여러명 있어야하는가?
		// 각 Node에 Endorser들이 생성되는가?
		// 어느 Node에 접속해서 하는가?
		log.info("schemaRequestWithEndorserSigned : {}" + schemaRequestWithEndorserSigned);

		//  Transaction Endorser sends the request
		String response = submitRequest(pool, schemaRequestWithEndorserSigned).get();
		log.info("response : {}" + response);
		JSONObject responseJson = new JSONObject(response);
		assertEquals("REPLY", responseJson.getString("op"));

		pool.closePoolLedger().get();
		Pool.deletePoolLedgerConfig(poolName).get();

		trusteeWallet.closeWallet().get();
		Wallet.deleteWallet(trusteeWalletConfig, trusteeWalletCredentials).get();

		authorWallet.closeWallet().get();
		Wallet.deleteWallet(authorWalletConfig, authorWalletCredentials).get();

		endorserWallet.closeWallet().get();
		Wallet.deleteWallet(endorserWalletConfig, endorserWalletCredentials).get();

		System.out.println("Endorser sample -> completed");
	}
}