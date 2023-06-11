package com.example.courseproject.service;

import com.example.courseproject.model.dao.DishCompositionDao;
import com.example.courseproject.model.dao.DishDao;
import com.example.courseproject.model.dao.UserDao;
import com.example.courseproject.model.dto.request.DishRequest;
import com.example.courseproject.model.dto.response.DishResponse;
import com.example.courseproject.model.dto.response.DishCompositionResponse;
import com.example.courseproject.model.dto.response.DishStatusResponse;
import com.example.courseproject.model.dto.response.UserResponse;
import com.example.courseproject.repository.DishCompositionRepository;
import com.example.courseproject.repository.DishesRepository;
import com.example.courseproject.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.example.courseproject.constant.RoleConstants.CLIENT_ROLE;
import static com.example.courseproject.constant.RoleConstants.WAITER_ROLE;
import static com.example.courseproject.constant.RoleConstants.MANAGER_ROLE;
import static com.example.courseproject.constant.StatusConstants.SUCCESS_STATUS;
import static com.example.courseproject.constant.StatusConstants.FAILED_STATUS;

@Service
@RequiredArgsConstructor
public class DietService {

    private final UsersRepository usersRepository;
    private final DishCompositionRepository dishCompositionRepository;
    private final DishesRepository dishesRepository;

    public List<UserResponse> getUsers() {
        return usersRepository.findAll().stream().map(this::toUserDto).collect(Collectors.toList());
    }

    public DishStatusResponse createDish(Long roleId, DishRequest request) {
        DishStatusResponse dishStatusResponse = new DishStatusResponse();
        Optional<UserDao> userOptional = usersRepository.findById(roleId);
        if (userOptional.isPresent() && userOptional.get().getRole().equals(WAITER_ROLE)
                || userOptional.isPresent() && userOptional.get().getRole().equals(MANAGER_ROLE)) {
            DishDao dishDao = this.toDishEntity(request.getCalories(), request.getName());
            DishCompositionDao dishCompositionDao = this.toDishCompositionEntity(request.getComposition(), dishDao);
            dishesRepository.save(dishDao);
            dishCompositionRepository.save(dishCompositionDao);
            Optional<DishDao> dishOptional = dishesRepository.findByName(request.getName());
            if (dishOptional.isPresent()) {
                Optional<DishCompositionDao> dishCompositionOptional = dishCompositionRepository.findByDish(
                        dishOptional.get());
                if (dishCompositionOptional.isPresent()) {
                    dishStatusResponse.setId(dishOptional.get().getId());
                    dishStatusResponse.setStatus(SUCCESS_STATUS);
                    return dishStatusResponse;
                }
            }
        } else {
            throw new RuntimeException("Only the waiter and the manager have the opportunity to create the dish!");
        }
        dishStatusResponse.setId(null);
        dishStatusResponse.setStatus(FAILED_STATUS);
        return dishStatusResponse;
    }

    public DishStatusResponse deleteDish(Long roleId, Long dishId) {
        DishStatusResponse dishStatusResponse = new DishStatusResponse();
        Optional<UserDao> userOptional = usersRepository.findById(roleId);
        if (userOptional.isPresent() && userOptional.get().getRole().equals(WAITER_ROLE)
                || userOptional.isPresent() && userOptional.get().getRole().equals(MANAGER_ROLE)) {
            dishesRepository.deleteById(dishId);
            Optional<DishDao> dishOptional = dishesRepository.findById(dishId);
            if (dishOptional.isEmpty()) {
                dishStatusResponse.setId(null);
                dishStatusResponse.setStatus(SUCCESS_STATUS);
                return dishStatusResponse;
            }

        } else {
            throw new RuntimeException("Only the waiter and the manager have the opportunity to remove the dish!");
        }
        dishStatusResponse.setId(null);
        dishStatusResponse.setStatus(FAILED_STATUS);
        return dishStatusResponse;
    }

    public List<DishResponse> getDishes() {
        List<DishDao> dishes = dishesRepository.findAll();
        if (!dishes.isEmpty()) {
            return dishes.stream().map(this::toDishDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public DishResponse getDish(Long dishId) {
        Optional<DishDao> dishOptional = dishesRepository.findById(dishId);
        return dishOptional.map(this::toDishDto).orElse(null);
    }

    public List<DishCompositionResponse> getComposition(Long dishId) {
        Optional<DishDao> dishOptional = dishesRepository.findById(dishId);
        return dishOptional.map(this::toCompositionDishListDto).orElse(null);
    }

    private DishResponse toDishDto(DishDao dishDao) {
        DishResponse dish = new DishResponse();
        dish.setName(dishDao.getName());
        dish.setCalories(dishDao.getCalories());
        dish.setComposition(new ArrayList<>(Arrays.asList(
                dishDao.getDishComposition().getComposition()
                        .replaceAll("[\\[\\]\"]", StringUtils.EMPTY)
                        .split(",")))
                .stream().map(String::trim)
                .collect(Collectors.toList()));
        return dish;
    }

    private List<DishCompositionResponse> toCompositionDishListDto(DishDao dishDao) {
        List<DishCompositionResponse> dishCompositionResponse = new ArrayList<>();
        List<String> ingredients = new ArrayList<>(Arrays.asList(
                dishDao.getDishComposition().getComposition().split(",")));
        if (!ingredients.isEmpty()) {
            ingredients.forEach(ingredient -> {
                DishCompositionResponse dishComposition = new DishCompositionResponse();
                dishComposition.setIngredient(ingredient.trim().replaceAll("[\\[\\]\"]", StringUtils.EMPTY));
                dishCompositionResponse.add(dishComposition);
            });
            return dishCompositionResponse;
        }
        return Collections.emptyList();
    }

    private DishDao toDishEntity(Long calories, String name) {
        DishDao dishDao = new DishDao();
        dishDao.setCalories(calories);
        dishDao.setName(name);
        return dishDao;
    }

    private DishCompositionDao toDishCompositionEntity(List<String> composition, DishDao dishDao) {
        DishCompositionDao dishCompositionDao = new DishCompositionDao();
        dishCompositionDao.setComposition(composition.toString());
        dishCompositionDao.setDish(dishDao);
        return dishCompositionDao;
    }

    private UserResponse toUserDto(UserDao userDao) {
        UserResponse user = new UserResponse();
        user.setId(userDao.getId());
        user.setRole(userDao.getRole());
        return user;
    }

    @PostConstruct
    private void initUsers() {
        Optional<UserDao> clientOptional = usersRepository.findByRole(CLIENT_ROLE);
        Optional<UserDao> waiterOptional = usersRepository.findByRole(WAITER_ROLE);
        Optional<UserDao> managerOptional = usersRepository.findByRole(MANAGER_ROLE);

        if (clientOptional.isEmpty() && waiterOptional.isEmpty() && managerOptional.isEmpty()) {
            UserDao client = new UserDao();
            client.setRole(CLIENT_ROLE);
            usersRepository.save(client);

            UserDao waiter = new UserDao();
            waiter.setRole(WAITER_ROLE);
            usersRepository.save(waiter);

            UserDao manager = new UserDao();
            manager.setRole(MANAGER_ROLE);
            usersRepository.save(manager);
        }
    }
}
