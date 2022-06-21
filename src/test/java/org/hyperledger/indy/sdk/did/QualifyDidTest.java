package org.hyperledger.indy.sdk.did;

import org.hyperledger.indy.sdk.IndyIntegrationTestWithSingleWallet;
import org.hyperledger.indy.sdk.did.Did;
import org.hyperledger.indy.sdk.did.DidResults;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class QualifyDidTest extends IndyIntegrationTestWithSingleWallet {

	@Test
	public void qualifyDid() throws Exception {
		DidResults.CreateAndStoreMyDidResult result = Did.createAndStoreMyDid(wallet, "{}").get();
		String did = result.getDid();
		String method = "peer";

		String fullQualifiedDid = Did.qualifyDid(wallet, did, method).get();
		String expectedDid = "did:" + method + ":" + did;
		assertEquals(expectedDid, fullQualifiedDid);
	}
}