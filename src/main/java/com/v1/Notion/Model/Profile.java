package com.v1.Notion.Model;

import jakarta.persistence.*;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10) // Gender is usually a short string
    private String gender;

    @Column(nullable = true)
    private String dateOfBirth;

    @Column(length = 500) // Length for the "about" section
    private String about;

    @Column(unique = true) // Optional: Ensures unique contact numbers
    private Long contactNumber;

    // Default constructor
    public Profile() {}

    // Parameterized constructor
    public Profile(Long id, String gender, String dateOfBirth, String about, Long contactNumber) {
        this.id = id;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.about = about;
        this.contactNumber = contactNumber;
    }

    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for gender
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter and Setter for dateOfBirth
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Getter and Setter for about
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    // Getter and Setter for contactNumber
    public Long getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }
}
