package com.example.xchange;

import java.util.ArrayList;

public class Request {
    private final xChanger requester;
    private final xChanger requestee;
    private static Long previous_request_id = 1L; // Static field to track the last used ID
    private Long requested_id;
    private Item offered_item;
    private Item requested_item;
    private SimpleCalendar date_initiated;
    private boolean active;

    Request(xChanger requester, xChanger requestee, Item offered_item, Item requested_item, SimpleCalendar date_initiated) {
        if (requester == null) {
            throw new IllegalArgumentException("Requester cannot be null.");
        }
        if (requestee == null) {
            throw new IllegalArgumentException("Requestee cannot be null.");
        }
        if (date_initiated.getYear() > 2024 || (date_initiated.getYear() == 2024 &&
                (date_initiated.getMonth() > 12 || (date_initiated.getMonth() == 12 && date_initiated.getDay() > 3)))) {
            throw new IllegalArgumentException("Date cannot be in the future.");
        }
        this.requester = requester;
        this.requestee = requestee;
        this.requested_id = previous_request_id;
        previous_request_id++;
        this.offered_item = offered_item;
        this.requested_item = requested_item;
        this.date_initiated = date_initiated;
        this.active = true;
        add_to_list();
    }

    public long getRequestID() {
        return this.requested_id;
    }

    public xChanger getRequester() {
        return requester;
    }

    public xChanger getRequestee() {
        return requestee;
    }

    public Item getOfferedItem() {
        return offered_item;
    }

    public Item getRequestedItem() {
        return requested_item;
    }

    public SimpleCalendar getDateInitiated() {
        return date_initiated;
    }

    public void add_to_list() {
        this.getRequestee().getRequests().add(this);
    }

    public boolean isActive() {
        return this.active;
    }

    public void make_unactive() {
        this.active = false;
    }

    public static void resetId() {
        previous_request_id = 1L;
    }

    public class xChanger extends User {
        private static Long nextId = 10L;
        private Float sumOfratings;
        private int numOfratings;
        private float rating;
        ArrayList<String> reports;
        private ArrayList<User> xchangers;
        private ArrayList<Item> items;
        private ArrayList<Request> requests;
        private ArrayList<Counteroffer> counterOffers;
        private ArrayList<xChange> finalized;
        private String location;
        private int succeed_Deals;
        private int failed_Deals;

        xChanger(String username, String email, SimpleCalendar join_date, String password, String location) {
            super(nextId++, username, email, join_date, password, location);
            items = new ArrayList<>();
            requests = new ArrayList<>();
            counterOffers = new ArrayList<>();
            finalized = new ArrayList<>();
            this.location = location;
            this.numOfratings = 0;
            this.sumOfratings = 0.0f;
            this.rating = 0;
            xchangers=new ArrayList<>();
            this.reports=new ArrayList<>();
        }

        // Implement login method
        @Override
        public boolean login(String username, String password) {
            for (User user : xchangers) {
                if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean register(User user) {
            for (User temp_user : xchangers) {
                if (temp_user.getUsername().equals(user.getUsername()) || temp_user.getEmail().equals(user.getEmail())) {
                    return false;
                }
            }
            xchangers.add(user);
            return true;
        }

        public Float getRating() {
            return rating;
        }

        public void setRating(Float rating, xChanger xchanger) {
            xchanger.numOfratings++;
            xchanger.sumOfratings += rating;
            xchanger.rating = xchanger.sumOfratings / xchanger.numOfratings;
        }

        public void setRating(Float rating) {
            this.numOfratings++;
            this.sumOfratings += rating;
            this.rating = this.sumOfratings / this.numOfratings;
        }

        public ArrayList<Item> getItems() {
            return this.items;
        }

        public ArrayList<Request> getRequests() {
            return this.requests;
        }

        public ArrayList<Counteroffer> getCounterOffers() {
            return this.counterOffers;
        }

        public ArrayList<xChange> getFinalized() {
            return this.finalized;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void deleteItem(Item item_for_deletion) {
            this.getItems().removeIf(item -> item == item_for_deletion);
        }

        public Item getItem(Item item_to_get) {
            for (Item item : this.getItems()) {
                if (item == item_to_get) {
                    return item;
                }
            }
            return null;
        }

        public void UploadItem(String item_name, String item_description, String item_category, String item_condition, ArrayList<Image> item_images) {
            this.getItems().add(new Item(item_name, item_description, item_category, item_condition, item_images));
        }

        public void RequestItem(xChanger xchanger2, Item offered_item, Item requested_item) {
            Request request = new Request(this, xchanger2, offered_item, requested_item, new SimpleCalendar(2024, 12, 3));
            this.getRequests().add(request);
            xchanger2.getRequests().add(request);
        }

        public void plusOneSucceedDeal() {
            this.succeed_Deals++;
        }

        public void plusOneFailedDeal() {
            this.failed_Deals++;
        }

        public void report(xChanger xchanger, String message, xChange finalized) {
            if (finalized.getStatus() != null) {
                xchanger.setRating((float) (xchanger.getRating() - 0.2));
                message = "User " + this.getUsername() + " reported user " + xchanger.getUsername();
            }
            this.reports.add(message);
        }

        public String acceptRequest(Request request) {
            String email = "";
            xChange deal = new xChange(request, new SimpleCalendar(2024, 12, 3));
            email = deal.acceptOffer();
            return email;
        }


        public String acceptCounteroffer(Counteroffer counteroffer) {
            String email = "";
            xChange deal = new xChange(counteroffer.getRequest(), counteroffer, new SimpleCalendar(2024, 12, 3));
            email = deal.acceptOffer();
            return email;
        }

        public void rejectRequest(Request request) {
            xChange deal = new xChange(request, new SimpleCalendar(2024, 12, 3));
            deal.rejectOffer();
    <<<<<<< HEAD
        }

        public void rejectCounteroffer(Counteroffer counteroffer) {
            xChange deal = new xChange(counteroffer.getRequest(), counteroffer, new SimpleCalendar(2024, 12, 3));
            deal.rejectOffer();
        }

        public void counterOffer(Item item, String message, Request request) {
            if (item == null || message == null || request == null) {
                throw new IllegalArgumentException("Item, message, or request cannot be null.");
            }
            this.counterOffers.add(new Counteroffer(request, message, item));
        }
    }
}
