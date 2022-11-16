package com.example.myfirstmac.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code": "400",
 *     "message": "잘못된 요청입니다.",
 *     "validation": {
 *         "title": "값을 입력해주세요"
 *     }
 * }
 */



@Getter
@Setter
// 빈 값은 반환하지 않게 해주는 어노테이션
// 그러나 비어있는 것은 그것대로 중요한 정보가 될 수 있으므로 권장하지는 않음.
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    // 회사마다 팀마다 에러 클래스 정의가 다름

    private final String code;
    private final String message;

    // 초기값이 있으므로 롬복이 생성해주지 않음.
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String field, String defaultMessage) {
        this.validation.put(field, defaultMessage);
    }
   }
