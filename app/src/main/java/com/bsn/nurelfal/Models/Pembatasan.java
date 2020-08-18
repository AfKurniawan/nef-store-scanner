package com.bsn.nurelfal.Models;

public class Pembatasan {

    String pembatasan, total;

    public Pembatasan(){}


    public Pembatasan(String pembatasan, String total) {
        this.pembatasan = pembatasan;
        this.total = total;


    }

    public String getPembatasan() {
        return pembatasan;
    }

    public String getTotal(){
        return total;
    }



   // SET VOID


    public void setPembatasan(String pembatasan) {
        this.pembatasan = pembatasan;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
