package com.v1.Notion.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String courseName;

    @Column(columnDefinition = "TEXT") // For potentially large course descriptions
    private String courseDescription;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @Column(columnDefinition = "TEXT") // For detailed content on learning outcomes
    private String whatWillYouLearn;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> courseContent;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<RatingAndReview> ratingAndReview = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal price; // Changed to BigDecimal for monetary values

    private String thumbnail;

    @ManyToMany
    @JoinTable(
        name = "course_tags",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tags> tags; // Updated to Many-to-Many

    @ManyToMany
    @JoinTable(
        name = "course_students",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<User> studentEnrolled;
}
