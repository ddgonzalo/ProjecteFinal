package com.example.dana.projectefinal;

import java.util.LinkedList;
import java.util.List;

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

        public String getId() {return id; }
        public void setId(String id) {
            this.id = id;
        }
    }



    public static class Lloguer {
        private String id;
        private String dataInici;
        private String dataFi;
        private String llocEntrega;
        private String llocRecollida;
        private String client = "null";
        private double preu;
        private int totalArticles;
        private List<String> llistaBicicletes = new LinkedList<>();
        private List<String> llistaScooters = new LinkedList<>();

        public String getId() {return id;}
        public void setId(String id) {this.id = id;}

        public String getDataInici() {return dataInici;}
        public void setDataInici(String dataInici) {this.dataInici = dataInici;}

        public String getDataFi() {return dataFi;}
        public void setDataFi(String dataFi) {this.dataFi = dataFi;}

        public String getLlocEntrega() {return llocEntrega;}
        public void setLlocEntrega(String llocEntrega) {this.llocEntrega = llocEntrega;}

        public String getLlocRecollida() {return llocRecollida;}
        public void setLlocRecollida(String llocRecollida) {this.llocRecollida = llocRecollida;}

        public String getClient() {return client;}
        public void setClient(String client) {this.client = client;}

        public double getPreu() {return preu;}
        public void setPreu(double preu) {this.preu = preu;}

        public int getTotalArticles() {return totalArticles;}
        public void setTotalArticles(int totalArticles) {this.totalArticles = totalArticles;}

        public List<String> getLlistaBicicletes() {return llistaBicicletes;}
        public void setLlistaBicicletes(List<String> llistaBicicletes) {this.llistaBicicletes = llistaBicicletes;}

        public List<String> getLlistaScooters() {return llistaScooters;}
        public void setLlistaScooters(List<String> llistaScooters) {this.llistaScooters = llistaScooters;}
    }


    public static class Client {
        private String dni;
        private String nom;
        private String dataNaixement;
        private String telefon;
        private String movil;
        private String correu;
        private String adreça;
        private String poblacio;
        private String codiPostal;

        public String getDni() {
            return dni;
        }

        public void setDni(String dni) {
            this.dni = dni;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getDataNaixement() {
            return dataNaixement;
        }

        public void setDataNaixement(String dataNaixement) {
            this.dataNaixement = dataNaixement;
        }

        public String getTelefon() {
            return telefon;
        }

        public void setTelefon(String telefon) {
            this.telefon = telefon;
        }

        public String getMovil() {
            return movil;
        }

        public void setMovil(String movil) {
            this.movil = movil;
        }

        public String getCorreu() {
            return correu;
        }

        public void setCorreu(String correu) {
            this.correu = correu;
        }

        public String getAdreça() {
            return adreça;
        }

        public void setAdreça(String adreça) {
            this.adreça = adreça;
        }

        public String getPoblacio() {
            return poblacio;
        }

        public void setPoblacio(String poblacio) {
            this.poblacio = poblacio;
        }

        public String getCodiPostal() {
            return codiPostal;
        }

        public void setCodiPostal(String codiPostal) {
            this.codiPostal = codiPostal;
        }
    }
}
