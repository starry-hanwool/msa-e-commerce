package me.hanwool.mallutilapp.value;

import lombok.Getter;

@Getter
public enum ResponseCode {
    FAIL(-701, "해당 요청에 실패하였습니다"),
    NOT_FOUND(-704, "해당건이 없습니다."),
    INVALID_REQUEST(-705, "권한이 없습니다.");

    public final int code;
    public final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
