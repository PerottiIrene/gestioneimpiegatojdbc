package it.prova.gestioneimpiegatojdbc.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.connection.MyConnection;
import it.prova.gestioneimpiegatojdbc.dao.Constants;
import it.prova.gestioneimpiegatojdbc.dao.compagnia.CompagniaDAO;
import it.prova.gestioneimpiegatojdbc.dao.compagnia.CompagniaDAOImpl;
import it.prova.gestioneimpiegatojdbc.dao.impiegato.ImpiegatoDAO;
import it.prova.gestioneimpiegatojdbc.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class TestImpiegato {

	public static void main(String[] args) {

		ImpiegatoDAO impiegatoDAOInstance = null;
		CompagniaDAO compagniaDAOInstance = null;

		// ##############################################################################################################
		// Grande novità: la Connection viene allestista dal chiamante!!! Non è più a
		// carico dei singoli metodi DAO!!!
		// ##############################################################################################################
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			// ecco chi 'inietta' la connection: il chiamante
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);
			compagniaDAOInstance = new CompagniaDAOImpl(connection);

			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");

			testInsertImpiegato(impiegatoDAOInstance);
			System.out.println("In tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + " elementi.");

			testDeleteImpiegato(impiegatoDAOInstance);
			System.out.println("in tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + "elementi");

			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testInsertCompagnia(compagniaDAOInstance);
			System.out.println("In tabella compagnia ci sono " + compagniaDAOInstance.list().size() + " elementi.");

			testDeleteCompagnia(compagniaDAOInstance);
			System.out.println("in tabella compagnia ci sono " + compagniaDAOInstance.list().size() + "elementi");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void testInsertImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testInsertUser inizio.............");
		Compagnia compagnia1 = new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29));
		int quantiElementiInseriti = -1;
		quantiElementiInseriti = impiegatoDAOInstance.insert(new Impiegato("irene", "perotti", "64fesgah",
				new java.sql.Date(2020, 05, 04), new java.sql.Date(1999, 04, 07), compagnia1));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertUser : FAILED");

		System.out.println(".......testInsertUser fine: PASSED.............");
	}

	private static void testDeleteImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		// me ne creo uno al volo
		Compagnia compagnia1 = new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29));
		int quantiElementiInseriti = impiegatoDAOInstance.insert(new Impiegato("Giuseppe", "Verdi", "4599",
				new java.sql.Date(2000, 05, 14), new java.sql.Date(1999, 03, 14), compagnia1));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testDeleteUser : FAILED, user da rimuovere non inserito");

		List<Impiegato> elencoVociPresenti = impiegatoDAOInstance.list();
		int numeroElementiPresentiPrimaDellaRimozione = elencoVociPresenti.size();
		if (numeroElementiPresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteUser : FAILED, non ci sono voci sul DB");

		Impiegato ultimoDellaLista = elencoVociPresenti.get(numeroElementiPresentiPrimaDellaRimozione - 1);
		impiegatoDAOInstance.delete(ultimoDellaLista);

		// ricarico per vedere se sono scalati di una unità
		int numeroElementiPresentiDopoDellaRimozione = impiegatoDAOInstance.list().size();
		if (numeroElementiPresentiDopoDellaRimozione != numeroElementiPresentiPrimaDellaRimozione - 1)
			throw new RuntimeException("testDeleteUser : FAILED, la rimozione non è avvenuta");

		System.out.println(".......testDeleteUser fine: PASSED.............");
	}

	// test Compagnia=======================

	private static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testInsertUser inizio.............");
		int quantiElementiInseriti = -1;
		quantiElementiInseriti = compagniaDAOInstance
				.insert(new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29)));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertUser : FAILED");

		System.out.println(".......testInsertUser fine: PASSED.............");
	}

	private static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		// me ne creo uno al volo
		
		int quantiElementiInseriti = compagniaDAOInstance
				.insert(new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29)));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testDeleteUser : FAILED, user da rimuovere non inserito");

		List<Compagnia> elencoVociPresenti = compagniaDAOInstance.list();
		int numeroElementiPresentiPrimaDellaRimozione = elencoVociPresenti.size();
		if (numeroElementiPresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteUser : FAILED, non ci sono voci sul DB");

		Compagnia ultimoDellaLista = elencoVociPresenti.get(numeroElementiPresentiPrimaDellaRimozione - 1);
		compagniaDAOInstance.delete(ultimoDellaLista);

		// ricarico per vedere se sono scalati di una unità
		int numeroElementiPresentiDopoDellaRimozione = compagniaDAOInstance.list().size();
		if (numeroElementiPresentiDopoDellaRimozione != numeroElementiPresentiPrimaDellaRimozione - 1)
			throw new RuntimeException("testDeleteUser : FAILED, la rimozione non è avvenuta");

		System.out.println(".......testDeleteUser fine: PASSED.............");
	}
	
	
	private static void testFindByExample(ImpiegatoDAO impiegatoDAOInstance) {
		
		
	}
}
