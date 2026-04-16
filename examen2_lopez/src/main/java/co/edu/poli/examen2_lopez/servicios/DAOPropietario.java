package co.edu.poli.examen2_lopez.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.examen2_lopez.modelo.Propietario;

public class DAOPropietario implements CRUD<Propietario>{

    @Override
    public String create(Propietario i) throws Exception {

        Connection con = ConexionBD.getInstancia().getConexion();
        con.setAutoCommit(false);

        try {

            String SQL_INSERT =
                    "INSERT INTO propietario (id, nombre) VALUES (?, ?)";

            PreparedStatement ps = con.prepareStatement(SQL_INSERT);
            ps.setString(1, i.getId());
            ps.setString(2, i.getNombre());
            ps.executeUpdate();

            con.commit();

            return "✔ Propietario [" + i.getId() + "] guardado correctamente.";

        } catch (Exception e) {

            con.rollback();
            e.printStackTrace();

            throw new RuntimeException("ERROR EN CREATE PROPIETARIO: " + e.getMessage(), e);

        } finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public <K> Propietario readone(K id) throws Exception {

        Connection con = ConexionBD.getInstancia().getConexion();

        String SQL_SELECT =
                "SELECT id, nombre FROM propietario WHERE id = ?";

        PreparedStatement ps = con.prepareStatement(SQL_SELECT);
        ps.setString(1, (String) id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Propietario(
                    rs.getString("id"),
                    rs.getString("nombre")
            );
        }

        return null;
    }

    @Override
    public List<Propietario> readall() throws Exception {

        Connection con = ConexionBD.getInstancia().getConexion();
        List<Propietario> lista = new ArrayList<>();

        String SQL_SELECT = "SELECT id, nombre FROM propietario";

        PreparedStatement ps = con.prepareStatement(SQL_SELECT);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Propietario i = new Propietario(
                    rs.getString("id"),
                    rs.getString("nombre")
            );
            lista.add(i);
        }

        return lista;
    }
}