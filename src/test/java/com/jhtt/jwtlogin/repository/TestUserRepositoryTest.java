package com.jhtt.jwtlogin.repository;

import com.jhtt.jwtlogin.Entity.TestUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TestUserRepositoryTest {

    @Autowired
    TestUserRepository testUserRepository;

    @Test
    public void save_and_find_user(){
        // Given
        String email = "test@mail.com";
        String password = "pw";

        TestUser user = new TestUser(email, password);

        // When
        TestUser savedUser = testUserRepository.save(user);
        TestUser foundUser = testUserRepository.findTestUserByEmail(email)
                .orElse(null);

        // Then
        assertThat(savedUser).isNotNull();
        assertThat(foundUser).as("DB에 저장된 TestUser를 email로 찾아올 수 있어야 합니다.").isNotNull();
        assertThat(savedUser).as("Transaction 내에서 같은 key로 찾아온 Entity는 같은 참조값을 같고 있어야 합니다.").isSameAs(foundUser);

    }
}