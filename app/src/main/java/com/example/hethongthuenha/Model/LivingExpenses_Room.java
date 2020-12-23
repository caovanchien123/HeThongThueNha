package com.example.hethongthuenha.Model;

import java.io.Serializable;

public class LivingExpenses_Room implements Serializable{
    private double mWater;
    private double mEletric;
    private double mInternet;
    private double mTivi;
    private double mParkingSpace;

    public LivingExpenses_Room() {
    }

    public LivingExpenses_Room(double mWater, double mEletric, double mInternet, double mTivi, double mParkingSpace) {
        this.mWater = mWater;
        this.mEletric = mEletric;
        this.mInternet = mInternet;
        this.mTivi = mTivi;
        this.mParkingSpace = mParkingSpace;
    }

    public double getmWater() {
        return mWater;
    }

    public void setmWater(double mWater) {
        this.mWater = mWater;
    }

    public double getmEletric() {
        return mEletric;
    }

    public void setmEletric(double mEletric) {
        this.mEletric = mEletric;
    }

    public double getmInternet() {
        return mInternet;
    }

    public void setmInternet(double mInternet) {
        this.mInternet = mInternet;
    }

    public double getmTivi() {
        return mTivi;
    }

    public void setmTivi(double mTivi) {
        this.mTivi = mTivi;
    }

    public double getmParkingSpace() {
        return mParkingSpace;
    }

    public void setmParkingSpace(double mParkingSpace) {
        this.mParkingSpace = mParkingSpace;
    }

    @Override
    public String toString() {
        return "LivingExpenses{" +
                "mWater=" + mWater +
                ", mEletric=" + mEletric +
                ", mInternet=" + mInternet +
                ", mTivi=" + mTivi +
                ", mParkingSpace=" + mParkingSpace +
                '}';
    }
}
