package com.example.paseasistencia.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Mallas implements Parcelable {
    private String Id;
    private String Sector;
    private String Mallas;

    public Mallas() {
    }

    public Mallas(String id, String sector, String mallas) {
        Id = id;
        Sector = sector;
        Mallas = mallas;
    }

    public Mallas(Cursor cursor) {
        this.Id = cursor.getString(0);
        this.Sector = cursor.getString(1);
        this.Mallas = cursor.getString(2);
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getSector() {
        return Sector;
    }

    public void setSector(String sector) {
        this.Sector = sector;
    }

    public String getMallas() {
        return Mallas;
    }

    public void setMallas(String mallas) {
        this.Mallas = mallas;
    }

    @Override
    public String toString() {
        return this.Mallas;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Id);
        dest.writeString(this.Sector);
        dest.writeString(this.Mallas);
    }

    protected Mallas(Parcel in) {
        this.Id = in.readString();
        this.Sector = in.readString();
        this.Mallas = in.readString();
    }

    public static final Parcelable.Creator<Mallas> CREATOR = new Parcelable.Creator<Mallas>() {
        @Override
        public Mallas createFromParcel(Parcel source) {
            return new Mallas(source);
        }

        @Override
        public Mallas[] newArray(int size) {
            return new Mallas[size];
        }
    };
}
