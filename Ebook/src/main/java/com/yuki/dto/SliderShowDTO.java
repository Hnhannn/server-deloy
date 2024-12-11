package com.yuki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SliderShowDTO {
    private int sliderID;
    private String imageUrl;
    private int userID;
    private boolean status;
    private int current;
}