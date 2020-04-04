package com.conestoga.projectdonut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {

    private int gameId;

    private String gameName;

    private byte[] coverImage;

    private String name;

    private String description;

}
