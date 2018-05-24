package com.example.dana.projectefinal;

public class Objectes {

    public static class Recordatori {
        private String data;
        private String titol;
        private String text;
        private String ubicacio;
        private int estat;
        private String tipusRecordatori;
        //private String color;

        public String getData() {return data;}
        public String getTitol() {return titol;}
        public String getText() {return text;}
        public String getUbicacio() {return ubicacio;}
        public int getEstat() {return estat;}
        public String getTipusRecordatori() {return tipusRecordatori;}
        //public String getColor() {return color;}

        public void setData(String data) {this.data = data;}
        public void setTitol(String titol) {this.titol = titol;}
        public void setText(String text) {this.text = text;}
        public void setUbicacio(String ubicacio) {this.ubicacio = ubicacio;}
        public void setEstat(int estat) {this.estat = estat;}
        public void setTipusRecordatori(String tipusRecordatori) {this.tipusRecordatori = tipusRecordatori;}
        //public void setColor(String color) {this.color = color;}
    }
}
