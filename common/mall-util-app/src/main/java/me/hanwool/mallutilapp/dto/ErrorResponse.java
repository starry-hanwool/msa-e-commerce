package me.hanwool.mallutilapp.dto;

import lombok.Data;
import me.hanwool.mallutilapp.value.ResponseCode;

@Data
public class ErrorResponse {

    private int code;
    private String msg;

    public ErrorResponse(ResponseCode responseCode) {
        this.code = responseCode.code;
        this.msg = responseCode.desc;
    }
}
