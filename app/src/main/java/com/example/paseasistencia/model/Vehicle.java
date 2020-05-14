package com.example.paseasistencia.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable {
    //integer para edentificador en la lista
    private int mVehicleImage;
    //para mostrar el nombre delvehiculo
    private String mVehicleName;

    public Vehicle(int mVehicleImage, String mVehicleName) {
        this.mVehicleImage = mVehicleImage;
        this.mVehicleName = mVehicleName;
    }

    public int getmVehicleImage() {
        return mVehicleImage;
    }

    public String getmVehicleName() {
        return mVehicleName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mVehicleImage);
        dest.writeString(this.mVehicleName);
    }

    protected Vehicle(Parcel in) {
        this.mVehicleImage = in.readInt();
        this.mVehicleName = in.readString();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel source) {
            return new Vehicle(source);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}
