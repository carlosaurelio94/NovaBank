package com.mindhub.homebanking.models;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class miClaseTest {

    @Test
    void suma() {
        MiClase miClase = new MiClase();
        assertEquals(4, miClase.suma(2,2));
    }

    @Test
    @Disabled
    void sumaFallida() {
        MiClase miClase = new MiClase();
        assertEquals(4, miClase.suma(3,2));
    }

    @Test
    void sumaDosCifras() {
        var miClase = new MiClase();
        assertTrue(miClase.suma(2,2) == 4);
    }

    @Test
    void calification39Desaprobado() {
        var miClase = new MiClase();
        assertEquals("Desaprobado", miClase.determinateCalification(39));
    }

    @Test
    void calification65Regular() {
        var miClase = new MiClase();
        assertEquals("Regular", miClase.determinateCalification(65));
    }

    @Test
    void calification74Bueno() {
        var miClase = new MiClase();
        assertEquals("Bueno", miClase.determinateCalification(74));
    }

    @Test
    void calification82MuyBueno() {
        var miClase = new MiClase();
        assertEquals("Muy bueno", miClase.determinateCalification(82));
    }

    @Test
    void calification99Excelente() {
        var miClase = new MiClase();
        assertEquals("Excelente", miClase.determinateCalification(99));
    }

}