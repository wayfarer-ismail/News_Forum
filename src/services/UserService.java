package services;

import models.User;

import java.sql.*;
import java.util.UUID;

public class UserService {

    /** This method saves a new user to the specified table. It serves the function of both adding users to the USER table for general
     * authentication purposes and also to the CURRENT_USER table to enable quick logon for repeat visitors
     *
     * @param user the User that is to be added to the table
     * @param table the table in COMMUNITY_NEWS.db (USERS or CURRENT_USER) that the User will be saved to
     */
    public static void saveUser(User user, String table) {

        String SQL_insertUser = "INSERT INTO " + table + " VALUES (\"" +
                user.getIdString() + "\", \"" +
                user.getUsername() + "\", \"" +
                user.getPassword() +
                "\");";

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(SQL_insertUser);
        } catch (SQLException e) {
            System.out.println("Something went wrong when saving your account to the database.");
        }
    }

    /** This method searches the USERS table for a given username and returns a User object or 'null' back to the caller
     *
     * @param username used to search the USERS table
     * @return The User if they exist in the USERS table or 'null' if they don't
     */
    public static User getUserByUsername(String username) {

        String SQL_userByUsername = "SELECT * FROM " + DatabaseInfo.Tables.USERS.label + " WHERE USERNAME=\"" + username + "\";";
        User user = null;

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(SQL_userByUsername)
        ) {
            if (results.next()) {
                user = new User(UUID.fromString(results.getString(1)), results.getString(2), results.getString(3));
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong finding the user in the database.");
        }

        return user;
    }

    /** This method searches the USERS table for a given User's id and returns a User object or 'null' back to the caller
     *
     * @param id a UUID of a User, converted to a String
     * @return The User if they exist in the USERS table or 'null' if they don't
     */
    public static User getUserByID(String id) {

        String SQL_userById = "SELECT * FROM USERS WHERE ID=\"" + id + "\";";
        User user = null;

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(SQL_userById)
        ) {
            if (results.next()) {
                user = new User(UUID.fromString(results.getString(1)), results.getString(2), results.getString(3));
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong finding the user in the database.");
        }

        return user;
    }

    /** This method queries the CURRENT_USER database to find out if there was a user logged in to the database
     * the last time it was opened and loads that user on start up. If more than one entry exists in the table then
     * no one will be logged in.
     *
     * @return The User in the CURRENT_USER table or 'null' if none existed
     */
    public static User getLoggedInUser() {

        String SQL_loadFromCurrentUserTable = "SELECT * FROM " + DatabaseInfo.Tables.CURRENT_USER.label + ";";
        User user = null;

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(SQL_loadFromCurrentUserTable)
        ) {
            if (results.next()) {
                user = new User(UUID.fromString(results.getString(1)), results.getString(2), results.getString(3));
                int size = 1;
                while (results.next()) {
                    size++;
                }
                if (size > 1) {
                    user = null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong loading the logged in user.");
        }

        return user;
    }

    /** This method deletes a User from the USERS table
     *
     * @param user the User to delete from the database
     */
    public static void deleteUser(User user) {

        String deleteFromUsersTable = "DELETE FROM " + DatabaseInfo.Tables.USERS.label + " WHERE ID = \"" + user.getIdString() + "\";";

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(deleteFromUsersTable);
        } catch (SQLException e) {
            System.out.println("Something went wrong deleting the current user's profile.");
        }
    }
}