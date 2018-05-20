package com.example.dana.projectefinal;

public class Objectes {

    public static class Recordatori {
        private String data;
        private String concepte;
        private String text;
        private String ubicacio;
        private int estat;
        private int tipusRecordatori;
        private String color;

        public String getData() {return data;}
        public String getConcepte() {return concepte;}
        public String getText() {return text;}
        public String getUbicacio() {return ubicacio;}
        public int getEstat() {return estat;}
        public int getTipusRecordatori() {return tipusRecordatori;}
        public String getColor() {return color;}

        public void setData(String data) {this.data = data;}
        public void setConcepte(String concepte) {this.concepte = concepte;}
        public void setText(String text) {this.text = text;}
        public void setUbicacio(String ubicacio) {this.ubicacio = ubicacio;}
        public void setEstat(int estat) {this.estat = estat;}
        public void setTipusRecordatori(int tipusRecordatori) {this.tipusRecordatori = tipusRecordatori;}
        public void setColor(String color) {this.color = color;}
    }
}
