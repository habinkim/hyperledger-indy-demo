package com.bpnsolution.hyperledgerindydemo.example;

public class Main {

	public static void main(String[] args) throws Exception {
		Anoncreds.demo();
		AnoncredsRevocation.demo();
		Ledger.demo();
		Crypto.demo();
		Endorser.demo(); // 인증서 서명
		// node 갯수가 몇개인지 확인 필요 (2개 이상이어야 함)
		// alience가 맺어져 있는가?
		// 서명이 필요
		// indy-cli에서
	}
}
