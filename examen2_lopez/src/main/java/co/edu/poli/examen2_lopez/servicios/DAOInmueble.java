package co.edu.poli.examen2_lopez.servicios;

import co.edu.poli.examen2_lopez.modelo.Casa;
import co.edu.poli.examen2_lopez.modelo.Apartamento;
import co.edu.poli.examen2_lopez.modelo.Inmueble;
import co.edu.poli.examen2_lopez.modelo.Propietario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;


public class DAOInmueble implements CRUD<Inmueble> {

    @Override
	public String create(Inmueble t) throws Exception {

		Connection con = ConexionBD.getInstancia().getConexion();

		con.setAutoCommit(false);

		String SQL_INSERT_INMUEBLE= "INSERT INTO inmueble (numero, fecha_compra, estado, propietario_id) "
				+ "VALUES (?, ?, ?, ?)";

		PreparedStatement ps = con.prepareStatement(SQL_INSERT_INMUEBLE);
		ps.setString(1, t.getNumero());
		ps.setString(2, t.getFechaCompra());
		ps.setBoolean(3, t.isEstado());
		ps.setString(4, t.getPropietario().getId());
		ps.executeUpdate();

        String SQL_INSERT_APARTAMENTO = "INSERT INTO inmueble_apto (numero, numero_piso) VALUES (?, ?)";
		String SQL_INSERT_CASA= "INSERT INTO inmueble_casa (numero, cantidad_pisos) VALUES (?, ?)";

		String sql = (t instanceof Apartamento) ? SQL_INSERT_APARTAMENTO : SQL_INSERT_CASA;
		ps = con.prepareStatement(sql);
		ps.setString(1, t.getNumero());
        if (t instanceof Apartamento)
			ps.setDouble(2, ((Apartamento) t).getNumeroPiso());
		else
			ps.setDouble(2, ((Casa) t).getCantidadPisos());
		
		try {
			ps.executeUpdate();
			con.commit();
			return "✔ " + t.getClass().getSimpleName() + " [" + t.getNumero() + "] guardada correctamente.";
		} catch (Exception e) {
			con.rollback();
			return e.getMessage();
		} finally {
			con.setAutoCommit(true);
		}
	}

    @Override
	public <K> Inmueble readone(K num) throws Exception {

		Connection con = ConexionBD.getInstancia().getConexion();

		String SQL_SELECT_APARTAMENTO = "SELECT  t.numero, t.fecha_compra, t.estado, "
				+ "        ti.id AS propietario_id, ti.nombre AS propietario_nombre, " + "        d.numeroPiso "
				+ "FROM    inmueble_apto d " + "INNER JOIN inmueble  t  ON d.numero     = t.numero "
				+ "INNER JOIN propietario  ti ON t.propietario_id = ti.id " + "WHERE   d.numero = ?";

		PreparedStatement ps = con.prepareStatement(SQL_SELECT_APARTAMENTO);
		ps.setString(1, (String) num);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return new Apartamento(rs.getString("numero"), rs.getString("fecha_compra"), rs.getBoolean("estado"),
					new Propietario(rs.getString("propietario_id"), rs.getString("propietario_nombre")), rs.getInt("numeroPiso"));
		}
        return null;
	}

	@Override
	public List<Inmueble> readall() {
		return null;
	}
}