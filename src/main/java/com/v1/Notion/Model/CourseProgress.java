package com.v1.Notion.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class CourseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @ManyToMany
    @JoinTable(
        name = "course_progress_completed_videos",
        joinColumns = @JoinColumn(name = "course_progress_id"),
        inverseJoinColumns = @JoinColumn(name = "sub_section_id")
    )
    private List<SubSection> completedVideos;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<SubSection> getCompletedVideos() {
        return completedVideos;
    }

    public void setCompletedVideos(List<SubSection> completedVideos) {
        this.completedVideos = completedVideos;
    }
}
