package co.edu.poli.examen2_lopez.integracion;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.edu.poli.examen2_lopez.modelo.*;
import co.edu.poli.examen2_lopez.servicios.ConexionBD;
import co.edu.poli.examen2_lopez.servicios.DAOInmueble;
import co.edu.poli.examen2_lopez.servicios.DAOPropietario;

public class TestDAOInmueble {

    DAOInmueble dao = new DAOInmueble();
    DAOPropietario daoP = new DAOPropietario();

    @BeforeEach
    void clean() throws Exception{

        Connection con = ConexionBD.getInstancia().getConexion();

        con.setAutoCommit(false);

        Statement st = con.createStatement();
        
        st.executeUpdate("DELETE FROM inmueble_apto");
        st.executeUpdate("DELETE FROM inmueble_casa");
        st.executeUpdate("DELETE FROM inmueble");
        st.executeUpdate("DELETE FROM propietario");

        con.setAutoCommit(true);
    }

    @Test
    void create_casa_y_readone() throws Exception {

        Propietario p = new Propietario("T005", "Test Mari");
        daoP.create(p);

        Casa casa = new Casa(
                "9001",
                "15/04/2026",
                true,
                p,
                3
        );

        String result = dao.create(casa);

        assertTrue(result.contains("guardado"));

        Inmueble i = dao.readone("9001");

        assertNotNull(i);
        assertTrue(i instanceof Casa);

        Casa c = (Casa) i;
        assertEquals(3, c.getCantidadPisos());
    }

    @Test
    void create_apartamento_y_readone() throws Exception {

        Propietario p = new Propietario("T006", "Test Vale");
         daoP.create(p);

        Apartamento apto = new Apartamento(
                "9002",
                "15/04/2026",
                true,
                p,
                7
        );

        String result = dao.create(apto);

        assertTrue(result.contains("guardado"));

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