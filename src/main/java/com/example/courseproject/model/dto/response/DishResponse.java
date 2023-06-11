package com.example.courseproject.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("calories")
    private Long calories;

    @JsonProperty("composition")
    private List<String> composition;
}
