package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Post {

    /* Class Variables */
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

    /* Instance Variables */
    private final UUID id;
    private final String title;
    private final String message;
    private final User user;
    private final Date date;
    private final List<Comment> comments;

    /* Constructors */
    public Post(String title, String message, User user) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.message = message;
        this.user = user;
        this.date = new Date();
        this.comments = new ArrayList<>();
    }

    public Post(String id, String title, String message, User user, long date, List<Comment> comments) {
        this.id = UUID.fromString(id);
        this.title = title;
        this.message = message;
        this.user = user;
        this.date = new Date(date);
        this.comments = comments;
    }

    /* Instance Methods */

    /** This method adds a Comment object to the Post's 'comments' List
     *
     * @param user The user that posted the comment
     * @param comment The comment's message
     */
    public void addComment (User user, String comment) {
        if (comment.length() > 168) {
            comment = comment.substring(0,165);
            comment = comment.concat("...");
        }
        Comment newComment = new Comment(comment, user, this);
        comments.add(newComment);
    }

    /** This method iterates over the comments associated with the post and prints them out
     */
    public void displayComments() {
        System.out.println("  There are " + comments.size() + " comments:");
        comments.forEach( c -> {
            System.out.print("   On " + simpleDateFormat.format(c.getDate()));
            System.out.println(", " + c.getUser().getUsername() + " commented: ");
            System.out.println("    " + c.getComment());
        });
    }

    /** This method prints the main post title and body. If 'true' is passed, it will also print all the comments
     * associated with the post.
     *
     * @param withComments determines if comments will be printed or not
     */
    public void printPost(boolean withComments) {
        System.out.println("Title: " + title);
        System.out.println("Date: " + simpleDateFormat.format(date));
        System.out.println(" Message: " + message);
        if (withComments) {
            displayComments();
        }
    }

    /* Getters and Setters */
    public String getIdString() {
        return id.toString();
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
