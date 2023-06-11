package com.example.courseproject.repository;

import com.example.courseproject.model.dao.DishDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DishesRepository extends JpaRepository <DishDao, Long> {
    Optional<DishDao> findByName(String name);
}
