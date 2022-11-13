package com.example.myfirstmac.domain.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String userId;
    String name;
    String address;

    @Builder
    public User(String userId, String name, String address) {
        this.userId = userId;
        this.name = name;
        this.address = address;
    }

    public UserEditor.UserEditorBuilder toEditor(){
        return UserEditor.builder()
                .name(name)
                .address(address);
    }

    /**
     * @param userEditor Build된 userEditor가 넘어온다.
     */
    public void edit(UserEditor userEditor) {
        name = userEditor.getName();
        address = userEditor.getAddress();
    }
}
