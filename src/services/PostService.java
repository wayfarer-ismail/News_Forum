package services;

import models.Comment;
import models.Post;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService {


    /**
     * This method saves a Post to the POSTS table
     * @param post The post to save to the POSTS table
     */
    public static void savePost(Post post) {
        String SQL_savePost;
        try (Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
            Statement statement = connection.createStatement() ) {

            SQL_savePost = "INSERT INTO " + DatabaseInfo.Tables.POSTS.label + " VALUES (" +
                    post.getIdString() + ", " +
                    post.getTitle() + ", " +
                    post.getMessage() + ", " +
                    post.getUser().getIdString() + ", " +
                    post.getDate().getTime() +
                    ");";
            statement.executeUpdate(SQL_savePost);

        } catch (SQLException e) {
            System.out.println("There was a problem saving the post.");
        }
    }



    /** This method returns all the posts from a user
     *
     * @param user The user from which to retrieve their posts
     * @return A List of all the posts from a given user or 'null' if there aren't any
     */
    public static List<Post> getPostsByUser(User user) {
        // TODO: Finish the getPostsByUser Method
        return null;
    }



    /** This method retrieves and returns the top five most recent news stories from the POSTS table.
     *
     * @return A List of the five most recent Post objects
     */
    public static List<Post> mostRecentNews() {
        // TODO: Finish the mostRecentNews Method
        return null;
    }



    /** This method attempts to retrieve a post by a given title by searching the POSTS table.
     *
     * @param title The title to search for
     * @return Either the Post from the POSTS table or 'null'.
     */
    public static Post getPostByTitle(String title) {

        String SQL_findPostByTitle = "SELECT * FROM " + DatabaseInfo.Tables.POSTS + " WHERE TITLE = \"" + title + "\";";

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(SQL_findPostByTitle)
        ) {
            if (results.next()) {
                User user = UserService.getUserByID(results.getString(4));
                Post post = new Post(results.getString(1), results.getString(2), results.getString(3), user, results.getLong(5), PostService.getCommentsForPost(results.getString(1)));
                post.getComments().forEach(c -> c.setPost(post));
                return post;
            }
        } catch (SQLException e) {
            System.out.println("There was a problem retrieving the post.");
        }

        return null;
    }



    /** This method retrieves the most recent Post made by a given user
     *
     * @param user The user used to find the most recent post
     * @return A Post object representing the user's latest post or 'null'
     */
    public static Post getMostRecentPostByUser(User user) {

        String SQL_getMostRecentPost = "SELECT * FROM " + DatabaseInfo.Tables.POSTS.label + " WHERE USER_ID = \"" + user.getIdString() + "\" ORDER BY POST_DATE DESC;";
        Post post = null;

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(SQL_getMostRecentPost)
        ) {
            if (results.next()) {
                post = new Post(results.getString(1), results.getString(2), results.getString(3), user, results.getLong(5), null);
            }
        } catch (SQLException e) {
            System.out.println("There was a problem retrieving the user's last post.");
        }

        return post;
    }



    /** This method returns the total number of posts that the user has made.
     *
     * @param user The user used to count their posts
     * @return An 'int' representing the number of posts made by a user
     */
    public static int getNumberOfPostsByUser(User user) {

        String SQL_countPosts = "SELECT COUNT(USER_ID) FROM " + DatabaseInfo.Tables.POSTS + " WHERE USER_ID =\"" + user.getIdString() + "\";";
        int totalPosts = 0;

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(SQL_countPosts)
        ) {
            totalPosts = results.getInt(1);
        } catch (SQLException e) {
            System.out.println("There was a problem retrieving the number of posts by the user.");
        }

        return totalPosts;
    }




    /** This method saves a comment to the COMMENTS table
     *
     * @param comment The comment to save to the COMMENTS table
     */
    public static void saveComment(Comment comment) {

        String SQL_saveComment = "INSERT INTO " + DatabaseInfo.Tables.COMMENTS + " VALUES (" +
                "\"" + comment.getPost().getIdString() + "\", " +
                "\"" + comment.getUser().getIdString() + "\", " +
                comment.getDate().getTime() + ", " +
                "\"" + comment.getComment() +
                "\");";

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(SQL_saveComment);
        } catch (SQLException e) {
            System.out.println("There was a problem saving your comment to the database.");
        }
    }

    /** This method gets all the comments associated with a given Post, searched by the Post's
     * UUID converted to a String.
     *
     * @param postID The Post's UUID, converted to a String
     * @return A List of all Comment object's associated with a given Post
     */
    private static List<Comment> getCommentsForPost(String postID) {

        String SQL_getComments = "SELECT * FROM COMMENTS WHERE POST_ID = \"" + postID + "\";";
        List<Comment> comments = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(SQL_getComments)
        ) {
            while (results.next()) {
                Comment comment = new Comment(UserService.getUserByID(results.getString(2)), results.getLong(3), results.getString(4));
                comments.add(comment);
            }
        } catch (SQLException e) {
            System.out.println("There was a problem loading comments for the given post.");
        }

        return comments;
    }

    /** This method returns the total number of comments that the user has made.
     *
     * @param user The user used to count their comments
     * @return An 'int' representing the number of comments made by a user
     */
    public static int getNumberOfCommentsByUser(User user) {

        String SQL_countComments = "SELECT COUNT(USER_ID) FROM " + DatabaseInfo.Tables.COMMENTS + " WHERE USER_ID =\"" + user.getIdString() + "\";";
        int totalComments = 0;

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(SQL_countComments)
        ) {
            totalComments = results.getInt(1);
        } catch (SQLException e) {
            System.out.println("There was a problem retrieving the number of comments by the user.");
        }

        return totalComments;
    }
}
