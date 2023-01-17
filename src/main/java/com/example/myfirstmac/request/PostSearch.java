package com.example.myfirstmac.request;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static java.lang.Math.max;
import static java.lang.Math.min;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class PostSearch {

    private final static int MAX_SIZE = 2000;
    private final static int MIN_PAGE = 1;

    @Builder.Default
    private final Integer page = 1;
    @Builder.Default
    private final Integer size = 10;

    public long getOffset() {
        return Math.min((size == null ? 1 : size), MAX_SIZE) * (Math.max((page == null ? 1 : page), MIN_PAGE) - 1);
//        return ((long) max(MIN_PAGE, page == null ? 1 : page) - 1) * min(size == null ? 1 : size, MAX_SIZE);
    }

}
