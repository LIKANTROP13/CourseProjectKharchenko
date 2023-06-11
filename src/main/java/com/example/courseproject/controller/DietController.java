package com.example.courseproject.controller;

import com.example.courseproject.model.dto.request.DishRequest;
import com.example.courseproject.model.dto.response.DishResponse;
import com.example.courseproject.model.dto.response.DishCompositionResponse;
import com.example.courseproject.model.dto.response.DishStatusResponse;
import com.example.courseproject.model.dto.response.UserResponse;
import com.example.courseproject.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/diet-course")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getPersonal() {
        return ResponseEntity.ok(dietService.getUsers());
    }

    @PostMapping(value = "/dish/create/{roleId}", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<DishStatusResponse> createDish(@PathVariable Long roleId, @RequestBody DishRequest request) {
        return ResponseEntity.ok(dietService.createDish(roleId, request));
    }

    @GetMapping("/dish/get/{dishId}")
    public ResponseEntity<DishResponse> getDish(@PathVariable Long dishId) {
        return ResponseEntity.ok(dietService.getDish(dishId));
    }

    @GetMapping("/dish/get/composition/{dishId}")
    public ResponseEntity<List<DishCompositionResponse>> getComposition(@PathVariable Long dishId) {
        return ResponseEntity.ok(dietService.getComposition(dishId));
    }

    @GetMapping("/dishes/get")
    public ResponseEntity<List<DishResponse>> getDishes() {
        return ResponseEntity.ok(dietService.getDishes());
    }

    @DeleteMapping("/dish/delete/{roleId}/{dishId}")
    public ResponseEntity<DishStatusResponse> deleteDish(@PathVariable Long roleId, @PathVariable Long dishId) {
        return ResponseEntity.ok(dietService.deleteDish(roleId, dishId));
    }
}
