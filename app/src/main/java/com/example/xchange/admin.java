package com.example.xchange;

import java.util.ArrayList;

public class admin extends User {

    private static long nextId = 1;
    private static ArrayList<User> admins=new ArrayList<>();


    // Constructor using SimpleCalendar
    public admin(String username, String email, SimpleCalendar join_date, String password, String location) {
        super(nextId++, username, email, join_date, password, location);
        this.register(this);
    }

    // Implement login method
    @Override
    public boolean login(String username, String password) {
        for (User user : admins) {
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean register(User user) {
        // Check if the user already exists
        for (User temp_user : admins) {
            if (temp_user.getUsername().equals(user.getUsername()) || temp_user.getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        admins.add(user);
        return true;
    }

    public static ArrayList<User> getadmins(){
        return admins;
    }
    //TODO κραταμε τις συναρτησεις για την λογικη οταν βαλουμε τα αρχεια αποθηκευσης

    //    public void manageCategories(String category, String action) {
//        if (action.equals("add")) {
//            MainActivity.categories.add(category);
//        } else {
//            for (String cat : MainActivity.categories) {
//                if (cat.equals(category)) {
//                    MainActivity.categories.remove(category);
//                    return;
//                } else {
//                    System.out.println("This category does not exist");
//                }
//            }
//        }
//    }
//
//    public void viewReports() {
//        int i = 0;
//        for (String report : MainActivity.reports) {
//            System.out.println("Report " + i + ": " + report);
//            i++;
//        }
//    }
//
//    public void viewStatistics() {
//        for (String key : MainActivity.statistics.keySet()) {
//            System.out.println(key + ": " + MainActivity.statistics.get(key));
//        }
//    }
//
//    public int getSpecificStatistic(String key) {
//        return MainActivity.statistics.get(key);
//    }
//
    public static void resetNextId() {
        nextId = 1L;
    }
}