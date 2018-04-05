package io.eklesia.eklesia.ricerca;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Santa Caterina on 25/03/2018.
 */

public class ContenutoFiltro implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ContenutoFiltro createFromParcel(Parcel in) {
            return new ContenutoFiltro(in);
        }

        public ContenutoFiltro[] newArray(int size) {
            return new ContenutoFiltro[size];
        }
    };

    private String placeId;
    private int distanza;
    private ArrayList<Integer> denominazioni;
    private ArrayList<Integer> congregazioni;
    public ContenutoFiltro(){
        denominazioni=new ArrayList<Integer>();
        congregazioni=new ArrayList<Integer>();
        placeId="";
        distanza=-1;
    }

    public ContenutoFiltro(Parcel in){
        this.placeId=in.readString();
        this.distanza=in.readInt();
        this.denominazioni=in.readArrayList(Integer.class.getClassLoader());
        this.congregazioni=in.readArrayList(Integer.class.getClassLoader());
    }

    public void clear(){
        placeId="";
        distanza=-1;
        denominazioni=new ArrayList<Integer>();
        congregazioni=new ArrayList<Integer>();

    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public int getDistanza() {
        return distanza;
    }

    public void setDistanza(int distanza) {
        this.distanza = distanza;
    }

    public ArrayList<Integer> getDenominazioni() {
        return denominazioni;
    }
    public void addDenominazione(Integer denominazione){
        this.denominazioni.add(denominazione);
    }

    public void removeDenominazione(Integer denominazione){
        this.denominazioni.remove(denominazione);
    }
    public void setDenominazioni(ArrayList<Integer> denominazioni) {
        this.denominazioni = denominazioni;
    }

    public ArrayList<Integer> getCongregazioni() {
        return congregazioni;
    }

    public void addCongregazione(Integer congregazione){
        this.congregazioni.add(congregazione);
    }
    public void removeCongregazione(Integer congregazione){
        this.denominazioni.remove(congregazione);
    }
    public void setCongregazioni(ArrayList<Integer> congregazioni) {
        this.congregazioni = congregazioni;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.placeId);
        dest.writeInt(this.distanza);
        dest.writeList(this.denominazioni);
        dest.writeList(this.congregazioni);
    }
}
