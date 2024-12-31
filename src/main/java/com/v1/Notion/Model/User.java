package com.v1.Notion.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean approved = true;

    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile additionalDetails;

    @ManyToMany
    @JoinTable(
        name = "user_courses",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    private String image;

    private String token;

    @Column(name = "reset_password_expires")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetPasswordExpires;

    @OneToMany
    @JoinColumn(name = "course_progress_id")
    private List<CourseProgress> courseProgress;
}
