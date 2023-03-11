package com.mara.jpalibrary.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "Book")
public class Book {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Title should not be empty")
    @Size(min = 2, max = 50, message = "Book title size should be between 2 and 50 characters")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "Author should not be empty")
    @Size(min = 2, max = 50, message = "Author name size should be between 2 and 50 characters")
    private String author;

    @Column(name = "year_of_production")
    @Min(value = 0, message = "Minimum value of year = 0")
    @Max(value = 2023, message = "Maximum value of year = 2023")
    private int yearOfProduction;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "owned_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ownedAt;

    public Book() {}

    public Date getOwnedAt() {
        return ownedAt;
    }

    public void setOwnedAt(Date ownedAt) {
        this.ownedAt = ownedAt;
    }

    public boolean isOverdue() {
        if (ownedAt != null)
            return TimeUnit.DAYS.convert(Math.abs(new Date().getTime()
                - ownedAt.getTime()), TimeUnit.MILLISECONDS) > 10;
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
