package com.example.xchange;

import java.time.LocalDate;

public class Request {
    private final xChanger requester;
    private final xChanger requestee;
    private Long request_id;
    private Item offered_item;
    private Item requested_item;
    private LocalDate date_initiated;

    Request(xChanger requester, xChanger requestee, Long request_id, Item offered_item, Item requested_item, LocalDate date_initiated) {
        this.requester = requester;
        this.requestee = requestee;
        this.request_id = request_id;
        this.offered_item = offered_item;
        this.requested_item = requested_item;
        this.date_initiated = date_initiated;
    }

    public long getRequestID() {
        return request_id;
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

//    public void setRequestID(Long request_id) {
//        this.request_id = request_id;
//    }
//    public void setDateInitiated(LocalDate date_initiated) {
//        this.date_initiated = date_initiated;
//    }
}