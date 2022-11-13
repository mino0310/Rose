package com.example.myfirstmac.request;


import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class UserSearch {

    private static final int MAX_SIZE = 2000;

    private static final int MIN_PAGE = 1;

    // Builder 에서 기본값을 지정해주는 어노테이션
    // 생성자 레벨에서 사용하면 적용되지 않고 @Builder를 클래스 레벨에 적용해야 기본값이 적용된다.
    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    // 페이지와 사이즈를 지정해주는 것은 컨트롤러 단에서 일일이 계산하는 것보다 별도 처리를 담당하는 클래스의 별도의 메서드로 하는 것이 좋다.
    // 그런 식으로 추상화를 시켜둬야 안에서 로직이 바뀌더라도 의존없이 처리할 수 있다.
    public long getOffset(){
        return ((long) max(MIN_PAGE, page) - 1) * min(size, MAX_SIZE);
    }
}
