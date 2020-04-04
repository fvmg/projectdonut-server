package com.conestoga.projectdonut.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JobApplication {

    @Id
    @GeneratedValue
    private int id;

    @Lob
    private byte[] resume;

    @Lob
    private byte[] coverLetter;

    @OneToOne
    private User user;

    @OneToOne
    private Job job;

}
