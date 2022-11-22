package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	
	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, Citta c) {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione "
		                   +" WHERE localita=? AND Month(data)=? ORDER BY data ASC ";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setString(1, c.getNome());
			st.setInt(2, mese);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	
	  public Double getUmiditaMedia(int mese, Citta citta) {
		  final String sql = "SELECT AVG(umidita) AS U FROM situazione "
                  +" WHERE localita=? AND Month(data)=?  ";
		  try {
			  Connection conn = ConnectDB.getConnection();
				PreparedStatement st = conn.prepareStatement(sql);

				st.setString(1, citta.getNome());
				st.setInt(2, mese);
				
				ResultSet rs = st.executeQuery();
				rs.next();
				Double u = rs.getDouble("U");

				conn.close();
				return u;

			} catch (SQLException e) {

				e.printStackTrace();
				throw new RuntimeException(e);
			}
	
	  }
	  
	  
	  

	public List<Citta> getAllCitta() {
		final String sql = "SELECT DISTINCT Localita FROM situazione ORDER BY localita";

		List<Citta> citta = new ArrayList<Citta>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Citta c = new Citta(rs.getString("Localita"));
				citta.add(c);
			}

			conn.close();
			return citta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


}
