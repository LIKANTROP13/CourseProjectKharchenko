package com.example.courseproject.repository;

import com.example.courseproject.model.dao.DishCompositionDao;
import com.example.courseproject.model.dao.DishDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DishCompositionRepository extends JpaRepository <DishCompositionDao, Long> {

    Optional<DishCompositionDao> findByDish(DishDao dish);
}
