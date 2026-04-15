package co.edu.poli.examen2_lopez.integracion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import co.edu.poli.examen2_lopez.modelo.Propietario;
import co.edu.poli.examen2_lopez.servicios.DAOPropietario;

public class TestDAOPropietario {

    DAOPropietario dao = new DAOPropietario();

    @Test
    void readAll_noDebeSerNull() throws Exception {

        List<Propietario> lista = dao.readall();

        assertNotNull(lista);
    }

    @Test
    void readAll_listaValida() throws Exception {

        List<Propietario> lista = dao.readall();

        assertTrue(lista.size() >= 0);
    }

    @Test
    void readAll_objetosValidos() throws Exception {

        List<Propietario> lista = dao.readall();

        if (!lista.isEmpty()) {

            Propietario p = lista.get(0);

            assertNotNull(p.getId());
            assertNotNull(p.getNombre());
        }
    }
}