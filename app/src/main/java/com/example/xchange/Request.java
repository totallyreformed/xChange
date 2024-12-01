package com.example.xchange;

import java.time.LocalDate;

public class Request {
    private final xChanger requester;
    private final xChanger requestee;
    private Long previous_request_id=1L;
    private Long requested_id;
    private Item offered_item;
    private Item requested_item;
    private LocalDate date_initiated;
    private boolean active;


    Request(xChanger requester, xChanger requestee, Item offered_item, Item requested_item, LocalDate date_initiated) {
        this.requester = requester;
        this.requestee = requestee;
        this.requested_id = previous_request_id;
        previous_request_id++;
        this.offered_item = offered_item;
        this.requested_item = requested_item;
        this.date_initiated = date_initiated;
        this.active=true;
        MainActivity.statistics.put("NUMBER OF ALL DEALS", MainActivity.statistics.get("NUMBER OF ALL DEALS") + 1);
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
    public LocalDate getDateInitiated() {
        return date_initiated;
    }
    public void add_to_list(){
        this.getRequestee().getRequests().add(this);
    }
    public boolean isActive(){
        return this.active;
    }

    public void make_unactive(){
        this.active=false;
    }
//    public void setRequestID(Long request_id) {
//        this.request_id = request_id;
//    }
//    public void setDateInitiated(LocalDate date_initiated) {
//        this.date_initiated = date_initiated;
//    }
}