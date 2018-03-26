package io.eklesia.eklesia.ricerca;

import java.util.ArrayList;

/**
 * Created by Santa Caterina on 25/03/2018.
 */

public class ContenutoFiltro {
    private String placeId;
    private int distanza;
    private ArrayList<String> denominazioni;
    private ArrayList<String> congregazioni;
    public ContenutoFiltro(){
        denominazioni=new ArrayList<>();
        congregazioni=new ArrayList<>();
        placeId="";
        distanza=-1;
    }

    public void clear(){
        placeId="";
        distanza=-1;
        denominazioni=new ArrayList<>();
        congregazioni=new ArrayList<>();

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

    public ArrayList<String> getDenominazioni() {
        return denominazioni;
    }
    public void addDenominazione(String denominazione){
        this.denominazioni.add(denominazione);
    }

    public void removeDenominazione(String denominazione){
        this.denominazioni.remove(denominazione);
    }
    public void setDenominazioni(ArrayList<String> denominazioni) {
        this.denominazioni = denominazioni;
    }

    public ArrayList<String> getCongregazioni() {
        return congregazioni;
    }

    public void addCongregazione(String congregazione){
        this.congregazioni.add(congregazione);
    }
    public void removeCongregazione(String congregazione){
        this.denominazioni.remove(congregazione);
    }
    public void setCongregazioni(ArrayList<String> congregazioni) {
        this.congregazioni = congregazioni;
    }
}
