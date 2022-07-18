package com.bpnsolution.hyperledgerindydemo.practice;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.did.Did;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static org.hyperledger.indy.sdk.did.Did.*;
import static org.hyperledger.indy.sdk.ledger.Ledger.*;

/**
 * 오류 발생
 */
@Slf4j
public class Step_02_UpdateDIDdocument {

	protected static final String ENDPOINT = "127.0.0.1:9701";
	private static final String ENDPOINT_JSON = "{\"endpoint\":{\"ha\":\"" + ENDPOINT + "\"}}";

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
		String submitterDid = didListJson.getJSONObject(0).getString("did");
		String selectedDid = didListJson.getJSONObject(1).getString("did");
		String selectedVerkey = didListJson.getJSONObject(1).getString("verkey");

		// 지갑 내 존재하는 DID 중 인증키 변경을 원하는 DID를 선택한 후 새로운 비대칭키 쌍을 생성
		replaceKeysStart(walletHandle, selectedDid, "{}");

		// 기존 DID의 키 쌍을 삭제하고 새로 생성한 키 쌍을 등록
		replaceKeysApply(walletHandle, selectedDid);

		// `set_endpoint_for_did` API의 `address` 매개변수에 서비스 URL을 입력하여 DID document에 `serviceEndpoint`를 추가
		setEndpointForDid(walletHandle, selectedDid, ENDPOINT, selectedVerkey);

		String nymRequest = buildNymRequest(submitterDid, selectedDid, selectedVerkey, null, "ENDORSER").get();
		log.info("NYM request: {}", nymRequest);
		String attribRequest = buildAttribRequest(selectedDid, selectedDid, null, ENDPOINT_JSON, null).get();
		log.info("ATTRIB request: {}", attribRequest);

		String nymResponse = signAndSubmitRequest(pool, walletHandle, submitterDid, nymRequest).get();
		log.info("NYM response: {}", nymResponse);
		String attribResponse = signAndSubmitRequest(pool, walletHandle, submitterDid, attribRequest).get();
		log.info("ATTRIB response: {}", attribResponse);
	}

}
