package co.edu.poli.examen2_lopez.servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import co.edu.poli.examen2_lopez.modelo.Propietario;

public class DAOPropietario implements CRUD<Propietario>{

    @Override
	public String create(Propietario t) {
		return null;
	}
	
	@Override
	public <K> Propietario readone(K id) throws Exception {
		return null;
	}

    @Override
	public List<Propietario> readall() throws Exception {

		Connection con = ConexionBD.getInstancia().getConexion();
		List<Propietario> lista = new ArrayList<>();

		String SQL_SELECT_TITULAR = "SELECT ti.id AS propietario_id, ti.nombre AS propietario_nombre " 
		+ "FROM propietario ti;";

		PreparedStatement ps = con.prepareStatement(SQL_SELECT_TITULAR);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Propietario t = new Propietario(rs.getString("propietario_id"), rs.getString("propietario_nombre"));
			lista.add(t);
		}
		return lista;
	}

}