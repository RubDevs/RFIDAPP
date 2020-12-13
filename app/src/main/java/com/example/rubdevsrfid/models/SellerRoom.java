package com.example.rubdevsrfid.models;

import java.util.Date;

public class SellerRoom {
    private String seller;
    private String room;
    private Date fecha;

    public SellerRoom(String seller, String room, Date fecha) {
        this.seller = seller;
        this.room = room;
        this.fecha = fecha;
    }

    public SellerRoom() {
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getFecha() { return fecha; }

    public void setFecha(Date fecha) { this.fecha = fecha; }
}
