package com.example.myfirstmac.domain.session;


import com.example.myfirstmac.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.GenerationType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Session {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne
    private User user;

    @Builder
    public Session(User user) {
        this.accessToken = UUID.randomUUID().toString() ;
        this.user = user ;
    }
}
