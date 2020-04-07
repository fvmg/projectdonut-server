package com.conestoga.projectdonut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameForYouDto {

    private int id;

    private String name;

    private String genre;

    private byte[] coverImage;

    private String description;

}
