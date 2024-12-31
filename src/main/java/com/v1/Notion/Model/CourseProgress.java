package com.v1.Notion.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
