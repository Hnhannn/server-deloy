package com.yuki.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTypesDTO {
    private int bookTypeID;
    private String typeName;
    private String accessType;
}