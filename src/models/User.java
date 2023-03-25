package models;

import java.util.UUID;

/**
 * The User class is used to represent a user of the Community News Forum. Users are stored both in the USERS
 * and CURRENT_USER table of the database.
 */
public class User {

    /* Instance Variables */
    private final UUID id;
    private final String username;
    private final String password;

    /* Constructors */
    public User (String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
    }

    public User (UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /* Getters and Setters */
    public String getUsername() {
        return username;
    }

    public String getIdString() {
        return id.toString();
    }

    public String getPassword() {
        return password;
    }
}
