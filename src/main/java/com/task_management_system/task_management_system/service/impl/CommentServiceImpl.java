//package com.task_management_system.task_management_system.service.impl;
//
//import com.task_management_system.task_management_system.model.Comment;
//import com.task_management_system.task_management_system.repository.CommentRepository;
//import com.task_management_system.task_management_system.service.CommentService;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CommentServiceImpl implements CommentService {
//
//    private final CommentRepository commentRepository;
//
//    @Autowired
//    public CommentServiceImpl(CommentRepository commentRepository) {
//        this.commentRepository = commentRepository;
//    }
//
//    @Override
//    public Comment addComment(Comment comment) {
//        Comment commentSave = commentRepository.save(comment);
//        return commentSave;
//    }
//
//    @Override
//    public void deleteComment(Long commentId) {
//        commentRepository.deleteById(commentId);
//    }
//}
