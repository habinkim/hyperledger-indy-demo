package com.bpnsolution.hyperledgerindydemo.practice;

import org.hyperledger.indy.sdk.IndyException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {

	public static void main(String[] args) throws IndyException, IOException, ExecutionException, InterruptedException {
		Step_01_CreateDIDAndDIDdocument.demo();
//		Step_02_UpdateDIDdocument.demo();
		Step_03_CreateSchemaAndCredentialDefinition.demo();
	}

}
