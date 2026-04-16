package co.edu.poli.examen2_lopez.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import co.edu.poli.examen2_lopez.modelo.Apartamento;
import co.edu.poli.examen2_lopez.modelo.Casa;
import co.edu.poli.examen2_lopez.modelo.Inmueble;
import co.edu.poli.examen2_lopez.modelo.Propietario;

public class DAOInmueble implements CRUD<Inmueble> {

    @Override
    public String create(Inmueble i) throws Exception {

        Connection con = ConexionBD.getInstancia().getConexion();
        con.setAutoCommit(false);

        try {

			// CLASE Inmueble
            String SQL_INSERT_INMUEBLE =
                    "INSERT INTO inmueble (numero, fecha_compra, estado, propietario_id) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(SQL_INSERT_INMUEBLE);
            ps.setString(1, i.getNumero());
            ps.setString(2, i.getFechaCompra());
            ps.setBoolean(3, i.isEstado());
            ps.setString(4, i.getPropietario().getId());
            ps.executeUpdate();

			// CLASE Apartamento
            String sql;

            if (i instanceof Apartamento) {
                sql = "INSERT INTO inmueble_apto (numero, numero_piso) VALUES (?, ?)";
            } else {
                sql = "INSERT INTO inmueble_casa (numero, cantidad_pisos) VALUES (?, ?)";
            }

            ps = con.prepareStatement(sql);
            ps.setString(1, i.getNumero());

            if (i instanceof Apartamento) {
                ps.setInt(2, ((Apartamento) i).getNumeroPiso());
            } else {
                ps.setInt(2, ((Casa) i).getCantidadPisos());
            }

            ps.executeUpdate();

            con.commit();

            return "✔ " + i.getClass().getSimpleName() +
                    " [" + i.getNumero() + "] guardado correctamente.";

        } catch (Exception e) {

            con.rollback();
            e.printStackTrace(); 

            throw new RuntimeException("ERROR EN CREATE: " + e.getMessage(), e);

        } finally {
            con.setAutoCommit(true);
        }
    }

    @Override
    public <K> Inmueble readone(K num) throws Exception {

        Connection con = ConexionBD.getInstancia().getConexion();

        // Consultar Apartamento
        String SQL_APTO =
                "SELECT i.numero, i.fecha_compra, i.estado, " +
                "p.id AS propietario_id, p.nombre AS propietario_nombre, " +
                "a.numero_piso " +
                "FROM inmueble_apto a " +
                "INNER JOIN inmueble i ON a.numero = i.numero " +
                "INNER JOIN propietario p ON i.propietario_id = p.id " +
                "WHERE a.numero = ?";

        PreparedStatement ps = con.prepareStatement(SQL_APTO);
        ps.setString(1, (String) num);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Apartamento(
                    rs.getString("numero"),
                    rs.getString("fecha_compra"),
                    rs.getBoolean("estado"),
                    new Propietario(
                            rs.getString("propietario_id"),
                            rs.getString("propietario_nombre")
                    ),
                    rs.getInt("numero_piso") // 👈 corregido nombre columna
            );
        }

        // Consultar Casa
        String SQL_CASA =
                "SELECT i.numero, i.fecha_compra, i.estado, " +
                "p.id AS propietario_id, p.nombre AS propietario_nombre, " +
                "c.cantidad_pisos " +
                "FROM inmueble_casa c " +
                "INNER JOIN inmueble i ON c.numero = i.numero " +
                "INNER JOIN propietario p ON i.propietario_id = p.id " +
                "WHERE c.numero = ?";

        ps = con.prepareStatement(SQL_CASA);
        ps.setString(1, (String) num);
        rs = ps.executeQuery();

        if (rs.next()) {
            return new Casa(
                    rs.getString("numero"),
                    rs.getString("fecha_compra"),
                    rs.getBoolean("estado"),
                    new Propietario(
                            rs.getString("propietario_id"),
                            rs.getString("propietario_nombre")
                    ),
                    rs.getInt("cantidad_pisos")
            );
        }

        return null;
    }

    @Override
    public List<Inmueble> readall() {
        return null;
    }
}