package com.jhtt.jwtlogin.repository;


import com.jhtt.jwtlogin.Entity.TestUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestUserRepository extends JpaRepository<TestUser, Long> {

    Optional<TestUser> findTestUserByEmail(String email);
}
