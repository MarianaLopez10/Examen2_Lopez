package co.edu.poli.examen2_lopez.unitaria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import co.edu.poli.examen2_lopez.modelo.*;

public class TestInmueble {

    @Test
    void vender_cambiaEstadoAFalso() {

        Propietario p = new Propietario("1", "Test");

        Inmueble i = new Casa("100", "15/04/2026", true, p, 2);

        String msg = i.vender();

        assertFalse(i.isEstado());
        assertTrue(msg.contains("VENDIDO"));
    }

    @Test
    void alquilar_cambiaEstadoAVerdadero() {

        Propietario p = new Propietario("1", "Test");

        Inmueble i = new Apartamento("200", "15/04/2026", false, p, 3);

        String msg = i.alquilar();

        assertTrue(i.isEstado());
        assertTrue(msg.contains("ALQUILADO"));
    }

    @Test
    void gettersFuncionan() {

        Propietario p = new Propietario("1", "Test");

        Inmueble i = new Casa("300", "15/04/2026", true, p, 5);

        assertEquals("300", i.getNumero());
        assertEquals("15/04/2026", i.getFechaCompra());
        assertTrue(i.isEstado());
        assertEquals(p, i.getPropietario());
    }
}