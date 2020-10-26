package ImageHoster.service;

import ImageHoster.model.Comment;
import ImageHoster.model.Image;
import ImageHoster.model.User;
import ImageHoster.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Date;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    //This method is called by the Image Controller class and is used to create a new comment type object
    //that can be persisted in the database. The method calls on the Comment Repository class to perform
    //this operation, and then returns the new comment object back to the Image Controller method.
    public Comment createComment(String commentText, Image image, User user) {

        Comment newComment = new Comment();

        newComment.setText(commentText);
        newComment.setImage(image);
        newComment.setUser(user);
        newComment.setCreatedDate(new Date());


        return commentRepository.createComment(newComment);
    }
}
