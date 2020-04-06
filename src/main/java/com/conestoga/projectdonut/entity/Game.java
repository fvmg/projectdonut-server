package com.conestoga.projectdonut.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Game {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private String briefDescription;

    @Lob
    private String fullDescription;

    private String version;

    private String downloadLink;

    private Date releaseDate;

    @ManyToMany
    private List<Genre> genres;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private List<Job> jobs;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<GameImage> images;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] coverImage;

    public void setBaseGenre(Genre baseGenre) {
        for (Genre genre : genres) {
            if (genre.getBaseGenre()) {
                genres.remove(genre);
                break;
            }
        }
        genres.add(baseGenre);
    }

    public void addJob(Job job) {
        jobs.add(job);
    }

    public void addImage(GameImage gameImage) {
        images.add(gameImage);
    }

}
