package com.bpnsolution.hyperledgerindydemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object data; // 결과값

    public String code; // 오류 여부
	public String message; // 메시지

    public ResponseDto(Object data, String message) { // 생성자 (조회 성공 처리)
        this.data = data;
        this.code = "1";
        this.message = message;
    }

    public ResponseDto(String code, String message) { // 생성자 (오류 처리)
        this.data = null;
        this.code = code;
        this.message = message;
    }

    public ResponseDto(String message) { // 생성자 (데이터 삭제 성공 처리)
        this.data = null;
        this.code = "1";
        this.message = message;
    }

    public ResponseEntity<ResponseDto> wrap() { // ResponseEntity로 wrapping하는 메소드

        ResponseEntity<ResponseDto> res = new ResponseEntity<>(HttpStatus.OK);
        res = ResponseEntity.ok(this);

        return res;

    }
    
}