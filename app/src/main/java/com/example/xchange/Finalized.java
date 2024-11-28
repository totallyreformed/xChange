package com.example.xchange;

import java.time.LocalDate;
public class Finalized {
    private enum status {
        ACCEPTED,
        REJECTED;
    }

    private status deal_status;
    private Request request;
    private Counteroffer counteroffer;
    private Long finalized_id;
    private LocalDate date_finalized;
    private xChanger offerer;
    private xChanger offeree;
    private Item offered_item;
    private Item requested_item;

    public Finalized(Request reqeust, Long finalized_id, LocalDate date_finalized) {
        this.request = reqeust;
        this.finalized_id = finalized_id;
        this.date_finalized = date_finalized;
        this.deal_status = null;
        this.offerer = reqeust.getRequester();
        this.offeree = reqeust.getRequestee();
        this.offered_item = reqeust.getOfferedItem();
        this.requested_item = reqeust.getRequestedItem();
    }

    public Finalized(Counteroffer counteroffer, LocalDate date_finalized) {
        this.counteroffer = counteroffer;
        this.finalized_id = request.getRequestID();
        this.date_finalized = date_finalized;
        this.deal_status = null;
        this.offerer = counteroffer.getCounterofferer();
        this.offeree = counteroffer.getCounterofferee();
        this.offered_item = counteroffer.getOfferedItem();
        this.requested_item = counteroffer.getRequestedItem();
    }

    public Request getRequest() {
        return request;
    }

    public Counteroffer getCounterOffer() {
        return counteroffer;
    }

    public Long getFinalizedID() {
        return finalized_id;
    }

    public LocalDate getDateFinalized() {
        return date_finalized;
    }

    public xChanger getOfferer() {
        return offerer;
    }

    public xChanger getOfferee() {
        return offeree;
    }

    public Item getOfferedItem() {
        return offered_item;
    }

    public Item getRequestedItem() {
        return requested_item;
    }

    public void setDealStatus(status deal_status) {
        this.deal_status = deal_status;
    }
}