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

    public static class Article {
        private String id;
        private String marca;
        private String model;
        private String velocitat;
        private String autonomia;
        private String bateria;
        private double pes;
        private double preu;

        public String getMarca() {
            return marca;
        }

        public void setMarca(String marca) {
            this.marca = marca;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getVelocitat() {
            return velocitat;
        }

        public void setVelocitat(String velocitat) {
            this.velocitat = velocitat;
        }

        public String getAutonomia() {
            return autonomia;
        }

        public void setAutonomia(String autonomia) {
            this.autonomia = autonomia;
        }

        public String getBateria() {
            return bateria;
        }

        public void setBateria(String bateria) {
            this.bateria = bateria;
        }

        public double getPes() {
            return pes;
        }

        public void setPes(double pes) {
            this.pes = pes;
        }

        public double getPreu() {
            return preu;
        }

        public void setPreu(double preu) {
            this.preu = preu;
        }

        public String getId() {

            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
