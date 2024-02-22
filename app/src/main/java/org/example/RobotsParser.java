package org.example;

import java.util.ArrayList;
import java.util.List;

public class RobotsParser {
    
    final List<String> allowList;
    final List<String> denyList;

    /*
     * NOT YET IMPLEMENTED 
     * Given a root URL, attempts to find and parse the robots.txt file into an allow list and
     * a denylist.
     */
    public RobotsParser(final String url) {
        
        allowList = new ArrayList<String>();
        denyList = new ArrayList<String>();
    }

    /*
     * NOT YET IMPLEMENTED
     * Checks that url is in allowList OR that url isn't in denylist.
     */
    public boolean canAccess(final String url) {
        return true;
    }
}
