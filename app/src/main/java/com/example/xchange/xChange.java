package com.example.xchange;

public class xChange {
    private String deal_status;
    private Request request;
    private Counteroffer counteroffer;
    private Long finalized_id;
    private SimpleCalendar date_finalized;
    private xChanger offerer;
    private xChanger offeree;
    private Item offered_item;
    private Item requested_item;

    public xChange(Request request, SimpleCalendar date_finalized) {
        if (request == null || date_finalized == null) {
            throw new IllegalArgumentException("Request and date_finalized cannot be null.");
        }
        this.request = request;
        this.finalized_id = request.getRequestID();
        this.date_finalized = date_finalized;
        this.deal_status = null;
        this.offerer = request.getRequester();
        this.offeree = request.getRequestee();
        this.offered_item = request.getOfferedItem();
        this.requested_item = request.getRequestedItem();
    }

    public xChange(Request request, Counteroffer counteroffer, SimpleCalendar date_finalized) {
        if (request == null || counteroffer == null || date_finalized == null) {
            throw new IllegalArgumentException("Request, counteroffer, and date_finalized cannot be null.");
        }
        this.counteroffer = counteroffer;
        this.request = request;
        this.finalized_id = request.getRequestID();
        this.date_finalized = date_finalized;
        this.deal_status = null;
        this.offerer = counteroffer.getCounterofferer();
        this.offeree = counteroffer.getCounterofferee();
        this.offered_item = counteroffer.getOfferedItem();
        this.requested_item = counteroffer.getRequestedItem();
    }

    // Getters
    public Request getRequest() {
        return request;
    }

    public Counteroffer getCounterOffer() {
        return counteroffer;
    }

    public Long getFinalizedID() {
        return finalized_id;
    }

    public SimpleCalendar getDateFinalized() {
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

    public void setDealStatus(String deal_status) {
        if (deal_status == null || deal_status.trim().isEmpty()) {
            throw new IllegalArgumentException("Deal status cannot be null or empty.");
        }
        this.deal_status = deal_status;
    }

    public String getStatus() {
        return this.deal_status;
    }

    // Methods to accept or reject the offer
    public String acceptOffer() {
        this.setDealStatus("Accepted");
        this.getOfferer().deleteItem(this.getRequestedItem());
        this.getOfferee().deleteItem(this.getOfferedItem());
        this.getOfferee().getFinalized().add(this);
        this.getOfferer().getFinalized().add(this);
        this.getRequest().make_unactive();

        // Check if counteroffer is not null
        if (this.getCounterOffer() != null && this.getCounterOffer().getRequest() == this.getRequest()) {
            this.getCounterOffer().make_unactive();
        }

        this.getOfferee().plusOneSucceedDeal();
        this.getOfferer().plusOneSucceedDeal();

        return this.getOfferee().getEmail();
    }

    public void rejectOffer() {
        this.setDealStatus("Rejected");
        this.offeree.getFinalized().add(this);
        this.offerer.getFinalized().add(this);
        this.getRequest().make_unactive();

        // Check if counteroffer is not null
        if (this.getCounterOffer() != null && this.getCounterOffer().getRequest() == this.getRequest()) {
            this.getCounterOffer().make_unactive();
        }

        this.getOfferee().plusOneFailedDeal();
        this.getOfferer().plusOneFailedDeal();
    }
}
