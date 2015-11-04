package com.backofficer.payment;

public class Payment{
    private int id;
    private String data;
    private boolean approved;

    public Payment(){
    }

    public Payment(int id, String data, boolean approved){
        this.setId(id);
        this.setData(data);
        this.setApproved(approved);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Payment))
            return false;
        if (obj == this)
            return true;

        Payment toCompare = (Payment) obj;
        return (    this.id == toCompare.id &&
                    this.approved == toCompare.approved &&
                    this.data.equals(toCompare.data));
    }

}
