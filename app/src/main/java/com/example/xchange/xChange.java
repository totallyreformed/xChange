package com.example.xchange;

import java.time.LocalDate;
public class xChange {
    private String deal_status;
    private Request request;
    private Counteroffer counteroffer;
    private Long finalized_id;
    private LocalDate date_finalized;
    private xChanger offerer;
    private xChanger offeree;
    private Item offered_item;
    private Item requested_item;

    public xChange(Request request, LocalDate date_finalized) {
        this.request = request;
        this.finalized_id = request.getRequestID();
        this.date_finalized = date_finalized;
        this.deal_status = null;
        this.offerer = request.getRequester();
        this.offeree = request.getRequestee();
        this.offered_item = request.getOfferedItem();
        this.requested_item = request.getRequestedItem();
    }

    public xChange(Request request,Counteroffer counteroffer, LocalDate date_finalized) {
        this.counteroffer = counteroffer;
        this.request=request;
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

    public void setDealStatus(String deal_status) {
        this.deal_status = deal_status;
    }

    public String getStatus(){return this.deal_status;}

    public String acceptOffer(){
        this.setDealStatus("Accepted");
        this.offerer.deleteItem(this.getRequestedItem());
        this.offeree.deleteItem(this.getOfferedItem());
        this.offeree.getFinalized().add(this);
        this.offerer.getFinalized().add(this);
        this.getRequest().make_unactive();
        if(this.getCounterOffer().getRequest()==this.getRequest()){
            this.getCounterOffer().make_unactive();
        }
        this.getOfferee().plusOneSucceedDeal();
        this.getOfferer().plusOneSucceedDeal();

        return this.getOfferee().getEmail();
    }
    public void rejectOffer(){
        this.setDealStatus("Rejected");
        this.offeree.getFinalized().add(this);
        this.offerer.getFinalized().add(this);
        this.getRequest().make_unactive();
        if(this.getCounterOffer().getRequest()==this.getRequest()){
            this.getCounterOffer().make_unactive();
        }
        this.getOfferee().plusOneFailedDeal();
        this.getOfferer().plusOneFailedDeal();
    }


}