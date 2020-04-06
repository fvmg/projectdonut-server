package com.conestoga.projectdonut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationDto {

    private String name;

    private String email;

    private byte[] resume;

    private byte[] coverLetter;

}
