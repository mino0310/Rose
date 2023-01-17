package com.example.myfirstmac.service;


import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.domain.user.UserEditor;
import com.example.myfirstmac.exception.UserNotFound;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.UserCreate;
import com.example.myfirstmac.request.UserEdit;
import com.example.myfirstmac.request.UserSearch;
import com.example.myfirstmac.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long create(UserCreate userCreate) {
        // userCreate -> Entity

//        User user = new User(userCreate.getUserId(), userCreate.getName(), userCreate.getAddress());

        User user = User.builder()
                .userId(userCreate.getUserId())
                .name(userCreate.getName())
                .address(userCreate.getAddress()).build();

        User savedUser = userRepository.save(user);
        Long id = savedUser.getId(); // 식별자 값 반환을 요구할 경우 사용


//        return savedUser;
        return id;
    }

    public UserResponse getUser(Long id) {

        // Java 에서 제공해주는 예외는 구현하고자 하는 비즈니스 로직의 예외를 정확히 표현해주지 못한다. 따라서 구체적인 예외를 생성해줄 필요가 있다.
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        User user = userRepository.findById(id)
                .orElseThrow(UserNotFound::new);

        UserResponse userResponse = UserResponse.builder().id(user.getId()).name(user.getName()).userId(user.getUserId()).address(user.getAddress()).build();
        return userResponse;
    }


    public List<UserResponse> getList(UserSearch userSearch) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("id")));
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
//        return userRepository.findAll(pageable).stream()
//                .map(UserResponse::new)
//                .collect(Collectors.toList());

        return userRepository.getList(userSearch).stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }


    // @Transactional 을 붙이면 종료와 동시에 commit 을 치면서 update문을 날려버린다.
    @Transactional
    public void edit(Long id, UserEdit userEdit) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFound::new);

        // 수정 시에 Setter 를 사용하는 것은 부적합. 추적이 어려워 디버깅이 어렵다.
        // user.setName(userEdit.getName());
        // user.setAddress(userEdit.getAddress());

        UserEditor.UserEditorBuilder userEditorBuilder = user.toEditor();

        UserEditor userEditor = userEditorBuilder
                .name(userEdit.getName() != null ? userEdit.getName() : user.getName())
                .address(userEdit.getAddress() != null ? userEdit.getAddress() : user.getAddress())
                .build();


        user.edit(userEditor);

    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFound::new);

        userRepository.delete(user);
    }
}
