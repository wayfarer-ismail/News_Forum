package services;

import models.User;

import java.sql.*;

public class AuthService {

    /** This method performs two functions, it clears any data from the CURRENT_USER table
     * and then saves the current User into the CURRENT_USER table
     *
     * @param user the User to login
     */
    public static void login(User user) {
        AuthService.logout();
        UserService.saveUser(user, DatabaseInfo.Tables.CURRENT_USER.label);
    }

    /** This method calls the .getLoggedInUser() from UserService, it is simply a mask
     * to keep all the authentication aspects under one class
     *
     * @return The User in the CURRENT_USER table or 'null' if none existed
     */
    public static User loadFromCurrentUserTable() {
        return UserService.getLoggedInUser();
    }

    /** This method deletes all the data from the CURRENT_USER table and should be used to set
     * the main User object back to null then restart the program.
     *
     * @return 'null' in order to set the User variable of the main program back to default
     */
    public static User logout() {

        String SQL_clearCurrentUserTable = "DELETE FROM " + DatabaseInfo.Tables.CURRENT_USER.label + ";";

        try (
                Connection connection = DriverManager.getConnection(DatabaseInfo.DB_URL);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(SQL_clearCurrentUserTable);
        } catch (SQLException e) {
            System.out.println("Something went wrong clearing the current user information");
        }

        return null;
    }
}
