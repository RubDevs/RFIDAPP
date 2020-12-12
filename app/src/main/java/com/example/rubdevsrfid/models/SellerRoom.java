package com.example.rubdevsrfid.models;

public class SellerRoom {
    private Seller seller;
    private String room;

    public SellerRoom(Seller seller, String room) {
        this.seller = seller;
        this.room = room;
    }

    public SellerRoom() {
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
