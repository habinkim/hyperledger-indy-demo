package org.hyperledger.indy.sdk.did;

import org.hyperledger.indy.sdk.IndyIntegrationTestWithSingleWallet;
import org.hyperledger.indy.sdk.InvalidStructureException;
import org.hyperledger.indy.sdk.did.Did;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.isA;

public class StoreTheirDidTest extends IndyIntegrationTestWithSingleWallet {

	@Test
	public void testStoreTheirDidWorks() throws Exception {
		Did.storeTheirDid(this.wallet, String.format("{\"did\":\"%s\"}", DID)).get();
	}

	@Test
	public void testCreateMyDidWorksForInvalidIdentityJson() throws Exception {
		thrown.expect(ExecutionException.class);
		thrown.expectCause(isA(InvalidStructureException.class));

		Did.storeTheirDid(this.wallet, "{\"field\":\"value\"}").get();
	}
}
