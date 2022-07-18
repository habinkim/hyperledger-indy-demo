package com.bpnsolution.hyperledgerindydemo.practice;

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
import static org.hyperledger.indy.sdk.ledger.Ledger.buildNymRequest;
import static org.hyperledger.indy.sdk.ledger.Ledger.signAndSubmitRequest;

@Slf4j
public class Step_01_CreateDIDAndDIDdocument {

	public static void main(String[] args) throws IndyException, IOException, ExecutionException, InterruptedException {
		demo();
	}

	public static void demo()  throws IndyException, IOException, ExecutionException, InterruptedException {
		String poolName = "pool";
		String habinSeed = "000000000000000000000000Steward1";

		// Set protocol version 2 to work with Indy Node 2
		Pool.setProtocolVersion(PROTOCOL_VERSION).get();

		//1. Create and Open Pool
		PoolUtils.createPoolLedgerConfig(poolName);
		Pool pool = Pool.openPoolLedger(poolName, "{}").get();

		// 지갑을 식별할 수 있는 지갑 id, 지갑 데이터 저장소를 정의하는 `storage_type`, 그리고 `storage config` 관련 정보가 JSON 데이터로 입력됨
		String walletConfig = new JSONObject().put("id", "habinWallet").toString();
		// 지갑에 접근하기 위한 인증키 혹은 비밀번호가 입력되고, 키 생성 방법과 키를 저장하는 저장소 등의 정보 또한 JSON 데이터로 입력됨
		String walletCredentials = new JSONObject().put("key", "habin_wallet_key").toString();
		// DID, VC, 인증키 등을 보관할 지갑을 생성
		Wallet.createWallet(walletConfig, walletCredentials).get();
		// 지갑 생성을 마친 사용자는 `open_wallet` API를 통해 자신이 소유한 지갑을 실행
		Wallet walletHandle = Wallet.openWallet(walletConfig, walletCredentials).get();

		String trusteeWalletConfig = new JSONObject().put("id", "trusteeWallet").toString();
		String trusteeWalletCredentials = new JSONObject().put("key", "trustee_wallet_key").toString();
		Wallet.createWallet(trusteeWalletConfig, trusteeWalletCredentials).get();
		Wallet trusteeWallet = Wallet.openWallet(trusteeWalletConfig, trusteeWalletCredentials).get();

		String didJson = "{\"seed\": \"" + habinSeed + "\"}";
		// `create_and_store_my_did` API를 통해 DID와 DID document에 들어갈 인증키를 생성하여 지갑에 저장
		// `wallet_handle`에는 DID를 저장할 지갑의 id가 입력됨
		// `did_json` 매개변수에는 DID 생성 방법 등을 명시한 JSON 데이터가 포함되어 있음
		DidResults.CreateAndStoreMyDidResult habinResult = Did.createAndStoreMyDid(walletHandle, didJson).get();
		String defaultHabinDid = habinResult.getDid();
		String defaultHabinVerkey = habinResult.getVerkey();
		System.out.println("Habin DID: " + defaultHabinDid);
		System.out.println("Habin Verkey: " + defaultHabinVerkey);

		DidResults.CreateAndStoreMyDidResult trusteeResult = Did.createAndStoreMyDid(walletHandle, "{}").get();
		String trusteeDID = trusteeResult.getDid();
		String trusteeVerkey = trusteeResult.getVerkey();
		System.out.println("Trust anchor DID: " + trusteeDID);
		System.out.println("Trust anchor Verkey: " + trusteeVerkey);

		// 사용자는 `build_nym_request` API를 이용해 블록체인에 DID를 등록하기 위한 NYM 트랜잭션을 생성함
		String nymRequest = buildNymRequest(defaultHabinDid, trusteeDID, trusteeVerkey, null, "ENDORSER").get();
		System.out.println("NYM request JSON:\n" + nymRequest);

		// NYM 트랜잭션 생성까지 마쳤으면 마지막으로, `sign_and_submit_request` API를 통해 생성한 NYM 트랜잭션을 indy-node로 전송함
		String nymResponseJson = signAndSubmitRequest(pool, walletHandle, defaultHabinDid, nymRequest).get();
		System.out.println("NYM transaction response:\n" + nymResponseJson);

		// indy-node는 NYM 트랜잭션 등록에 대한 작업 결과를 반환함으로써 DID 및 DID document 생성 과정을 끝내게 됨
	}

}
