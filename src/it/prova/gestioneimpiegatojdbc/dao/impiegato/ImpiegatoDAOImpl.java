package it.prova.gestioneimpiegatojdbc.dao.impiegato;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.AbstractMySQLDAO;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {

	// la connection stavolta fa parte del this, quindi deve essere 'iniettata'
	// dall'esterno
	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Impiegato> list() throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from impiegato")) {

			while (rs.next()) {
				impiegatoTemp = new Impiegato();
				impiegatoTemp.setNome(rs.getString("NOME"));
				impiegatoTemp.setCognome(rs.getString("COGNOME"));
				impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
				impiegatoTemp.setDataNascita(rs.getDate("DATANASCITA"));
				impiegatoTemp.setDataAssunzione(rs.getDate("DATAASSUNZIONE"));
				impiegatoTemp.setId(rs.getLong("ID"));

				result.add(impiegatoTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Impiegato get(Long long1) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (long1 == null || long1 < 1)
			throw new Exception("Valore di input non ammesso.");

		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato where id=?")) {

			ps.setLong(1, long1);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Impiegato();
					result.setNome(rs.getString("NOME"));
					result.setCognome(rs.getString("COGNOME"));
					result.setCodiceFiscale(rs.getString("CODICEFISCALE"));
					result.setDataNascita(rs.getDate("DATANASCITA"));
					result.setDataAssunzione(rs.getDate("DATAASSUNZIONE"));
					result.setId(rs.getLong("ID"));

				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Impiegato input) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE impiegato SET nome=?, cognome=?, codiceFiscale=?, dataNascita=?, dataAssunzione=? where id=?;")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(6, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Impiegato input) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO impiegato (nome, cognome, codiceFiscale, dataNascita,dataAssunzione) VALUES (?, ?, ?, ?, ?);")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Impiegato input) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM impiegato WHERE ID=?")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findByExample(Impiegato input) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;

		String query = "select * from impiegato where 1=1 ";
		if (input.getCognome() != null && !input.getCognome().isEmpty()) {
			query += " and cognome like '" + input.getCognome() + "%' ";
		}
		if (input.getNome() != null && !input.getNome().isEmpty()) {
			query += " and nome like '" + input.getNome() + "%' ";
		}

		if (input.getCodiceFiscale() != null && !input.getCodiceFiscale().isEmpty()) {
			query += " and codicefiscale like '" + input.getCodiceFiscale() + "%' ";
		}

		if (input.getDataNascita() != null) {
			query += " and datanascita='" + new java.sql.Date(input.getDataNascita().getTime()) + "' ";
		}

		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				impiegatoTemp = new Impiegato();
				impiegatoTemp.setNome(rs.getString("NOME"));
				impiegatoTemp.setCognome(rs.getString("COGNOME"));
				impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
				impiegatoTemp.setDataNascita(rs.getDate("datanascita"));
				impiegatoTemp.setDataAssunzione(rs.getDate("dataassunzione"));
				impiegatoTemp.setId(rs.getLong("ID"));
				result.add(impiegatoTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findAllByCompagnia(Compagnia compagnia) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		if (compagnia == null || compagnia.getId() < 1)
			throw new Exception("Valore di input non ammesso.");
		Compagnia compagniaTemp = null;
		Impiegato impiegatoTemp = null;
		List<Impiegato> result = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato i join compagnia c on c.id=i.compagnia_id where c.id= ?")) {
			ps.setLong(1, compagnia.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					compagniaTemp = new Compagnia();
					compagniaTemp.setRagioneSociale(rs.getString("c.ragionesociale"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("c.fatturatoannuo"));
					compagniaTemp.setDataFondazione(rs.getDate("c.datafondazione"));
					compagniaTemp.setId(rs.getLong("c.id"));
					impiegatoTemp = new Impiegato();
					impiegatoTemp.setNome(rs.getString("i.nome"));
					impiegatoTemp.setCognome(rs.getString("i.cognome"));
					impiegatoTemp.setCodiceFiscale(rs.getString("i.codicefiscale"));
					impiegatoTemp.setDataNascita(rs.getDate("i.datanascita"));
					impiegatoTemp.setDataAssunzione(rs.getDate("i.dataassunzione"));
					impiegatoTemp.setId(rs.getLong("i.id"));
					impiegatoTemp.setCompagnia(compagniaTemp);
					result.add(impiegatoTemp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int countByDataFondazioneCompagniaGreaterThan(java.util.Date data) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		if (data == null)
			throw new Exception("Valore di input non ammesso.");
		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("select count(*) from impiegato i join compagnia c on c.id=i.compagnia_id where c.datafondazione > ?")) {

			ps.setDate(1, new java.sql.Date(data.getTime()));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = rs.getInt("count(*)");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findAllErroriAssunzione() throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		Compagnia compagniaTemp = null;
		Impiegato impiegatoTemp = null;
		List<Impiegato> result = new ArrayList<>();
		try (Statement s = connection.createStatement()) {
			try (ResultSet rs = s.executeQuery("select * from impiegato i join compagnia c on c.id=i.compagnia_id where i.dataassunzione < c.datafondazione")) {
				while (rs.next()) {
					compagniaTemp = new Compagnia();
					compagniaTemp.setRagioneSociale(rs.getString("c.ragionesociale"));
					compagniaTemp.setFatturatoAnnuo(rs.getInt("c.fatturatoannuo"));
					compagniaTemp.setDataFondazione(rs.getDate("c.datafondazione"));
					compagniaTemp.setId(rs.getLong("c.id"));
					impiegatoTemp = new Impiegato();
					impiegatoTemp.setNome(rs.getString("i.nome"));
					impiegatoTemp.setCognome(rs.getString("i.cognome"));
					impiegatoTemp.setCodiceFiscale(rs.getString("i.codicefiscale"));
					impiegatoTemp.setDataNascita(rs.getDate("i.datanascita"));
					impiegatoTemp.setDataAssunzione(rs.getDate("i.dataassunzione"));
					impiegatoTemp.setId(rs.getLong("i.id"));
					impiegatoTemp.setCompagnia(compagniaTemp);
					result.add(impiegatoTemp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int deleteAll() throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM impiegato")) {
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

}
