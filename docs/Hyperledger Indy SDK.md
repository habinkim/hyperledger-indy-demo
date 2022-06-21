# Hyperledger Indy SDK

[TOC]

------



## 1. 인디와 리빈디의 정의와 중요한 이유

Indy는 개인적이고 안전하며 강력한 ID를 위한 소프트웨어 에코시스템을 제공하고 libindy는 클라이언트가 이를 가능하게 합니다. Indy는 전통적으로 신원을 중앙 집중화하는 조직이 아닌 사람들에게 자신의 개인 정보 및 공개에 대한 결정을 맡깁니다. 이를 통해 연결 계약, 취소, 새로운 지불 워크플로, 자산 및 문서 관리 기능, 창의적인 형태의 에스크로, 선별된 평판, 다른 멋진 기술과의 통합 등 모든 종류의 풍부한 혁신이 가능합니다.

Indy는 오픈 소스 분산 원장 기술을 사용합니다. 이러한 원장은 중앙 관리자가 있는 거대한 데이터베이스 대신 참가자 풀에서 협력적으로 제공하는 데이터베이스 형태입니다. 데이터는 여러 위치에 중복 존재하며 많은 시스템에서 조정하는 트랜잭션에서 발생합니다. 강력한 산업 표준 암호화가 이를 보호합니다. 키 관리 및 사이버 보안의 모범 사례가 설계에 널리 퍼져 있습니다. 그 결과 단일 개체의 통제 하에 있는 신뢰할 수 있고 공개된 진실 소스가 생성되고, 시스템 장애에 강하고, 해킹에 탄력적이며, 적대적 개체에 의한 전복에 대해 매우 면역성이 있습니다.

암호화 및 블록체인 세부 사항의 개념이 미스터리하게 느껴지더라도 두려워하지 마십시오. 이 가이드는 Indy의 주요 개념을 소개하는 데 도움이 될 것입니다. 당신은 올바른 장소에서 시작하고 있습니다.



## 2. 예제 시나리오

### 2.1. 우리가 다룰 내용

우리의 목표는 Indy의 많은 개념을 소개하고 모든 것이 작동하도록 하기 위해 무대 뒤에서 일어나는 일에 대한 아이디어를 제공하는 것입니다.

우리는 이야기로 탐험의 틀을 잡을 것입니다. 가상의 Faber College를 졸업한 Alice는 가상의 회사 Acme Corp에 지원하기를 원합니다. 그녀는 취직하자마자 차를 살 수 있도록 Thrift Bank에 대출을 신청하려고 합니다. 그녀는 대학 성적 증명서를 취업 지원서 교육의 증거로 사용하기를 원하고 일단 고용되면 Alice는 고용 사실을 대출에 대한 신용도의 증거로 사용하려고 합니다.

이를 달성하는 데 필요한 일종의 신원 및 신뢰 상호 작용은 오늘날 세상에서 지저분합니다. 느리고 사생활을 침해하며 사기에 취약합니다. 인디가 어떻게 비약적인 도약인지 보여드리겠습니다.



### 2.2. 앨리스 소개

Faber College를 졸업한 Alice는 모교에서 디지털 성적 증명서를 제공하고 있다는 동문 뉴스레터를 받습니다. 그녀는 대학 동문 웹사이트에 로그인하고 **성적 증명서 받기** 를 클릭하여 성적 증명서를 요청합니다 . (이 요청을 시작하는 다른 방법에는 QR 코드 스캔, 게시된 URL에서 스크립트 패키지 다운로드 등이 포함될 수 있습니다.)

Alice는 아직 그것을 깨닫지 못했지만 이 디지털 성적표를 사용하려면 Faber College가 캠퍼스 데이터베이스에서 그녀를 위해 구축한 기존의 ID가 아니라 새로운 유형의 ID가 필요합니다. 그녀는 과거와 미래의 모든 관계와 무관하게 그녀의 허락 없이는 누구도 철회하거나 선택하거나 상관시킬 수 없습니다. 이것은 ***자주적인 아이덴티티\*** 이며 인디의 핵심적인 특징입니다.

일반적인 상황에서 자주적 신원을 관리하려면 데스크톱이나 모바일 애플리케이션과 같은 도구가 필요합니다. 독립 실행형 앱일 수도 있고 원장이 **기관** 이라고 부르는 타사 서비스 제공자를 활용할 수도 있습니다 . Sovrin Foundation은 이러한 도구의 참조 버전을 게시합니다. Faber College는 이러한 요구 사항을 연구한 후 Alice에게 아직 ***Indy 앱 이 없는 경우 추천할 것입니다.\*** **이 앱은 대본** 가져오기 버튼 에서 워크플로의 일부로 설치됩니다.

Alice **가 Get Transcript** 를 클릭하면 Indy **연결 요청** 이 있는 파일을 다운로드합니다 . 확장자가 .indy이고 그녀의 Indy 앱과 연결된 이 연결 요청 파일을 통해 그녀는 원장 생태계의 다른 당사자인 Faber College와 안전한 통신 채널을 설정할 수 있습니다.

따라서 Alice **가 Get Transcript** 를 클릭하면 일반적으로 앱을 설치하고(필요한 경우) 앱을 실행한 다음 앱에서 Faber와의 연결 요청을 수락할지 여부를 묻습니다.

그러나 이 가이드에서는 앱 대신 **Indy SDK API** (libindy에서 제공)를 사용하므로 배후에서 어떤 일이 일어나는지 볼 수 있습니다. 우리는 특히 호기심이 많고 기술적으로 모험적인 앨리스인 척 할 것입니다…



### 2.3. 인프라 준비



#### 1단계: Faber, Acme, Thrift 및 Government에 대한 Trust Anchor 자격 증명 얻기

Faber College와 다른 배우들은 Alice에게 이 서비스를 제공하기 위해 약간의 준비를 했습니다. 이러한 단계를 이해하기 위해 몇 가지 정의부터 시작하겠습니다.

**원장은 원장 엔터티** 를 설명하는 **ID 레코드** 를 저장하기 위한 것 입니다. 신원 기록은 공개 데이터이며 공개 키, 서비스 끝점, 자격 증명 스키마 및 자격 정의를 포함할 수 있습니다. 모든 **신원 기록** 은 중앙 집중식 해결 권한 없이 전 세계적으로 고유하고 (원장을 통해) 확인할 수 있는 정확히 하나의 **DID (탈중앙화 식별자)와 연결됩니다.** 개인 정보를 유지하기 위해 각 **ID 소유자** 는 여러 DID를 소유할 수 있습니다.

이 자습서에서는 두 가지 유형의 DID를 사용합니다. 첫 번째는 **Verinym** 입니다. Verinym 은 **ID 소유자** 의 **법적 ID** 와 연결 **됩니다** . 예를 들어, 모든 당사자는 정부에서 일부 문서 유형에 대한 스키마를 게시하는 데 일부 DID를 사용하는지 확인할 수 있어야 합니다. 두 번째 유형은 **가명** - 진행 중인 디지털 관계( **Connection )의 맥락에서 개인 정보를 유지하는 데 사용되는** **블라인드 식별자** 입니다. 가명이 하나의 디지털 관계만 유지하는 데 사용되는 경우 이를 Pairwise-Unique Identifier라고 합니다. 이 튜토리얼에서는 액터 간의 보안 연결을 유지하기 위해 Pairwise-Unique Identifiers를 사용할 것입니다.

Ledger에 알려진 DID 생성은 **ID 레코드** 자체(NYM 트랜잭션)입니다. NYM 트랜잭션은 해당 원장에 알려진 새 DID 생성, 확인 키 설정 및 회전, 역할 설정 및 변경에 사용할 수 있습니다. 이 트랜잭션의 가장 중요한 필드는 `dest`(대상 DID), `role`(생성되는 사용자 NYM 레코드의 역할) 및 `verkey`(대상 확인 키)입니다. 지원되는 원장 거래에 대한 자세한 정보는 [요청](https://github.com/hyperledger/indy-node/blob/master/docs/source/requests.md) 을 참조하십시오.

DID 확인 키로 게시하면 사람, 조직 또는 사물이 해당 서명 키와 이 키로 서명해야 하는 DID 관련 작업을 알고 있는 유일한 사람으로서 누군가가 이 DID를 소유하고 있음을 확인할 수 있습니다.

**우리 원장은 공개 허가를 받았으며 DID를 게시하려는 사람은 누구나 원장에서 Trust Anchor** 역할을 받아야 합니다 . **Trust Anchor** 는 원장이 이미 알고 있고 다른 사람을 부트스트랩하는 데 도움을 줄 수 있는 사람이나 조직입니다 . ( 사이버 보안 전문가가 "신뢰할 수 있는 제3자"라고 부르는 것과는 다릅니다. 촉진자처럼 생각하십시오. *)* [역할](https://github.com/hyperledger/indy-node/blob/master/docs/source/auth_rules.md) 에 대한 자세한 정보는 역할을 참조하십시오.

**원장에 트랜잭션을 배치할 수 있는 첫 번째 단계는 원장에서 Trust Anchor의 역할을 얻는 것입니다. Faber College, Acme Corp 및 Thrift Bank는 원장에서 Trust Anchor의 역할을 수행하여 Alice에게 서비스를 제공하기 위해 Verinyms 및 Pairwise-Unique Identifiers를 생성할 수 있어야 합니다.**

Trust **Anchor** 가 되려면 원장에서 이미 **Trust Anchor** 역할을 갖고 있는 사람이나 조직에 연락해야 합니다 . 데모를 위해 비어 있는 테스트 원장에는 **Steward** 역할이 있는 NYM만 있지만 모든 **Steward** 는 자동으로 **Trust Anchors** 입니다.



#### 2단계: 인디 노드 풀에 연결

Alice의 사용 사례를 처음부터 끝까지 다룰 코드 작성을 시작할 준비가 되었습니다. 데모 목적을 위해 다른 에이전트에서 실행되도록 의도된 코드를 포함하는 단일 테스트가 될 것이라는 점에 유의하는 것이 중요합니다. 우리는 항상 에이전트가 각 코드 부분을 실행하도록 의도된 것을 가리킬 것입니다. 또한 다른 지갑을 사용하여 다른 에이전트의 DID와 키를 저장합니다. 의 시작하자.

첫 번째 코드 블록에는 **스튜어드** 에이전트의 코드가 포함됩니다.

**적절한 역할을 얻은 후 원장의 트랜잭션을 쓰고 읽으려면 Indy 노드 풀에 연결해야 합니다. Sovrin 풀이나 [우리](https://hyperledger-indy.readthedocs.io/README.html#how-to-start-local-nodes-pool-with-docker) 가 이 튜토리얼의 일부로 시작한 로컬 풀과 같이 존재하는 다른 풀에 연결하려면 풀 구성을 설정해야 합니다.**

풀의 노드 목록은 원장에 NODE 트랜잭션으로 저장됩니다. Libindy를 사용하면 제네시스 트랜잭션이라고 하는 몇 가지 알려진 트랜잭션으로 실제 NODE 트랜잭션 목록을 복원할 수 있습니다. 각 **풀 구성** 은 풀 구성 이름과 풀 구성 JSON의 쌍으로 정의됩니다. pool 구성 json에서 가장 중요한 필드는 제네시스 트랜잭션 목록이 있는 파일의 경로입니다. 이 경로가 올바른지 확인하십시오.

호출을 통해 명명 된 `pool.create_pool_ledger_config`풀 구성을 생성할 수 있습니다. 풀 구성이 생성된 후 를 호출하여 이 구성이 설명하는 노드 풀에 연결할 수 있습니다 `pool.open_pool_ledger`. 이 호출은 향후 libindy 호출에서 이 열린 연결을 참조하는 데 사용할 수 있는 풀 핸들을 반환합니다.

아래 코드 블록에는 이러한 각 항목이 포함되어 있습니다. 주석이 이것이 "Steward Agent"에 대한 코드임을 나타내는 방법에 유의하십시오.

```
  await pool.set_protocol_version(2)
  
  pool_ = {'name': 'pool1'}
  pool_['genesis_txn_path'] = get_pool_genesis_txn_path(pool_['name'])
  pool_['config'] = json.dumps({"genesis_txn": str(pool_['genesis_txn_path'])})
  await pool.create_pool_ledger_config(pool_['name'], pool_['config'])
  pool_['handle'] = await pool.open_pool_ledger(pool_['name'], None)
```



#### 3단계: Steward's Verinym에 대한 소유권 얻기

**다음으로, Steward의 에이전트는 원장에 대한 Steward 역할이 있는 해당 NYM 트랜잭션이 있는 DID에 대한 소유권을 가져와야 합니다.**

우리가 사용하는 테스트 원장은 알려진 일부 **Steward** NYM을 저장하도록 미리 구성되었습니다. 또한 우리는 이러한 NYM에 대한 키를 생성하는 데 사용된 난수 생성기의 **시드 값을 알고 있습니다.** 이러한 **시드 값을 통해** **Steward의** 에이전트 측 에서 이러한 DID에 대한 서명 키를 복원 하고 결과적으로 DID 소유권을 얻을 수 있습니다.

Libindy는 **Wallet** 의 개념을 가지고 있습니다. 지갑은 DID, 키 등과 같은 암호화 자료를 안전하게 보관할 수 있는 저장소입니다. **Steward의** DID 및 해당 서명 키를 저장하려면 에이전트가 먼저 를 호출하여 명명된 지갑을 생성해야 합니다 `wallet.create_wallet`. 그 후 이름이 지정된 지갑은 을(를) 호출하여 열 수 있습니다 `wallet.open_wallet`. 이 호출은 향후 libindy 호출에서 이 열린 지갑을 참조하는 데 사용할 수 있는 지갑 핸들을 반환합니다.

지갑이 열리면 `did.create_and_store_my_did`생성된 DID와 생성된 키의 verkey 부분을 반환하는 호출을 통해 이 지갑에 DID 레코드를 생성할 수 있습니다. 이 DID에 대한 서명키 부분도 지갑에 저장되지만 직접 읽을 수는 없습니다.

```
  # Steward Agent
  steward = {
      'name': "Sovrin Steward",
      'wallet_config': json.dumps({'id': 'sovrin_steward_wallet'}),
      'wallet_credentials': json.dumps({'key': 'steward_wallet_key'}),
      'pool': pool_['handle'],
      'seed': '000000000000000000000000Steward1'
  }
  
  await wallet.create_wallet(steward['wallet_config'], steward['wallet_credentials'])
  steward['wallet'] = await wallet.open_wallet(steward['wallet_config'], steward['wallet_credentials'])
  
  steward['did_info'] = json.dumps({'seed': steward['seed']})
  steward['did'], steward['key'] = await did.create_and_store_my_did(steward['wallet'], steward['did_info'])
```

**참고:** `did.create_and_store_my_did`에 시드에 대한 정보만 제공했지만 스튜어드의 DID에 대한 정보는 제공하지 않았습니다. 기본적으로 DID는 verkey의 처음 16바이트로 생성됩니다. 이러한 DID의 경우 DID와 verkey가 모두 필요한 작업을 처리할 때 verkey를 축약된 형식으로 사용할 수 있습니다. 이 형식에서 verkey는 물결표 '~'로 시작하고 그 뒤에 22 또는 23자가 옵니다. 물결표는 DID 자체가 verkey의 처음 16바이트를 나타내고 물결표 뒤의 문자열은 verkey의 두 번째 16바이트를 나타내며 둘 다 base58Check 인코딩을 사용함을 나타냅니다.



#### 4단계: Steward의 Faber, Acme, Thrift 및 Government 온보딩

**Faber, Acme, Thrift 및 Government는 이제 Steward와 연결을 설정해야 합니다.**

각 연결은 실제로 한 쌍의 DID(Pairwise-Unique Identifier)입니다. 하나의 DID는 연결의 한 당사자가 소유하고 두 번째 DID는 다른 당사자가 소유합니다.

양 당사자는 두 DID를 모두 알고 있으며 이 쌍이 설명하는 연결을 이해합니다.

그들 사이의 관계는 다른 사람과 공유할 수 없습니다. 각 pairwise 관계가 다른 DID를 사용한다는 점에서 두 당사자에게 고유합니다.

연결 설정 프로세스를 **온보딩** 이라고 합니다.

이 튜토리얼에서는 온보딩 프로세스의 간단한 버전을 설명합니다. 우리의 경우 한 당사자는 항상 Trust Anchor가 될 것입니다. 실제 엔터프라이즈 시나리오에서는 더 복잡한 버전을 사용할 수 있습니다.



##### 시설 연결

**스튜어드** 와 **페이버 칼리지** 의 연결 설정 과정을 살펴보자 .

1. **Faber** 와 **Steward** 는 온보딩 프로세스를 시작하기 위해 어떤 식으로든 연락합니다. 웹사이트나 전화로 양식을 작성할 수 있습니다.

2. **Steward 는** **Faber**`did.create_and_store_my_did` 와의 안전한 상호 작용에만 사용할 것이라고 호출하여 지갑에 새로운 DID 레코드를 생성합니다 .

   ```
   # Steward Agent
   (steward['did_for_faber'], steward['key_for_faber']) = await did.create_and_store_my_did(steward['wallet'], "{}")
   ```

3. **Steward** 는 NYM 요청 을 작성하고 생성된 요청을 보내기 위해 지속적으로 호출 하여 해당 `NYM`트랜잭션을 원장으로 보냅니다.`ledger.build_nym_request``ledger.sign_and_submit_request`

   ```
   # Steward Agent
   nym_request = await ledger.build_nym_request(steward['did'], steward['did_for_faber'], steward['key_for_faber'], None, role)
   await ledger.sign_and_submit_request(steward['pool'], steward['wallet'], steward['did'], nym_request)
   ```

4. **Steward** 는 생성된 `DID`및 를 포함하는 연결 요청을 생성합니다 `Nonce`. 이 nonce는 고유한 연결 요청을 추적하기 위해 생성된 큰 난수일 뿐입니다. nonce는 한 번만 사용할 수 있는 임의의 임의 숫자입니다. 연결 요청이 수락되면 초대받은 사람이 응답을 이전 요청과 일치시킬 수 있도록 nonce에 디지털 서명을 합니다.

   ```
   # Steward Agent
   connection_request = {
       'did': steward['did_for_faber'],
       'nonce': 123456789
   }
   ```

5. **Steward 는** **Faber** 에게 연결 요청을 보냅니다 .

6. **Faber 는** **Steward** 의 연결 요청을 수락합니다 .

7. **Faber** 는 지갑이 아직 존재하지 않는 경우 생성합니다.

   ```
   # Faber Agent
   await wallet.create_wallet(faber['wallet_config'], faber['wallet_credentials'])
   faber['wallet'] = await wallet.open_wallet(faber['wallet_config'], faber['wallet_credentials'])
   ```

8. **Faber 는** **Steward**`did.create_and_store_my_did` 와의 안전한 상호 작용에만 사용할 것이라고 호출하여 지갑에 새로운 DID 레코드를 생성합니다 .

   ```
   # Faber Agent
   (faber['did_for_steward'], faber['key_for_steward']) = await did.create_and_store_my_did(faber['wallet'], "{}")
   ```

9. **Faber** 는 수신된 연결 요청에서 생성된 을 포함하는 연결 응답을 생성 `DID`합니다 `Verkey`.`Nonce`

   ```
   # Faber Agent
   connection_response = json.dumps({
       'did': faber['did_for_steward'],
       'verkey': faber['key_for_steward'],
       'nonce': connection_request['nonce']
   })
   ```

10. **Faber** 는 . **_** _`did.key_for_did`

    ```
    # Faber Agent
    faber['steward_key_for_faber'] = await did.key_for_did(faber['pool'], faber['wallet'], connection_request['did'])
    ```

11. **Faber**`crypto.anon_crypt` 는 **Steward** verkey 로 호출하여 연결 응답을 익명으로 암호화합니다 . 익명 암호화 스키마는 공개 키가 제공된 수신자에게 메시지를 보내기 위해 설계되었습니다. 수신자만 개인 키를 사용하여 이러한 메시지를 해독할 수 있습니다. 받는 사람은 메시지의 무결성을 확인할 수 있지만 보낸 사람의 ID는 확인할 수 없습니다.

    ```
    # Faber Agent
    anoncrypted_connection_response = await crypto.anon_crypt(faber['steward_key_for_faber'], connection_response.encode('utf-8'))
    ```

12. **Faber** 는 익명으로 암호화된 연결 응답을 **Steward** 에게 보냅니다 .

13. **Steward** 는 을(를) 호출하여 익명으로 연결 응답을 해독합니다 `crypto.anon_decrypt`.

    ```
    # Steward Agent
    decrypted_connection_response = \
        (await crypto.anon_decrypt(steward['wallet'], steward['key_for_faber'], anoncrypted_connection_response)).decode("utf-8")
    ```

14. **Steward** 는 Nonce를 비교하여 **Faber** 를 인증합니다 .

    ```
    # Steward Agent
    assert connection_request['nonce'] == decrypted_connection_response['nonce']
    ```

15. **Steward 는** **Faber의** DID에 `NYM`대한 트랜잭션을 Ledger 로 보냅니다 . Steward가 이 트랜잭션의 발신자임에도 불구하고 Faber가 제공한 verkey를 사용하므로 DID의 소유자는 Faber가 됩니다.

    ```
    # Steward Agent
    nym_request = await ledger.build_nym_request(steward['did'], decrypted_connection_response['did'], decrypted_connection_response['verkey'], None, role)
    await ledger.sign_and_submit_request(steward['pool'], steward['wallet'], steward['did'], nym_request)
    ```

이 시점에서 **Faber** 는 **Steward** 에 연결되어 안전한 P2P 방식으로 상호 작용할 수 있습니다. **Faber** 는 다음과 같은 이유로 **Steward** 의 응답을 신뢰할 수 있습니다 .

- 현재 끝점에 연결합니다.
- 재생 불가 - 무작위 도전으로 인해 공격이 가능합니다.
- **Steward** 전자 서명 을 검증하는 데 사용된 검증 키 가 원장에서 방금 확인했기 때문에 올바른 키임을 알고 있습니다.

**참고:** 모든 당사자는 동일한 DID를 사용하여 다른 관계를 설정해서는 안 됩니다. 독립적인 쌍별 관계를 가짐으로써 다른 사람들이 여러 상호 작용에 걸쳐 귀하의 활동을 연관시키는 능력을 감소시킵니다.



##### Verinym 얻기

이전에 생성된 **Faber** DID는 그 자체로 자주적 신원과 동일한 것이 아님을 이해하는 것이 중요합니다. **이 DID는 Steward** 와의 안전한 상호 작용을 위해서만 사용해야 합니다 . 연결이 설정된 후 **Faber** 는 원장에서 Verinym으로 사용할 새 DID 레코드를 생성해야 합니다.

1. **Faber** 는 를 호출하여 지갑에 새로운 DID를 생성합니다 `did.create_and_store_my_did`.

   ```
   # Faber Agent
   (faber['did'], faber['key']) = await did.create_and_store_my_did(faber['wallet'], "{}")
   ```

2. **Faber** 는 생성된 DID와 verkey를 포함할 메시지를 준비합니다.

   ```
   # Faber Agent
   faber['did_info'] = json.dumps({
       'did': faber['did'],
       'verkey': faber['key']
   })
   ```

3. **Faber**`crypto.auth_crypt` 는 인증된 암호화 스키마의 구현인 함수 를 호출하여 메시지를 인증하고 암호화합니다 . 인증된 암호화는 특히 받는 사람에 대한 기밀 메시지를 보내기 위해 설계되었습니다. 발신자는 수신자의 공개 키(verkey)와 그의 비밀(서명) 키를 사용하여 공유 비밀 키를 계산할 수 있습니다. 수신자는 발신자의 공개 키(verkey)와 그의 비밀(서명) 키를 사용하여 정확히 동일한 공유 비밀 키를 계산할 수 있습니다. 해당 공유 비밀 키를 사용하여 암호화된 메시지가 변조되지 않았는지 확인한 후 결국 해독할 수 있습니다.

   ```
   # Faber Agent
   authcrypted_faber_did_info_json = \
       await crypto.auth_crypt(faber['wallet'], faber['key_for_steward'], faber['steward_key_for_faber, faber['did_info'].encode('utf-8'))
   ```

4. **Faber** 는 암호화된 메시지를 **Steward** 에게 보냅니다 .

5. **Steward** 는 을(를) 호출하여 수신된 메시지를 해독합니다 `crypto.auth_decrypt`.

   ```
   # Steward Agent    
   sender['faber_key_for_steward'], authdecrypted_faber_did_info_json = \
       await crypto.auth_decrypt(steward['wallet'], steward['key_for_faber'], authcrypted_faber_did_info_json)
   faber_did_info = json.loads(authdecrypted_faber_did_info_json)
   ```

6. **Steward** 는 를 호출 하여 원장에게 **Faber의** DID 인증 키를 요청 `did.key_for_did`합니다.

   ```
   # Steward Agent    
   steward['faber_key_for_steward'] = await did.key_for_did(steward['pool'], steward['wallet'], ['faber_did_for_steward'])
   ```

7. **Steward** 는 Message Sender Verkey와 Ledger에서 수신 한 **Faber Verkey를 비교하여** **Faber** 를 인증합니다 .

   ```
   # Steward Agent    
   assert sender_verkey == steward['faber_key_for_steward']
   ```

8. **Steward** 는 해당 NYM 트랜잭션을 역할이 있는 원장으로 보냅니다. Steward가 이 트랜잭션의 발신자임에도 불구하고 Faber에서 제공하는 Verkey를 사용하기 때문에 DID의 소유자는 Faber가 됩니다.`TRUST ANCHOR`

   ```
   # Steward Agent
   nym_request = await ledger.build_nym_request(steward['did'], decrypted_faber_did_info_json['did'],
                                                decrypted_faber_did_info_json['verkey'], None, 'TRUST_ANCHOR')
   await ledger.sign_and_submit_request(steward['pool'], steward['wallet'], steward['did'], nym_request)
   ```

이 시점에서 **Faber** 는 원장에서 자신의 신원과 관련된 DID를 가지고 있습니다.

**Acme** , **Thrift Bank** 및 **Government 는** **Steward** 와 동일한 온보딩 프로세스 연결 설정을 통과해야 합니다 .



#### 5단계: 자격 증명 스키마 설정

**자격 증명 스키마** 는 하나의 특정 자격 증명이 포함할 수 있는 속성 목록을 설명하는 기본 의미 체계입니다.

**참고:** 기존 스키마를 업데이트할 수 없습니다. 따라서 스키마를 발전시켜야 하는 경우 새 버전이나 이름을 가진 새 스키마를 만들어야 합니다.

**모든 Trust Anchor** 가 **자격 증명 스키마** 를 생성하고 원장에 저장할 수 있습니다 .

**정부 가** **Transcript** Credential Schema를 생성하고 원장에 게시하는 곳은 다음과 같습니다 .

1. **Trust Anchor** 는 생성 된 자격 증명 **스키마**`anoncreds.issuer_create_schema` 를 반환하는 를 호출하여 자격 증명 **스키마** 를 생성 합니다.

   ```
   # Government Agent
   transcript = {
       'name': 'Transcript',
       'version': '1.2',
       'attributes': ['first_name', 'last_name', 'degree', 'status', 'year', 'average', 'ssn']
   }
   (government['transcript_schema_id'], government['transcript_schema']) = \
       await anoncreds.issuer_create_schema(government['did'], transcript['name'], transcript['version'],
                                            json.dumps(transcript['attributes']))
   transcript_schema_id = government['transcript_schema_id']
   ```

2. **Trust Anchor** 는 `ledger.build_schema_request`Schema 요청 `ledger.sign_and_submit_request`을 작성하고 생성된 요청을 보내기 위해 지속적으로 호출하여 해당 Schema 트랜잭션을 Ledger 로 보냅니다.

   ```
   # Government Agent
   schema_request = await ledger.build_schema_request(government['did'], government['transcript_schema'])
   await ledger.sign_and_submit_request(government['pool'], government['wallet'], government['did'], schema_request)
   ```

같은 방식으로 **정부 는** **직업 인증서** 자격 증명 스키마를 생성하고 원장에 게시합니다 .

```
  # Government Agent
    job_certificate = {
        'name': 'Job-Certificate',
        'version': '0.2',
        'attributes': ['first_name', 'last_name', 'salary', 'employee_status', 'experience']
    }
    (government['job_certificate_schema_id'], government['job_certificate_schema']) = \
        await anoncreds.issuer_create_schema(government['did'], job_certificate['name'], job_certificate['version'],
                                             json.dumps(job_certificate['attributes']))
    job_certificate_schema_id = government['job_certificate_schema_id']
    
    schema_request = await ledger.build_schema_request(government['did'], government['job_certificate_schema'])
    await ledger.sign_and_submit_request(government['pool'], government['wallet'], government['did'], schema_request)
```

이 시점에서 우리는 **정부** 가 원장에 게시한 **성적 증명서** 및 **직업 인증서** 자격 증명 스키마 를 가지고 있습니다.



#### 6단계: 자격 증명 정의 설정

**자격 증명 정의** 는 발급자가 자격 증명 서명에 사용하는 키가 특정 자격 증명 스키마도 충족한다는 점에서 유사합니다.

**참고** 기존 자격 증명 정의에서 데이터를 업데이트할 수 없습니다. 따라서 `CredDef`진화해야 하는 경우(예: 키를 회전해야 하는 경우) 새 발급자 DID로 새 자격 증명 정의를 생성해야 합니다.

**Credential Definition은 Trust Anchor** 에 의해 생성되어 원장에 저장될 수 있습니다 . 여기에서 **Faber** 는 알려진 **Transcript** Credential Schema에 대한 Credential Definition을 만들고 Ledger에 게시합니다.

1. **Trust Anchor** 는 요청 을 작성하고 생성된 요청을 보내고 응답 에서 Anoncreds API가 요구하는 형식으로 가져오기 위해 일관 되게 호출하여 원장에서 특정 **자격 증명 스키마** 를 가져옵니다 .`ledger.build_get_schema_request``GetSchema``ledger.sign_and_submit_request``ledger.parse_get_schema_response``Schema``GetSchema`

   ```
   # Faber Agent
   get_schema_request = await ledger.build_get_schema_request(faber['did'], transcript_schema_id)
   get_schema_response = await ledger.submit_request(faber['pool'], get_schema_request) 
   faber['transcript_schema_id'], faber['transcript_schema'] = await ledger.parse_get_schema_response(get_schema_response)
   ```

2. **Trust Anchor** 는 생성 된 공개 **Credential Definition** 을 반환하는 호출 을 통해 수신 된 **Credential Schema 와 관련된** **Credential Definition** 을 생성 합니다. 이 **Credential Schema** 에 대한 private Credential Definition 부분 도 지갑에 저장되지만 직접 읽는 것은 불가능합니다.`anoncreds.issuer_create_and_store_credential_def`

   ```
   # Faber Agent
   transcript_cred_def = {
       'tag': 'TAG1',
       'type': 'CL',
       'config': {"support_revocation": False}
   }
   (faber['transcript_cred_def_id'], faber['transcript_cred_def']) = \
       await anoncreds.issuer_create_and_store_credential_def(faber['wallet'], faber['did'],
                                                              faber['transcript_schema'], transcript_cred_def['tag'],
                                                              transcript_cred_def['type'],
                                                              json.dumps(transcript_cred_def['config']))
   ```

3. **Trust Anchor** 는 요청 을 빌드하고 생성된 요청을 보내기 위해 지속적으로 호출 하여 해당 `CredDef`트랜잭션을 원장으로 보냅니다.`ledger.build_cred_def_request``CredDef``ledger.sign_and_submit_request`

   ```
   # Faber Agent     
   cred_def_request = await ledger.build_cred_def_request(faber['did'], faber['transcript_cred_def'])
   await ledger.sign_and_submit_request(faber['pool'], faber['wallet'], faber['did'], cred_def_request)
   ```

같은 방식으로 **Acme** 는 알려진 **Job-Certificate Credential Schema에 대한** **Credential Definition** 을 생성하고 원장에 게시합니다 .

```
  # Acme Agent
  get_schema_request = await ledger.build_get_schema_request(acme['did'], job_certificate_schema_id)
  get_schema_response = await ledger.submit_request(acme['pool'], get_schema_request) 
  acme['job_certificate_schema_id'], acme['job_certificate_schema'] = await ledger.parse_get_schema_response(get_schema_response)
    
  job_certificate_cred_def = {
      'tag': 'TAG1',
      'type': 'CL',
      'config': {"support_revocation": False}
  }
  (acme['job_certificate_cred_def_id'], acme['job_certificate_cred_def']) = \
      await anoncreds.issuer_create_and_store_credential_def(acme['wallet'], acme['did'],
                                                             acme['job_certificate_schema'], job_certificate_cred_def['tag'],
                                                             job_certificate_cred_def['type'],
                                                             json.dumps(job_certificate_cred_def['config']))
  
  cred_def_request = await ledger.build_cred_def_request(acme['did'], acme['job_certificate_cred_def'])
  await ledger.sign_and_submit_request(acme['pool'], acme['wallet'], acme['did'], cred_def_request)
```

**Acme** 는 **Job-Certificate* 자격 증명을 해지할 예정입니다. 해지 레지스트리를 생성하기로 결정합니다. Hyperledger Indy의 해지 레지스트리 유형 중 하나는 해지된 자격 증명을 게시하기 위해 암호화 누산기를 사용합니다. 이러한 축전지의 내부 작동에 대한 자세한 내용은 [여기를](https://github.com/fabienpe/indy-sdk/blob/master/docs/concepts/revocation/cred-revocation.md) 참조하십시오 ). 이러한 누산기를 사용하려면 원장 외부에 "유효성 꼬리"를 게시해야 합니다. 이 데모의 목적을 위해 유효성 꼬리는 '블롭 스토리지'를 사용하여 파일에 작성됩니다.

```
    # Acme Agent
    acme['tails_writer_config'] = json.dumps({'base_dir': "/tmp/indy_acme_tails", 'uri_pattern': ''})
    tails_writer = await blob_storage.open_writer('default', acme['tails_writer_config'])
```

유효성 꼬리가 구성되면 **Acme** 는 주어진 자격 증명 정의에 대한 새 해지 레지스트리를 생성할 수 있습니다.

```
    # Acme Agent
    (acme['revoc_reg_id'], acme['revoc_reg_def'], acme['revoc_reg_entry']) = \
        await anoncreds.issuer_create_and_store_revoc_reg(acme['wallet'], acme['did'], 'CL_ACCUM', 'TAG1',
                                                          acme['job_certificate_cred_def_id'],
                                                          json.dumps({'max_cred_num': 5,
                                                                      'issuance_type': 'ISSUANCE_ON_DEMAND'}),
                                                          tails_writer)

    acme['revoc_reg_def_request'] = await ledger.build_revoc_reg_def_request(acme['did'], acme['revoc_reg_def'])
    await ledger.sign_and_submit_request(acme['pool'], acme['wallet'], acme['did'], acme['revoc_reg_def_request'])

    acme['revoc_reg_entry_request'] = \
        await ledger.build_revoc_reg_entry_request(acme['did'], acme['revoc_reg_id'], 'CL_ACCUM',
                                                   acme['revoc_reg_entry'])
    await ledger.sign_and_submit_request(acme['pool'], acme['wallet'], acme['did'], acme['revoc_reg_entry_request'])
```

이 시점에서 **Acme 에서 발행한** **Job-Certificate** Credential Schema 에 대한 **Credential Definition** (해지 지원) 과 **Faber 에서 발행한** **Transcript** Credential Schema 에 대한 **Credential Definition** 이 있습니다.



### 2.4. 앨리스는 성적표를 얻습니다.

자격 증명은 이름, 나이, 신용 점수 등 신원에 대한 정보입니다. 사실이라고 주장하는 정보입니다. 이 경우 자격 증명의 이름은 "성적표"입니다.

자격 증명은 발급자가 제공합니다.

발행자는 Ledger에 알려진 모든 ID 소유자일 수 있으며 발행자는 식별할 수 있는 ID 소유자에 대한 자격 증명을 발행할 수 있습니다.

자격 증명의 유용성과 신뢰성은 당면한 자격 증명에 대한 발급자의 평판과 관련이 있습니다. Alice가 초콜릿 아이스크림을 좋아한다는 증명서를 자체 발급하는 것은 지극히 합리적일 수 있지만, 그녀가 Faber College를 졸업했다는 증명서를 자체 발급하는 경우에는 누구에게도 감명을 주지 않아야 합니다.

[Alice 소개](https://hyperledger-indy.readthedocs.io/projects/sdk/en/latest/docs/getting-started/indy-walkthrough.html#about-alice) 에서 언급했듯이 **Alice 는** **Faber College** 를 졸업했습니다 . **Faber College** 는 Alice와 연결을 설정한 후 **성적 증명서** 발급에 대한 자격 제안을 만들었습니다 .

```
  # Faber Agent
  faber['transcript_cred_offer'] = await anoncreds.issuer_create_credential_offer(faber['wallet'], faber['transcript_cred_def_id'])
```

**참고:** 액터 간에 전송되는 모든 메시지는 `Authenticated-encryption`체계를 사용하여 암호화됩니다.

이 **Transcript** Credential 의 가치는 **Faber College** 에서 증명 가능하게 발급한 것 입니다.

**Alice 는** **Transcript** Credential에 포함 된 속성을 확인하려고 합니다 . 이러한 속성은 기록에 대한 자격 증명 스키마가 원장에 기록되었기 때문에 알려져 **있습니다** .

```
  # Alice Agent
  get_schema_request = await ledger.build_get_schema_request(alice['did_for_faber'], alice['transcript_cred_offer']['schema_id'])
  get_schema_response = await ledger.submit_request(alice['pool'], get_schema_request)
  transcript_schema = await ledger.parse_get_schema_response(get_schema_response)

  print(transcript_schema['data'])
  # Transcript Schema:
  {
      'name': 'Transcript',
      'version': '1.2',
      'attr_names': ['first_name', 'last_name', 'degree', 'status', 'year', 'average', 'ssn']
  }
```

그러나 **Transcript** Credential은 아직 사용 가능한 형태로 Alice에게 전달되지 않았습니다. Alice는 해당 자격 증명을 사용하려고 합니다. **그것을 얻으려면 Alice가 요청해야 하지만 먼저 마스터 시크릿** 을 만들어야 합니다 .

**참고:** 마스터 비밀은 자격 증명이 고유하게 적용되도록 보장하기 위해 증명자가 사용하는 개인 데이터 항목입니다. 마스터 비밀은 여러 자격 증명의 데이터를 결합하여 자격 증명에 공통 주제(검증자)가 있음을 증명하는 입력입니다. 마스터 시크릿은 증명자에게만 알려져야 합니다.

앨리스는 지갑에 마스터 시크릿을 생성합니다.

```
  # Alice Agent
  alice['master_secret_id'] = await anoncreds.prover_create_master_secret(alice['wallet'], None)
```

**Alice는 또한 Transcript** Credential Offer `cred_def_id`에 해당하는 Credential Definition을 얻어야 합니다 .

```
  # Alice Agent
  get_cred_def_request = await ledger.build_get_cred_def_request(alice['did_for_faber'], alice['transcript_cred_offer']['cred_def_id'])
  get_cred_def_response = await ledger.submit_request(alice['pool'], get_cred_def_request)
  alice['transcript_cred_def'] = await ledger.parse_get_cred_def_response(get_cred_def_response)
```

이제 Alice는 **Faber Transcript** Credential 발급에 대한 Credential Request를 생성할 수 있는 모든 것을 갖게 되었습니다.

```
  # Alice Agent
    (alice['transcript_cred_request'], alice['transcript_cred_request_metadata']) = \
        await anoncreds.prover_create_credential_req(alice['wallet'], alice['did_for_faber'], alice['transcript_cred_offer'],
                                                     alice['transcript_cred_def'], alice['master_secret_id'])
```

**Faber 는** **Transcript** Credential Schema 의 각 속성에 대해 원시 값과 인코딩된 값을 모두 준비합니다 . **Faber 는 Alice에 대한** **Transcript** Credential을 생성합니다 .

```
  # Faber Agent
  # note that encoding is not standardized by Indy except that 32-bit integers are encoded as themselves. IS-786
  transcript_cred_values = json.dumps({
      "first_name": {"raw": "Alice", "encoded": "1139481716457488690172217916278103335"},
      "last_name": {"raw": "Garcia", "encoded": "5321642780241790123587902456789123452"},
      "degree": {"raw": "Bachelor of Science, Marketing", "encoded": "12434523576212321"},
      "status": {"raw": "graduated", "encoded": "2213454313412354"},
      "ssn": {"raw": "123-45-6789", "encoded": "3124141231422543541"},
      "year": {"raw": "2015", "encoded": "2015"},
      "average": {"raw": "5", "encoded": "5"}
  })

  faber['transcript_cred_def'], _, _ = \
      await anoncreds.issuer_create_credential(faber['wallet'], faber['transcript_cred_offer'], faber['transcript_cred_request'],
                                               transcript_cred_values, None, None)
```

이제 **Transcript** Credential이 발급되었습니다. 앨리스는 그것을 그녀의 지갑에 보관합니다.

```
  # Alice Agent
  await anoncreds.prover_store_credential(alice['wallet'], None, faber['transcript_cred_request'], faber['transcript_cred_request_metadata'],
                                          alice['transcript_cred'], alice['transcript_cred_def'], None)
```

Alice는 우편으로 받은 실제 사본을 보관하는 것과 거의 같은 방식으로 그것을 소유하고 있습니다.



### 2.5. 구직신청

미래의 어느 시점에서 Alice는 가상의 회사인 Acme Corp에서 일하고 싶어합니다. 일반적으로 그녀는 웹사이트를 탐색하고 하이퍼링크를 클릭하여 지원합니다. 그녀의 브라우저는 그녀의 Indy 앱이 열리는 연결 요청을 다운로드할 것입니다. 이것은 Alice에게 Acme Corp와의 연결을 수락하도록 요청하는 프롬프트를 트리거합니다. 우리는 Indy-SDK를 사용하고 있기 때문에 프로세스는 다르지만 단계는 동일합니다. 연결 설정 과정은 Faber가 Steward 연결 요청을 수락했을 때와 동일합니다.

Alice는 Acme와 연결을 설정한 후 **Job-Application** Proof Request를 받았습니다. 증명 요청은 특정 속성이 있고 다른 확인된 자격 증명이 제공할 수 있는 술어의 해결에 대한 검증 가능한 증거가 필요한 당사자가 수행하는 요청입니다.

이 경우 Acme Corp은 Alice에게 **Job Application** 을 제공하도록 요청하고 있습니다. 지원서에는 이름, 학위, 신분, 사회보장번호(SSN) 및 평균 점수 또는 등급에 대한 조건 만족이 필요합니다.

이 경우 **Job-Application** Proof Request는 다음과 같습니다.

```
  # Acme Agent
  acme['job_application_proof_request'] = json.dumps({
      'nonce': '1432422343242122312411212',
      'name': 'Job-Application',
      'version': '0.1',
      'requested_attributes': {
          'attr1_referent': {
              'name': 'first_name'
          },
          'attr2_referent': {
              'name': 'last_name'
          },
          'attr3_referent': {
              'name': 'degree',
              'restrictions': [{'cred_def_id': faber['transcript_cred_def_id']}]
          },
          'attr4_referent': {
              'name': 'status',
              'restrictions': [{'cred_def_id': faber['transcript_cred_def_id']}]
          },
          'attr5_referent': {
              'name': 'ssn',
              'restrictions': [{'cred_def_id': faber['transcript_cred_def_id']}]
          },
          'attr6_referent': {
              'name': 'phone_number'
          }
      },
      'requested_predicates': {
          'predicate1_referent': {
              'name': 'average',
              'p_type': '>=',
              'p_value': 4,
              'restrictions': [{'cred_def_id': faber['transcript_cred_def_id']}]
          }
      }
  })
```

일부 속성은 검증 가능하고 일부는 불가능합니다.

증명 요청에 따르면 자격 증명의 SSN, 학위 및 졸업 상태는 발급자와 schema_key가 공식적으로 주장해야 합니다. 또한 first_name, last_name 및 phone_number는 확인할 필요가 없습니다. 이러한 자격 증명에 확인 가능한 상태로 태그를 지정하지 않음으로써 Acme의 자격 증명 요청은 그녀의 이름과 전화 번호에 대한 Alice의 자격 증명을 수락할 것이라고 말합니다.

**Alice가 작업 응용 프로그램** 증명 요청 에 대한 Proof 생성에 사용할 수 있는 자격 증명을 표시하려면 Alice가 호출합니다 `anoncreds.prover_get_credentials_for_proof_req`.

```
  # Alice Agent
    creds_for_job_application_proof_request = json.loads(
        await anoncreds.prover_get_credentials_for_proof_req(alice['wallet'], alice['job_application_proof_request']))
```

**Alice는 이 채용 지원서** 의 요구 사항을 증명하는 자격 증명을 하나만 가지고 있습니다.

```
  # Alice Agent
  {
    'referent': 'Transcript Credential Referent',
    'attrs': {
        'first_name': 'Alice',
        'last_name': 'Garcia',
        'status': 'graduated',
        'degree': 'Bachelor of Science, Marketing',
        'ssn': '123-45-6789',
        'year': '2015',
        'average': '5'
    },
    'schema_id': job_certificate_schema_id,
    'cred_def_id': faber_transcript_cred_def_id,
    'rev_reg_id': None,
    'cred_rev_id': None
  }
```

이제 Alice는 이러한 속성을 세 그룹으로 나눌 수 있습니다.

1. 공개될 속성 값
2. 표시되지 않을 속성 값
3. 검증 가능한 증거 생성이 필요하지 않은 속성

**Job-Application** Proof Request의 경우 Alice는 속성을 다음과 같이 나눴습니다.

```
  # Alice Agent
    alice['job_application_requested_creds'] = json.dumps({
        'self_attested_attributes': {
            'attr1_referent': 'Alice',
            'attr2_referent': 'Garcia',
            'attr6_referent': '123-45-6789'
        },
        'requested_attributes': {
            'attr3_referent': {'cred_id': cred_for_attr3['referent'], 'revealed': True},
            'attr4_referent': {'cred_id': cred_for_attr4['referent'], 'revealed': True},
            'attr5_referent': {'cred_id': cred_for_attr5['referent'], 'revealed': True},
        },
        'requested_predicates': {'predicate1_referent': {'cred_id': cred_for_predicate1['referent']}}
    })
```

또한 Alice는 Credential Request 생성에 사용된 단계와 동일한 방식으로 각 사용된 Credential에 대한 Credential Schema 및 해당 Credential Definition을 가져와야 합니다.

이제 Alice는 **Acme Job-Application** Proof Request에 대한 Proof를 생성할 수 있는 모든 것을 갖게 되었습니다.

```
  # Alice Agent
  alice['apply_job_proof'] = \
        await anoncreds.prover_create_proof(alice['wallet'], alice['job_application_proof_request'], alice['job_application_requested_creds'],
                                            alice['master_secret_id'], alice['schemas'], alice['cred_defs'], alice['revoc_states'])
```

**Acme** 는 수신된 증명을 검사 할 때 다음 구조를 보게 됩니다.

```
  # Acme Agent
  {
      'requested_proof': {
          'revealed_attrs': {
              'attr4_referent': {'sub_proof_index': 0, 'raw':'graduated', 'encoded':'2213454313412354'},
              'attr5_referent': ['sub_proof_index': 0, 'raw':'123-45-6789', 'encoded':'3124141231422543541'},
              'attr3_referent': ['sub_proof_index': 0, 'raw':'Bachelor of Science, Marketing', 'encoded':'12434523576212321'}
          },
          'self_attested_attrs': {
              'attr1_referent': 'Alice',
              'attr2_referent': 'Garcia',
              'attr6_referent': '123-45-6789'
          },
          'unrevealed_attrs': {},
          'predicates': {
              'predicate1_referent': {'sub_proof_index': 0}
          }
      },
      'proof' : [] # Validity Proof that Acme can check
      'identifiers' : [ # Identifiers of credentials were used for Proof building
          {
            'schema_id': job_certificate_schema_id,
            'cred_def_id': faber_transcript_cred_def_id,
            'rev_reg_id': None,
            'timestamp': None
          }
      }
  }
```

**Acme** 는 요청된 모든 속성을 얻었습니다. 이제 **Acme** 는 Validity Proof를 확인하려고 합니다. 이를 수행하려면 **Acme** 는 먼저 Alice가 수행한 것과 동일한 방식으로 Proof에 제공된 각 식별자에 대한 모든 Credential Schema 및 해당 Credential Definition을 가져와야 합니다. 이제 **Acme** 는 Alice **의 Job-Application** Proof 를 확인할 수 있는 모든 것을 갖추고 있습니다.

```
 # Acme Agent
 assert await anoncreds.verifier_verify_proof(acme['job_application_proof_request'], acme['apply_job_proof'],
                                              acme['schemas'], acme['cred_defs'], acme['revoc_ref_defs'], acme['revoc_regs'])
```

여기에서는 지원서가 수락되고 Alice가 일자리를 얻는다고 가정하겠습니다. **Acme** 는 Alice에 대한 새로운 자격 증명 제안을 생성합니다.

```
  # Acme Agent
  acme['job_certificate_cred_offer'] = await anoncreds.issuer_create_credential_offer(acme['wallet'], acme['job_certificate_cred_def_id'])
```

Alice가 Acme와의 연결을 검사할 때 새 자격 증명 제안을 사용할 수 있음을 확인합니다.



### 2.6. 대출 신청

이제 Alice는 직업을 얻었으므로 대출을 신청하고 싶습니다. 재직증명서가 필요합니다. 그녀는 Acme에서 제공 하는 **Job-Certificate** 자격 증명 에서 이를 얻을 수 있습니다 . 앨리스는 친숙한 상호작용의 순서를 거칩니다.

1. 먼저 그녀는 자격 증명 요청을 만듭니다.

```
 # Alice Agent
   (alice['job_certificate_cred_request'], alice['job_certificate_cred_request_metadata']) = \
       await anoncreds.prover_create_credential_req(alice['wallet'], alice['did_for_acme'], alice['job_certificate_cred_offer'],
                                                    alice['acme_job_certificate_cred_def'], alice['master_secret_id'])
```

1. Acme 는 Alice에 대한 **Job-Certificate** Credential을 발급합니다.

```
 # Acme Agent
 alice_job_certificate_cred_values_json = json.dumps({
     "first_name": {"raw": "Alice", "encoded": "245712572474217942457235975012103335"},
     "last_name": {"raw": "Garcia", "encoded": "312643218496194691632153761283356127"},
     "employee_status": {"raw": "Permanent", "encoded": "2143135425425143112321314321"},
     "salary": {"raw": "2400", "encoded": "2400"},
     "experience": {"raw": "10", "encoded": "10"}
 })
```

여기에서 Faber의 Transcript 사용과의 한 가지 차이점은 **Job-Certificate** 를 취소할 수 있고 자격 증명을 만들 때 **Acme** 가 이전에 만든 해지 레지스트리의 ID 와 유효성 꼬리를 포함하는 blob 저장소에 대한 핸들을 사용한다는 것입니다.

```
    # Acme Agent
    acme['blob_storage_reader_cfg_handle'] = await blob_storage.open_reader('default', acme['tails_writer_config'])
    acme['job_certificate_cred'], acme['job_certificate_cred_rev_id'], acme['alice_cert_rev_reg_delta'] = \
        await anoncreds.issuer_create_credential(acme['wallet'], acme['job_certificate_cred_offer'],
                                                 acme['job_certificate_cred_request'],
                                                 acme['job_certificate_cred_values'],
                                                 acme['revoc_reg_id'],
                                                 acme['blob_storage_reader_cfg_handle'])
```

또한 **Acme** 는 다른 당사자가 나중에 자격 증명의 해지 상태를 확인할 수 있도록 원장에 해지 레지스트리 항목을 게시해야 합니다.

```
    # Acme agent
    acme['revoc_reg_entry_req'] = \
        await ledger.build_revoc_reg_entry_request(acme['did'], acme['revoc_reg_id'], 'CL_ACCUM',
                                                   acme['alice_cert_rev_reg_delta'])
    await ledger.sign_and_submit_request(acme['pool'], acme['wallet'], acme['did'], acme['revoc_reg_entry_req'])
```

**Alice** 가 **Acme 로부터** **Job-Certificate** 자격 증명을 받으면 자격 증명을 저장하기 전에 Ledger에서 해지 레지스트리 정의를 요청해야 합니다.

```
    # Alice Agent
    alice['acme_revoc_reg_des_req'] = \
        await ledger.build_get_revoc_reg_def_request(alice['did_for_acme'],
                                                     alice_job_certificate_cred['rev_reg_id'])
    alice['acme_revoc_reg_des_resp'] = await ledger.submit_request(alice['pool'], alice['acme_revoc_reg_des_req'])
    (alice['acme_revoc_reg_def_id'], alice['acme_revoc_reg_def_json']) = \
        await ledger.parse_get_revoc_reg_def_response(alice['acme_revoc_reg_des_resp'])
```

이제 **Job-Certificate** Credential이 발급되었으며 Alice가 소유하고 있습니다. Alice는 **Job-Certificate** Credential을 지갑에 저장합니다.

```
  # Alice Agent
  await anoncreds.prover_store_credential(alice['wallet'], None, alice['job_certificate_cred_request_metadata'],
                                          alice['job_certificate_cred'], alice['acme_job_certificate_cred_def'], alice['acme_revoc_reg_def_json'])
```

그녀는 그녀가 대출을 신청할 때 그것을 사용할 수 있습니다. 마치 그녀가 직업을 신청할 때 성적표를 사용한 것과 같은 방식입니다.

그러나 데이터 공유에 대한 이러한 접근 방식에는 단점이 있습니다. — 엄격하게 필요한 것보다 더 많은 데이터가 공개될 수 있습니다. Alice가 해야 할 일은 고용 증명을 제공하는 것뿐이라면 대신 익명의 자격 증명으로 이를 수행할 수 있습니다. 익명 자격 증명은 실제 값을 공개하지 않고 특정 술어를 증명할 수 있습니다(예: Alice는 정규직으로 고용되어 X보다 큰 급여와 고용 날짜를 표시하지만 실제 급여는 숨겨져 있음). Faber College와 Acme Corp의 자격 증명에서 필요한 것만 공개하는 복합 증명을 만들 수 있습니다.

Alice는 이제 Thrift Bank와 연결을 설정합니다.

Alice는 Thrift Bank로부터 다음과 같은 **Loan-Application-Basic Proof Request를 받습니다.**

```
  # Thrift Agent
  thrift['apply_loan_proof_request'] = json.dumps({
      'nonce': '123432421212',
      'name': 'Loan-Application-Basic',
      'version': '0.1',
      'requested_attributes': {
          'attr1_referent': {
              'name': 'employee_status',
              'restrictions': [{'cred_def_id': acme_job_certificate_cred_def_id}]
          }
      },
      'requested_predicates': {
          'predicate1_referent': {
              'name': 'salary',
              'p_type': '>=',
              'p_value': 2000,
              'restrictions': [{'cred_def_id': acme_job_certificate_cred_def_id}]
          },
          'predicate2_referent': {
              'name': 'experience',
              'p_type': '>=',
              'p_value': 1,
              'restrictions': [{'cred_def_id': acme_job_certificate_cred_def_id}]
          }
      },
      'non_revoked': {'to': int(time.time())}
  })
```

마지막 줄은 제공된 *Job-Certificate* 가 지원 시간까지 취소되지 않아야 함을 나타냅니다.

**Alice는 이 대출 신청 기본** 증명 요청 에 대한 증명 요구 사항을 충족하는 자격 증명이 하나만 있습니다.

```
  # Alice Agent
  {
      'referent': 'Job-Certificate Credential Referent',
      'revoc_reg_seq_no': None,
      'schema_id': job_certificate_schema_id,
      'cred_def_id': acme_job_certificate_cred_def_id,
      'attrs': {
          'employee_status': 'Permanent',
          'last_name': 'Garcia',
          'experience': '10',
          'first_name': 'Alice',
           'salary': '2400'
      }
  }
```

**대출 신청 기본** 증명 요청 앨리스 는 속성을 다음과 같이 나눴습니다. 그녀는 Ledger에서 쿼리한 해지 상태에서 각 속성에 대한 유효성 타임스탬프를 얻을 수 있습니다.

```
  # Alice Agent
  revoc_states_for_loan_app = json.loads(alice['revoc_states_for_loan_app'])
        timestamp_for_attr1 = await get_timestamp_for_attribute(cred_for_attr1, revoc_states_for_loan_app)
        timestamp_for_predicate1 = await get_timestamp_for_attribute(cred_for_predicate1, revoc_states_for_loan_app)
        timestamp_for_predicate2 = await get_timestamp_for_attribute(cred_for_predicate2, revoc_states_for_loan_app)
        alice['apply_loan_requested_creds'] = json.dumps({
            'self_attested_attributes': {},
            'requested_attributes': {
                'attr1_referent': {'cred_id': cred_for_attr1['referent'], 'revealed': True, 'timestamp': timestamp_for_attr1}
            },
            'requested_predicates': {
                'predicate1_referent': {'cred_id': cred_for_predicate1['referent'], 'timestamp': timestamp_for_predicate1},
                'predicate2_referent': {'cred_id': cred_for_predicate2['referent'], 'timestamp': timestamp_for_predicate2}
            }
        })
```

Alice는 **Loan-Application-Basic** Proof Request에 대한 Proof를 생성합니다.

```
  # Alice Agent
  alice['apply_loan_proof'] = \
            await anoncreds.prover_create_proof(alice['wallet'], alice['apply_loan_proof_request'],
                                                alice['apply_loan_requested_creds'], alice['master_secret_id'],
                                                alice['schemas_for_loan_app'], alice['cred_defs_for_loan_app'],
                                                alice['revoc_states_for_loan_app'])
```

Alice는 은행 에 **Loan-Application-Basic 증명만 보냅니다.** 이를 통해 그녀는 현재 기본 자격을 증명하는 것이 전부일 때 공유해야 하는 PII(개인 식별 정보)를 최소화할 수 있습니다.

**Thrift** 가 받은 증명을 검사 할 때 다음 구조를 볼 수 있습니다.

```
  # Thrift Agent
  {
      'requested_proof': {
          'revealed_attrs': {
              'attr1_referent': {'sub_proof_index': 0, 'raw': 'Permanent', 'encoded':'2143135425425143112321314321'},
          },
          'self_attested_attrs': {},
          'unrevealed_attrs': {},
          'predicates': {
              'predicate1_referent': {'sub_proof_index': 0},
              'predicate2_referent': {'sub_proof_index': 0}
          }
      },
      'proof' : [] # Validity Proof that Thrift can check
      'identifiers' : [ # Identifiers of credentials were used for Proof building
          'schema_id': acme['job_certificate_schema_id'],
          'cred_def_id': acme['job_certificate_cred_def_id'],
          'rev_reg_id': acme['revoc_reg_id'],
          'timestamp': 1550503925 # A integer timestamp
      ]
  }
```

**Thrift Bank** 는 Alice 의 **대출 신청 기본** 증명을 성공적으로 확인했습니다 .

```
  # Thrift Agent
  assert await anoncreds.verifier_verify_proof(thrift['apply_loan_proof_request'],
                                                 thrift['alice_apply_loan_proof'],
                                                 thrift['schemas_for_loan_app'],
                                                 thrift['cred_defs_for_loan_app'],
                                                 thrift['revoc_defs_for_loan_app'],
                                                 thrift['revoc_regs_for_loan_app'])
```

Thrift Bank는 Alice가 은행과 개인 정보를 공유해야 하는 두 번째 Proof Request를 보냅니다.

```
  # Thrift Agent
  thrift['apply_loan_kyc_proof_request'] = json.dumps({
      'nonce': '123432421212',
      'name': 'Loan-Application-KYC',
      'version': '0.1',
      'requested_attributes': {
          'attr1_referent': {'name': 'first_name'},
          'attr2_referent': {'name': 'last_name'},
          'attr3_referent': {'name': 'ssn'}
      },
      'requested_predicates': {}
  })
```

Alice는 이 **Loan-Application-KYC** Proof Request에 대한 증명 요구 사항을 충족하는 두 가지 자격 증명을 가지고 있습니다.

```
  # Alice Agent
  {
    'referent': 'Transcript Credential Referent',
    'schema_id': transcript_schema_id,
    'cred_def_id': faber_transcript_cred_def_id,
    'attrs': {
        'first_name': 'Alice',
        'last_name': 'Garcia',
        'status': 'graduated',
        'degree': 'Bachelor of Science, Marketing',
        'ssn': '123-45-6789',
        'year': '2015',
        'average': '5'
    },
    'rev_reg_id': None,
    'cred_rev_id': None
  },
  {
      'referent': 'Job-Certificate Credential Referent',
      'schema_key': job_certificate_schema_id,
      'cred_def_id': acme_job_certificate_cred_def_id,
      'attrs': {
          'employee_status': 'Permanent',
          'last_name': 'Garcia',
          'experience': '10',
          'first_name': 'Alice',
          'salary': '2400'
      },
      'rev_reg_id': None,
      'revoc_reg_seq_no': None
  }
```

**Loan-Application-KYC** Proof Request 의 경우 Alice는 속성을 다음과 같이 나눴습니다.

```
  # Alice Agent
  alice['apply_loan_kyc_requested_creds'] = json.dumps({
      'self_attested_attributes': {},
      'requested_attributes': {
          'attr1_referent': {'cred_id': cred_for_attr1['referent'], 'revealed': True},
          'attr2_referent': {'cred_id': cred_for_attr2['referent'], 'revealed': True},
          'attr3_referent': {'cred_id': cred_for_attr3['referent'], 'revealed': True}
      },
      'requested_predicates': {}
  })
```

Alice는 **Loan-Application-KYC** Proof Request에 대한 Proof를 생성합니다.

```
  # Alice Agent
  alice['apply_loan_kyc_proof'] = \
      await anoncreds.prover_create_proof(alice['wallet'], alice['apply_loan_kyc_proof_request'], alice['apply_loan_kyc_requested_creds'],
                                          alice['alice_master_secret_id'], alice['schemas'], alice['cred_defs'], alice['revoc_states'])
```

**Thrift** 가 받은 증명을 검사 할 때 다음 구조를 볼 수 있습니다.

```
  # Thrift Agent
  {
      'requested_proof': {
          'revealed_attributes': {
              'attr1_referent': {'sub_proof_index': 0, 'raw':'123-45-6789', 'encoded':'3124141231422543541'},
              'attr1_referent': {'sub_proof_index': 1, 'raw':'Alice', 'encoded':'245712572474217942457235975012103335'},
              'attr1_referent': {'sub_proof_index': 1, 'raw':'Garcia', 'encoded':'312643218496194691632153761283356127'},
          },
          'self_attested_attrs': {},
          'unrevealed_attrs': {},
          'predicates': {}
      },
      'proof' : [] # Validity Proof that Thrift can check
      'identifiers' : [ # Identifiers of credentials were used for Proof building
          {
            'schema_id': transcript_schema_id,
            'cred_def_id': faber['transcript_cred_def_id'],
            'rev_reg_id': None,
            'timestamp': None
          },
          {
            'schema_key': job_certificate_schema_id,
            'cred_def_id': acme['job_certificate_cred_def_id'],
            'rev_reg_id': None,
            'timestamp': None
          }
      ]
  }
```

**Thrift Bank 는 Alice의** **Loan-Application-KYC** Proof를 성공적으로 검증했습니다 .

```
  # Thrift Agent
  assert await anoncreds.verifier_verify_proof(thrift['apply_loan_kyc_proof_request'], thrift['alice_apply_loan_kyc_proof'],
                                               thrift['schemas'], thrift['cred_defs'], thrift['revoc_defs'], thrift['revoc_regs'])
```

Alice의 증명은 모두 성공적으로 확인되었으며 그녀는 **Thrift Bank** 에서 대출을 받았습니다 .



## 앨리스는 직장을 그만둔다

나중에 **Alice** 는 직장을 그만두기로 결정하고 **Acme 는** **Job-Certificate** 자격 증명 을 취소합니다 .

```
    # Acme Agent
    await anoncreds.issuer_revoke_credential(acme['wallet'],
                                             acme['blob_storage_reader_cfg_handle'],
                                             acme['revoc_reg_id'],
                                             acme['job_certificate_cred_rev_id'])
```

**그런 다음 Acme** 는 원장에서 해지를 게시 `ledger.build_revoc_reg_entry_request`하고 `ledger.sign_and_submit_request`.

**Alice가 대출( Loan-Application-Basic** )을 다시 신청하려고 하면 증명 검증이 실패합니다.



## 코드 살펴보기

이제 외부에서 Libindy 구현이 어떻게 작동하는지 볼 수 있는 기회를 얻었으므로 코드에서 아래에서 어떻게 작동하는지 보고 싶습니까? 그렇다면 [Jupiter에서 시작하기 시뮬레이션을](https://hyperledger-indy.readthedocs.io/projects/sdk/en/latest/docs/getting-started/run-getting-started.html) 실행하십시오 . 이 링크를 보려면 GitHub에 로그인해야 할 수 있습니다. [또한 여기](https://github.com/hyperledger/indy-sdk/blob/master/samples/python/src/getting_started.py) 에서 소스 코드를 찾을 수 있습니다 .

데모를 실행할 때 오류가 발생하면 [문제 해결 가이드](https://hyperledger-indy.readthedocs.io/projects/sdk/en/latest/docs/getting-started/Trouble_shoot_GSG.html) 를 확인하십시오 .



## 주요 컨셉



## 1. Revocation (해지)

### 자격 증명 해지 작동 방식

이 문서는 개념적 수준에서 자격 증명 해지를 설명하는 것을 목표로 합니다. 이 문서가 여전히 너무 수준이 낮은 것 같다면 오프셋 0:30에서 4:30 사이에 [이 소개 비디오 를 시청하는 것이 좋습니다.](https://drive.google.com/open?id=1FxdgkYwwLfpln6MnsZJAwnYjM6LpCoP0)



### 배경: 암호화 누산기

**메커니즘을 자세히 설명하기 전에 암호화 누산기** 를 매우 높은 수준에서 이해할 필요가 있습니다. 우리는 설명에서 어려운 수학을 피하려고 노력할 것입니다.

누산기는 많은 숫자를 곱한 결과로 생각할 수 있습니다. 방정식 에서 누산기는 다음 과 같습니다 . 각각의 새로운 요소가 곱해지면 값 이 *누적 됩니다. 숫자를 연결할 수 있습니다.* =2 및 =3 및 =5 및 =7 이면 누산기 는 210의 값을 갖습니다. 이 값이 있으면 3이 인수이기 때문에 "in"이라고 말합니다 . 누산기에서 3을 빼려면 210을 3으로 나누고 70을 얻습니다(=2 * 5 * 7). 3은 이제 "제거"되었습니다.`a * b * c * d =` **`e`****`e`**`a``b``c``d`**`e`**`e``e`

다른 모든 요인의 곱( ) **`e`**과 같이 단일 요인을 곱하여 생성할 수도 있습니다 .`a``b * c * d`

누적기에 기여 하는 *다른* 요소(이 자격 증명에 대한 개인 요소를 제외한 모든 요소 )의 곱을 **증인** 이라고 합니다 .

이것은 유용한 특성입니다. `a`이는 누산기에 대한 다른 모든 입력의 값 과 곱을 다른 사람에게 말할 수 *있지만 다른 입력 자체* 는 말할 수 없으며 출력을 생성할 수 있음을 의미합니다.



### 배경: 꼬리 파일

위의 간단한 예에서는 4개의 요소만 가지고 있으며 작은 숫자를 사용하고 있습니다. 우리는 또한 곱셈을 나누어서 반대로 할 수 있는 표준 산술을 사용하고 있습니다. 이러한 시스템에서 누산기의 내용은 간단한 소인수 분해에 의해 역설계될 수 있습니다.

해지에 유용하기 위해 Indy의 누산기는 되돌릴 수 없습니다. 즉, 누산기 값을 도출하는 유일한 방법은 요인을 아는 것이어야 합니다. 우리는 모듈식 산술(나눗셈이 정의되지 않은 경우)을 사용하고 인수에 막대한 소수를 사용하여 매우 긴 정수 **감시** 를 통해 이를 수행 합니다.

**tails 파일** 은 누산기 및 해당 요소와 연결됩니다. 누산기에 대해 무작위로 생성된 요소의 배열을 포함하는 이진 파일입니다. 2, 3, 7과 같은 작은 숫자 대신 이러한 요소는 화면에 편리하게 표시하기에는 너무 큰 엄청난 숫자입니다. 일반적으로 tails 파일에 있는 이러한 숫자 요소의 양은 수십만에서 수천만으로 많습니다.

tails 파일은 비밀이 아닙니다. 일반 텍스트로 세상에 공개되며 누구나 무료로 다운로드할 수 있습니다. 이 파일의 내용은 절대 변경되지 않습니다.

특정 발급자가 발급한 각 잠재적 또는 실제 자격 증명에는 tails 파일의 누적 요소에 대한 인덱스가 할당됩니다. 그러나 취소되지 않은 자격 증명만 누산기 값에 기여합니다. 아래에서 이것이 어떻게 작동하는지 볼 것입니다.

![tails 파일 및 누산기](https://hyperledger-indy.readthedocs.io/projects/sdk/en/latest/_images/tails.png)



### 설정

취소 가능한 자격 증명이 발급되기 전에 생태계에 대해 여러 가지 사실이 충족되어야 합니다.

1. 각 자격 증명 유형에 대한 **스키마** 를 원장에 작성해야 합니다. 예를 들어 회사에서 고용 증명서를 발급하려는 경우 "직원 자격 증명" 스키마를 게시해야 합니다. 마찬가지로, 출생 증명서 자격 증명이 발급되기 전에 "출생 증명서" 스키마를 정의하고 대중에게 공개해야 합니다. 여러 발급자가 동일한 스키마를 참조할 수 있습니다. 스키마는 시간이 지남에 따라 버전이 지정되고 발전될 수 있습니다. 모든 개인이나 기관은 원장에 스키마를 작성할 수 있습니다. 특별한 권한이 필요하지 않습니다.
2. 각 발급자는 생성하려는 각 자격 증명 유형에 대해 하나의 **자격 증명 정의** 를 원장에 게시해야 합니다 . 정의는 특정 스키마와 일치하는 자격 증명을 생성하려는 발급자의 의도를 알리고 발급자가 이러한 자격 증명에 서명하는 데 사용할 키를 지정합니다. (발급자의 DID를 인증하는 데 사용되는 verkey+ 서명 키 쌍은 자격 증명에 서명하는 데 사용되는 키와 별도로 유지해야 각 키 쌍이 독립적으로 회전할 수 있습니다. 시스템 관리자가 DID 키 쌍을 회전하고 실수로 모든 자격 증명을 무효화하면 좋지 않습니다. 기관에서 발행한…)
3. **또한 각 발급자는 원장에 해지 레지스트리** 를 게시해야 합니다 . 이 메타데이터는 자격 증명 정의를 참조하고 해당 자격 증명 유형에 대한 해지가 처리되는 방법을 지정합니다. 해지 레지스트리는 해지를 테스트하는 데 사용할 수 있는 암호화 **누산기 를 알려주고 관련** **테일 파일** 의 URI 및 해시를 제공 합니다 .
4. 각 발급자는 모든 관련 자격 증명에 대한 해지 상태를 설명하는 누산기 값을 원장에 게시해야 합니다. 이 누산기는 주기적으로 또는 필요에 따라 업데이트해야 합니다. 예를 들어, 운전 면허증 부서가 주어진 근무일 동안 3개의 면허증을 취소하고 오후 5시에 문을 닫을 때 운전 면허증 자격 증명에 대한 누산기 값을 업데이트하는 원장 트랜잭션을 발행하여 취소된 자격 증명 3개를 다음에서 제거할 수 있습니다. 축전지. "제거"가 의미하는 것은 위에서 설명한 대로입니다. 해지된 자격 증명 3개와 관련된 인덱스에 대한 테일 파일에 나열된 요소가 더 이상 누적기에 곱해지지 않습니다.

![취소 전과 후](https://hyperledger-indy.readthedocs.io/projects/sdk/en/latest/_images/before-and-after.png)



### 해지 테스트 방법

훨씬 나중에 일어날 필요가 있는 일에 대해 생각하기 위해 이제 건너뛰도록 합시다. 증명자가 검증자에게 증명을 제공할 때 우리는 일반적으로 증명을 핵심 정보 요구 사항에 중점을 둔 것으로 생각합니다. *귀하의 생년월일은 무엇입니까?* *주소를 공개하십시오* . 이것이 **1차 증거** 입니다.

그러나 필요한 또 다른 차원의 증명이 있습니다. *증명자는 기본 증명 뒤에 있는 자격 증명이 취소되지 않았음을 입증해야 합니다.* **이것을 철회하지 않는다는 증거** 라고 합니다.

Indy에서는 증명자가 자신이 알고 있는 누산기의 계수와 다른 모든 요인의 곱을 사용하여 자격 증명에 대한 누산기의 값을 도출할 수 있음을 증명자가 보여줌으로써 취소되지 않음을 증명할 수 있습니다. 검증자는 증명자가 올바른 답을 생성한다는 것을 알 수 있지만(답이 원장에 있기 때문에), 증명자가 그것을 도출한 방법에 대한 특정 세부 사항은 모릅니다. 발급자는 증명자를 패배시키는 방식으로 수학 문제의 답을 변경하여 취소할 수 있습니다.



### 발급 시 취소 준비

자격 증명이 발급되면 실제 자격 증명 파일이 소유자(나중에 증명자가 됨)에게 전송됩니다. 또한 발급자는 다른 두 가지 중요한 정보를 전달합니다.

- tails 파일에서 이 자격 증명에 해당하는 인덱스입니다. 이를 통해 보유자는 개인 요소를 조회할 수 `a`있으며 문서 상단의 누적기 배경 섹션에서 간단한 방정식으로 매핑할 수 있습니다.
- 증인.



### 철회 불가 증명 제시

증명자가 자신의 자격 증명이 취소되지 않았음을 입증해야 할 때 그녀는 자신의 비공개 요소와 증인을 사용하여 원장의 누산기 값을 도출하는 수학을 제공할 수 있음을 보여줍니다. 그녀는 자신의 사적 가치가 무엇인지 실제로 공개하지 않고 이를 수행합니다. 이것은 상관 관계를 피하기 위해 중요합니다.

그러나 합병증이 있습니다. 자격 증명이 발급된 이후로 누산기의 값이 변경된 경우에는 어떻게 됩니까? 이 경우 사적 요소 곱하기 증인은 누적기와 같지 않습니다...

이는 동일한 트랜잭션의 일부로 **감시 델타 도 게시하기 위해 누산기 업데이트를 요구함으로써 처리됩니다.** 이것은 증명자에게 증거(공개 테일 파일의 다른 인덱스 참조)를 조정하여 누산기의 현재 값과 조화를 이루는 방법을 알려줍니다. 증인을 업데이트하려면 증명자(검증자가 아님)가 꼬리 파일을 다운로드해야 합니다.



### 함께 모아서

이 토론에서 일부 세부 정보가 표시되지 않았습니다. 수학이 단순화되었으며 발급자가 다중 테일 파일 및 해지 레지스트리에 대처하는 방법 또는 이것이 바람직한 이유에 대해서는 논의하지 않았습니다. 그러나 메커니즘의 광범위한 흐름이 분명해야 하며 이제 해당 기능을 요약하기 쉽습니다.

- 발급자는 원장의 번호를 변경하여 취소합니다. 그들은 그들이 선택한 요소를 포함하거나 포함하지 않는 수학 문제에 대한 답을 변경하기 때문에 단일 트랜잭션에서 원하는 만큼의 자격 증명을 취소할 수 있습니다. 발급자는 취소하기 위해 증명자 또는 검증자에게 연락할 필요가 없습니다. 변경 사항은 전 세계적으로 발생하며 누산기 업데이트 트랜잭션이 원장에 나타나는 즉시 발생합니다.
- 취소는 되돌릴 수 있습니다.
- 증명자는 개인 정보를 보호하는 방식으로 비해지 증명을 보여줍니다. 자격 증명 ID 또는 꼬리 인덱스와 같은 것으로 상관될 수 없습니다. 이것은 테스트를 위해 상관 관계가 필요한 해지 목록 접근 방식과 근본적으로 다릅니다.
- 비해지 증명의 검증은 매우 쉽고 저렴합니다. 검증자는 꼬리 파일이 필요하지 않으며 계산도 간단합니다. 비 해지를 증명하는 것은 증명자에게 다소 비싸지 만 지나치게 복잡하지도 않습니다.
- 검증자는 해지를 테스트하기 위해 발급자에게 연락하거나 해지 목록을 참조할 필요가 없습니다.

------



## 2. Wallets (지갑)

### Indy-SDK 기본 지갑 구현

이 구현의 목적은 **indy-sdk** 용 기본 암호화 지갑을 제공하는 것 입니다.

**indy-sdk** 기본 지갑 구현은 강화된 버전의 [SQLCipher](https://www.zetetic.net/sqlcipher/) 를 사용 합니다 .

- HMAC-SHA1 대신 HMAC-SHA256.
- PBKDF2 100K는 64K 대신 암호 키 데이터에 대해 반올림합니다.
- 2 대신 HMAC 키 파생을 위한 PBKDF2 10 라운드.
- 페이지 크기는 1K 대신 2K입니다.



### 주요 자격 증명

기본 지갑은 데이터 암호화에 사용되는 선택적 암호를 허용합니다. 키를 비워 두거나 키를 생략하여 암호가 제공되지 않으면 지갑이 암호화되지 않고 [SQLite3](https://www.sqlite.org/index.html) 형식으로 저장됩니다. 지갑을 여는 암호는 **indy-sdk** 외부에 저장 되며 HSM, TEE 또는 오프라인 방법과 같은 소비자의 보안 선호도에 맡겨집니다.

**indy-sdk** 는 지갑을 열거나 생성하기 위한 JSON 매개변수인 *자격 증명 을 지원합니다.*

```
{
   "key": "<passphrase>"
   "rekey": "<passphrase>"
}
```

*자격 증명* 매개 변수 가 생략되거나 *키* 가 빈 문자열인 경우 지갑은 암호화되지 않은 상태로 남습니다.

*키* 는 지갑을 여는 암호이며 10만 라운드의 PBKDF2를 실행합니다.

*rekey* 가 제공 되면 *키* 를 사용하여 지갑을 열고 향후 공개 통화를 위해 암호를 *rekey 값으로 변경합니다.* *rekey* 는 기존 지갑에만 필요하며 새 지갑을 만들려고 하면 오류가 발생합니다.

*[ null* , *"" ]에* *rekey* 가 포함되어 있으면 지갑이 복호화됩니다.

*key* 가 [ *null* , *""* ]이고 *rekey* 에 비어 있지 않은 값이 포함되어 있으면 이제 지갑이 암호화됩니다 .



#### 참고

*rekey 는* *키* 를 변경할 때만 필요 합니다 . 그렇지 않으면 생략해야 합니다.



### 케이스

#### 일반 지갑 / 암호 없음

암호화되지 않은 지갑을 생성하려면 *자격 증명* 이 비어 있거나 지정되지 않을 수 있습니다. *키 는 [* *null* , *""* ] 일 수도 있습니다 .



#### 암호화된 지갑 / A 키

암호화된 지갑은 *키* 필드에 암호를 지정해야 합니다. 암호는 공백이 아닌 모든 값이 될 수 있습니다.

```
{
   "key": "Th1sIsArEALLY$3cuR3PassW0RD"
}
```



#### 일반 지갑에서 암호화 지갑으로 / 키 추가

암호화되지 않은 기존 지갑에 암호화를 추가하려면 *키* 를 [ *null* , *""* ]로 설정하고 *rekey* 를 유효한 암호로 설정해야 합니다. *그러면 지갑 오픈 콜은 암호화된 지갑* 섹션 과 동일합니다 .

```
{
   "key": null,
   "rekey": "il0V3MyN3WpA$SworD"
}
```



#### 암호화된 지갑에서 일반 지갑으로 / 키 제거

기존의 암호화된 지갑에서 암호화를 제거하려면 *키* 를 현재 값으로 설정하고 *rekey* 를 공백 값[ *null* , *""* ]으로 설정해야 합니다. *그러면 지갑 오픈 콜은 일반 지갑* 섹션 과 동일합니다 .

```
{
   "key": "Th1sIsArEALLY$3cuR3PassW0RD",
   "rekey": null
}
```



#### 암호 업데이트 / 키 변경

지갑 암호를 회전하는 것이 좋습니다. *key* 는 현재 값으로 설정되어야 하고 *rekey* 는 새 값으로 설정되어야 합니다. *그러면 지갑 오픈 콜은 암호화된 지갑* 섹션 과 동일합니다 .

```
{
   "key": "Th1sIsArEALLY$3cuR3PassW0RD",
   "rekey": "s8c0R31tYi$hARd"
}
```