package com.example.courseproject.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("calories")
    private Long calories;

    @JsonProperty("composition")
    private List<String> composition;
}
