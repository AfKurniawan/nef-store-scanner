package com.bsn.nurelfal.Models;

public class LimitTrx {

    String total_penarikan;

    public LimitTrx(){}


    public LimitTrx(String total_penarikan) {
        this.total_penarikan = total_penarikan;


    }

    public String getTotal_penarikan() {
        return total_penarikan;
    }



   // SET VOID


    public void setTotal_penarikan(String total_penarikan) {
        this.total_penarikan = total_penarikan;
    }


}
