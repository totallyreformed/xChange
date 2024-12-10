package com.example.xchange;

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
        this.getRequester().getRequests().add(this);
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
}