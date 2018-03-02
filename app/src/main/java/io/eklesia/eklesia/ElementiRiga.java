package io.eklesia.eklesia;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ivanc on 02/03/2018.
 */

public class ElementiRiga {
    private int icona;
    private String testo;

    public ElementiRiga(int icona, String testo) {
        this.icona = icona;
        this.testo = testo;
    }

    public int getIcona() {
        return icona;
    }

    public void setIcona(int icona) {
        this.icona = icona;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }
}
