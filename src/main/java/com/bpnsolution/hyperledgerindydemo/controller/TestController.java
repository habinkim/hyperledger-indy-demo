package com.bpnsolution.hyperledgerindydemo.controller;

import com.bpnsolution.hyperledgerindydemo.dto.CreateDidRequestDto;
import com.bpnsolution.hyperledgerindydemo.dto.ResponseDto;
import com.bpnsolution.hyperledgerindydemo.service.TestService;
import lombok.RequiredArgsConstructor;
import org.hyperledger.indy.sdk.IndyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

	private final TestService testService;

	@PostMapping("/createDid")
	public ResponseEntity<ResponseDto> createDid(@RequestBody CreateDidRequestDto dto) throws IndyException, IOException, ExecutionException, InterruptedException {
		return testService.createDid(dto);
	}

}
