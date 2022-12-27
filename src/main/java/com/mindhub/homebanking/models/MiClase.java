package com.mindhub.homebanking.models;

public class MiClase {

    public int suma (int numberA, int numberB) {
        return numberA + numberB;
    }

    public String determinateCalification (int calification) {
        if(calification < 0) {
            throw new IllegalArgumentException("No puede ser negativa la calificación");
        } else if (calification < 60) {
            return "Desaprobado";
        } else if (calification < 70) {
            return "Regular";
        } else if (calification < 80) {
            return "Bueno";
        } else if (calification < 90) {
            return "Muy bueno";
        } else {
            return "Excelente";
        }
    }
}
