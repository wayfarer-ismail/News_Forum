package viewmodels;

import services.DatabaseInfo;

public class MainThread {

    public static void main(String[] args) {
        CommunityNewsForum newsForum = new CommunityNewsForum();
        newsForum.start();
    }
}