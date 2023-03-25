package viewmodels;

import models.*;
import services.*;

import java.util.List;
import java.util.Scanner;

public class CommunityNewsForum {

    /* Class Variables */
    private static final Scanner CONSOLE = new Scanner(System.in);

    /* Instance Variables */
    private final String[] VALID_LOGGED_IN_COMMANDS = {"help", "profile", "my posts", "recent news", "find post", "write post", "logout", "delete account", "exit"};
    private final String[] VALID_POST_COMMANDS = {"help", "write comment", "exit"};

    private User user; // The current user of the forum, once logged in.
    private String currentResponse; // The current response retrieved from the terminal
    private Post postBeingViewed; // The Post that is being viewed by the User


    /*
     * These methods run when the program is first initiated.
     */

    protected void start() {
        user = AuthService.loadFromCurrentUserTable();
        if (user != null) {
            previousUserIntro();
        } else {
            noPreviousUserIntro();
        }
    }

    private void previousUserIntro() {
        System.out.println("Welcome back to the Community News Forum " + user.getUsername() + ", how have you been?");
        System.out.println("Let's get you caught up on what's happened since you left!");
        loggedInLoop();
    }

    private void noPreviousUserIntro() {
        System.out.println("Welcome to the Community News Forum!!");
        System.out.print("Enter your username to continue: ");
        String username = CONSOLE.next();

        user = UserService.getUserByUsername(username);

        if (user == null) {
            System.out.println(username + ", it looks like you haven't been here before. Would you like to create an account? (y/n): ");
            yesOrNoCommand();
            if (currentResponse.equals("y")) {
                System.out.println("Please enter a password for your account: ");
                String password = CONSOLE.next();
                System.out.println("You entered '" + password + "', is this correct? (y / n): ");
                yesOrNoCommand();
                if (currentResponse.equals("n")) {
                    System.out.println("Please enter a new password: ");
                    password = CONSOLE.next();
                    System.out.println("Your account will be created with the password: '" + password +"', please remember this.");
                }
                user = new User(username, password);
            } else {
                System.out.println("An account is mandatory to continue, thank you for visiting!");
                System.exit(1);
            }
            UserService.saveUser(user, DatabaseInfo.Tables.USERS.label);
            System.out.println("Your account was created, let's get you logged in!");
        }
        checkPassword(user);
        loggedInLoop();
    }

    private void loggedInLoop() {
        while (true) {
            System.out.print("What would you like to do? (you can type 'help' to see a list of all available commands): ");
            loggedInCommandHandler(CONSOLE.nextLine());
        }
    }

    private void enterPostLoop() {
        while (true) {
            System.out.print("What would you like to do: ");
            postCommandHandler(CONSOLE.nextLine());
        }
    }

    private void checkPassword(User user) {
        for (int i = 1; i < 4; i++) {
            System.out.println(user.getUsername() + ", enter your password: ");
            String passwordToCheck = CONSOLE.next();
            if (user.getPassword().equals(passwordToCheck)) {
                AuthService.login(user);
                return;
            }
            System.out.println("That password didn't match what we have on file, let's try again.");
            System.out.println("You have " + (3 - i) + " more attempts before the program is closed. \n");
        }
        this.user = AuthService.logout();
        System.out.println("The program is exiting... Goodbye.");
        System.exit(1);
    }

    /*
     * These methods are used to handle user inputs
     */

    private void postCommandHandler(String command) {
        switch (command) {
            case "help":
                help(VALID_POST_COMMANDS);
                break;
            case "write comment":
                writeNewComment();
                break;
            case "exit":
                loggedInLoop();
                break;
            default:
                System.out.println(command);
                invalidPostCommand();
        }
    }

    private void loggedInCommandHandler(String command) {
        switch (command) {
            case "help":
                help(VALID_LOGGED_IN_COMMANDS);
                break;
            case "profile":
                displayProfile();
                break;
            case "my posts":
                getUserPosts();
                break;
            case "recent news":
                showRecentNews();
                break;
            case "write post":
                writeNewPost();
                break;
            case "find post":
                findStory();
                break;
            case "logout":
                user = AuthService.logout();
                start();
                break;
            case "delete account":
                UserService.deleteUser(user);
                user = AuthService.logout();
                start();
                break;
            case "exit":
                System.exit(1);
                break;
            default:
                System.out.println(command);
                invalidCommand();
        }
    }

    private void invalidCommand() {
        // Checks user input and ensures a proper command is entered
        System.out.print("You've entered an invalid command, try again or type 'help': ");
        loggedInCommandHandler(CONSOLE.nextLine());
    }

    private void invalidPostCommand() {
        // Checks user input and ensures a proper command is entered
        System.out.print("You've entered an invalid command, try again or type 'help': ");
        postCommandHandler(CONSOLE.nextLine());
    }

    private void yesOrNoCommand() {
        while (true) {
            currentResponse = CONSOLE.next().toLowerCase();
            if (currentResponse.equals("y") || currentResponse.equals("n")) {
                break;
            } else {
                System.out.println("It looks like you might have entered an incorrect response, please enter only 'y' for yes or 'n' for no: ");
            }
        }
    }

    /*
     * Methods that correspond to valid commands for a user that is authenticated against the database
     */

    private void help(String[] availableCommands) {
        System.out.println("Available Commands:");
        for (String command : availableCommands) {
            System.out.print("'" + command + "'" + ", ");
        }
        System.out.println("end of list.");
    }

    private void displayProfile() {
        // Displays User info, total posts, total comments, and most recent post
        System.out.println("\nYour username: " + user.getUsername());
        System.out.println("  You've written " + PostService.getNumberOfPostsByUser(user) + " news posts to this community and commented " + PostService.getNumberOfCommentsByUser(user) + " times.");
        System.out.println("Your most recent news post was:\n");
        Post post = PostService.getMostRecentPostByUser(user);
        if (post != null) {
            post.printPost(false);
            System.out.println();
        } else {
            System.out.println("  Oh No! It looks like you haven't made any posts yet!\n");
        }
    }

    private void getUserPosts() {
        // Displays all the users posts
        System.out.println("Here are all your stories, the most recent ones are listed first:\n");
        List<Post> usersPosts = PostService.getPostsByUser(user);
        usersPosts.forEach(p -> {
            p.printPost(false);
            System.out.println();
        });
    }

    private void showRecentNews() {
        // Shows the top five most recent news stories
        // Allows a user to select one of the most recent news stories to leave a comment on
        System.out.println("\n Here's the top five most recent news stories\n");
        List<Post> recentStories = PostService.mostRecentNews();
        int counter = 1;
        for (Post story : recentStories) {
            System.out.println("-=== Story Number " + counter++ + " ==-");
            story.printPost(false);
            System.out.println();
        }
        System.out.print("If you'd like to view a story, enter the Story Number, or type `menu` to go back: ");
        String command = CONSOLE.next();
        switch (command) {
            case "1":
                viewStory(recentStories.get(0), 1);
                break;
            case "2":
                viewStory(recentStories.get(1), 2);
                break;
            case "3":
                viewStory(recentStories.get(2), 3);
                break;
            case "4":
                viewStory(recentStories.get(3), 4);
                break;
            case "5":
                viewStory(recentStories.get(4), 5);
                break;
            default:
        }
    }

    private void viewStory(Post post, int storyNumber) {
        // Allows a user to search for a story by title
        postBeingViewed = post;
        System.out.println("+--------------------+");
        System.out.println("|   Story Number " + storyNumber +"   |");
        System.out.println("+--------------------+\n");
        post.printPost(true);
        enterPostLoop();
    }

    private void findStory() {
        // Allows a user to search for a story by title
        System.out.print("What title are you looking for? It is case sensitive: ");
        Post post = PostService.getPostByTitle(CONSOLE.nextLine());
        if (post != null) {
            viewStory(post, 1);
        } else {
            System.out.println("We didn't find your story, check the spelling and try again.");
        }
    }

    private void writeNewPost() {
        System.out.print("Post Title: ");
        String title = CONSOLE.nextLine();
        while (title.isEmpty()) {
            title = CONSOLE.nextLine();
        }
        System.out.print("Post Body: ");
        String body = CONSOLE.nextLine();
        while (body.isEmpty()) {
            CONSOLE.nextLine();
        }

        Post newPost = new Post(title, body, user);
        System.out.println();
        newPost.printPost(false);
        System.out.println();
        System.out.print("This is how your post looks, would you like to 'save', 'delete', or 'exit'? ");
        String answer = CONSOLE.next();

        if (answer.equals("save")) {
            System.out.println("Saving your post...\n");
            PostService.savePost(newPost);
        } else if (answer.equals("delete")) {
            System.out.println("Let's try again..\n");
            writeNewPost();
        } else {
            System.out.println("We didn't understand, so we're taking you back to the main menu.");
        }
    }

    private void writeNewComment() {
        // User comments on the selected post
        System.out.print("Your Comment: ");
        Comment newComment = new Comment(CONSOLE.nextLine(), user, postBeingViewed);
        PostService.saveComment(newComment);
        postBeingViewed = PostService.getPostByTitle(postBeingViewed.getTitle());
        viewStory(postBeingViewed, 1);
    }

}
