//package com.task_management_system.task_management_system.model;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Getter
//@Setter
//public class Task {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String title;
//    private String description;
//
//    @Enumerated(EnumType.STRING)
//    private TaskStatus taskStatus;
//
//    @Enumerated(EnumType.STRING)
//    private TaskPriority taskPriority;
//
////    @ManyToOne
////    @JoinColumn(name = "user_id")
////    private User author;
////
////    @ManyToOne
////    @JoinColumn(name = "assignee_id")
////    private User assignee;
//
////    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
////    private List<Comment> comments = new ArrayList<>();
//}