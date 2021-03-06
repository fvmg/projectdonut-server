package com.conestoga.projectdonut.entity;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Job {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @Lob
    private String description;

    @Lob
    private String requirements;

    @Lob
    private String benefits;
}
