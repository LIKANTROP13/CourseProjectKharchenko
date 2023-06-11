package com.example.courseproject.repository;

import com.example.courseproject.model.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository <UserDao, Long> {

    Optional<UserDao> findByRole(String role);
}
