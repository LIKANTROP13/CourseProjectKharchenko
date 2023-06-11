package com.example.courseproject.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DishStatusResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("status")
    private String status;
}
