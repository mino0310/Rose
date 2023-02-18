package com.example.myfirstmac.domain.user;

import com.example.myfirstmac.domain.session.Session;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max=255)
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 255)
    private String password;

    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public Session addSession() {
        //uuid
        Session session = Session.builder().user(this).build();

        sessions.add(session);

        return session;
    }
}