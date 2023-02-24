package com.example.myfirstmac.service;


import com.example.myfirstmac.domain.session.Session;
import com.example.myfirstmac.domain.user.User;
import com.example.myfirstmac.exception.AlreadyEmailExist;
import com.example.myfirstmac.exception.AuthenticateNotValid;
import com.example.myfirstmac.exception.InvalidPostRequest;
import com.example.myfirstmac.exception.UserNotFound;
import com.example.myfirstmac.repository.UserRepository;
import com.example.myfirstmac.request.Login;
import com.example.myfirstmac.request.Signup;
import com.example.myfirstmac.response.SessionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;


    @Transactional
    public Long signin(Login login) {
//        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
//                .orElseThrow(AuthenticateNotValid::new);

//        Session  session = user.addSession();
//        SessionResponse sessionResponse = SessionResponse.builder().accessToken(session.getAccessToken()).build();

        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> {
                    // 아이디 비밀번호가 틀린건지 회원이 존재하지 않는 건지를 노출하게 되면 취약성이 생길 수 있으므로 하나의 예외로 통일.
                    throw new AuthenticateNotValid();
                });

        SCryptPasswordEncoder sCryptPasswordEncoder = new SCryptPasswordEncoder();

        boolean matches = sCryptPasswordEncoder.matches(login.getPassword(), user.getPassword());
        if (matches) {
            return user.getId();
        } else {
            throw new AuthenticateNotValid();
        }
    }

    public void signup(Signup signup) {

        // email 중복 체크
        userRepository.findByEmail(signup.getEmail()).ifPresent(s -> {
            throw new AlreadyEmailExist("email", "이미 존재하는 이메일입니다.");
        });

        // password encryption
        SCryptPasswordEncoder sCryptPasswordEncoder = new SCryptPasswordEncoder();

        String encodedPassword = sCryptPasswordEncoder.encode(signup.getPassword());



        User user = User.builder()
                .name(signup.getName())
                .password(encodedPassword)
                .email(signup.getEmail())
                .build();

        userRepository.save(user);
    }
}
