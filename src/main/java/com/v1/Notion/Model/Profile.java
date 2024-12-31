package com.v1.Notion.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
