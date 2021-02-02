package com.jhtt.jwtlogin.Entity;

import com.jhtt.jwtlogin.payload.SignUpRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class TestUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false,
            unique = true,
            length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    private String information;

    public TestUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static TestUser of(SignUpRequest request) {
        TestUser user = new TestUser(request.getEmail(), request.getPassword());
        if (request.getInformation() != null){
            user.setInformation(request.getInformation());
        }
        return user;
    }
}
