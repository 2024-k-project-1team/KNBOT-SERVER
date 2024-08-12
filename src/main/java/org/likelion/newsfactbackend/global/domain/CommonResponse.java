package org.likelion.newsfactbackend.global.domain;


import lombok.*;
import org.likelion.newsfactbackend.global.domain.enums.ResultCode;

@Data
@NoArgsConstructor
public class CommonResponse<T> {
    private int code;
    private String msg;

    public static<T> CommonResponse<T> success(){
        return CommonResponse.<T>builder()
                .code(ResultCode.OK.getCode())
                .msg(ResultCode.OK.getMessage())
                .build();
    }

    public static<T> CommonResponse<T> fail(ResultCode result){
        return CommonResponse.<T>builder()
                .code(result.getCode())
                .msg(result.getMessage())
                .build();
    }
    @Builder
    public CommonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
