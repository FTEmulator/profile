package com.FTEmulator.profile.entity;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String country;

    @Column
    private Short experience;

    @Column(length = 100)
    private String photo;

    @Column(length = 1000)
    private String biography;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Short getExperience() { return experience; }
    public void setExperience(Short experience) { this.experience = experience; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
}