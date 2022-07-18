package com.bpnsolution.hyperledgerindydemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDidRequestDto {

	private String poolName;
	private String seed;
	private String walletId;
	private String walletKey;

}
