package viewmodels;

import services.DatabaseInfo;

public class MainThread {

    public static void main(String[] args) {
        DatabaseInfo.loadDriver();
        DatabaseInfo.testDatabaseConnection();
        CommunityNewsForum newsForum = new CommunityNewsForum();
        newsForum.start();
    }
}