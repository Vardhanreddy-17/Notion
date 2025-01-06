package com.v1.Notion.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
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

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public String getWhatWillYouLearn() {
        return whatWillYouLearn;
    }

    public void setWhatWillYouLearn(String whatWillYouLearn) {
        this.whatWillYouLearn = whatWillYouLearn;
    }

    public List<Section> getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(List<Section> courseContent) {
        this.courseContent = courseContent;
    }

    public List<RatingAndReview> getRatingAndReview() {
        return ratingAndReview;
    }

    public void setRatingAndReview(List<RatingAndReview> ratingAndReview) {
        this.ratingAndReview = ratingAndReview;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public List<User> getStudentEnrolled() {
        return studentEnrolled;
    }

    public void setStudentEnrolled(List<User> studentEnrolled) {
        this.studentEnrolled = studentEnrolled;
    }
}
