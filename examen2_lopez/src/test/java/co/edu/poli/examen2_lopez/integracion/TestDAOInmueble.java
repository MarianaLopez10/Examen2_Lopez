package co.edu.poli.examen2_lopez.integracion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import co.edu.poli.examen2_lopez.modelo.*;
import co.edu.poli.examen2_lopez.servicios.DAOInmueble;

public class TestDAOInmueble {

    DAOInmueble dao = new DAOInmueble();

    @Test
    void create_casa_y_readone() throws Exception {

        Propietario p = new Propietario("T001", "Test");

        Casa casa = new Casa(
                "9001",
                "15/04/2026",
                true,
                p,
                3
        );

        String result = dao.create(casa);

        assertTrue(result.toLowerCase().contains("guard"));

        Inmueble i = dao.readone("9001");

        assertNotNull(i);
        assertTrue(i instanceof Casa);

        Casa c = (Casa) i;
        assertEquals(3, c.getCantidadPisos());
    }

    @Test
    void create_apartamento_y_readone() throws Exception {

        Propietario p = new Propietario("T002", "Test");

        Apartamento apto = new Apartamento(
                "9002",
                "15/04/2026",
                true,
                p,
                7
        );

        String result = dao.create(apto);

        assertTrue(result.toLowerCase().contains("guard"));

        Inmueble i = dao.readone("9002");

        assertNotNull(i);
        assertTrue(i instanceof Apartamento);

        Apartamento a = (Apartamento) i;
        assertEquals(7, a.getNumeroPiso());
    }

    @Test
    void readone_noExiste() throws Exception {

        Inmueble i = dao.readone("000000");

        assertNull(i);
    }
}