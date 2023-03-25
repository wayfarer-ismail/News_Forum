package models;

import java.util.Date;

public class Comment {

    /* Instance Variables */
    private Post post;
    private final User user;
    private final Date date;
    private final String comment;

    /* Constructors */
    public Comment(String comment, User user, Post post) {
        this.post = post;
        this.user = user;
        this.date = new Date();
        this.comment = comment;
    }

    public Comment(User user, long date, String comment) {
        this.post = null;
        this.user = user;
        this.date = new Date(date);
        this.comment = comment;
    }

    /* Getters and Setters */
    public void setPost(Post post) {
        this.post = post;
    }

    public String getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public Post getPost() {return post; }
}
