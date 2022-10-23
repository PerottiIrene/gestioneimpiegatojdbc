package it.prova.gestioneimpiegatojdbc.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Element;

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

			testFindByExample(impiegatoDAOInstance);
			System.out.println("in tabella impiegato ci sono " + compagniaDAOInstance.list().size() + "elementi");

			testUpdate(impiegatoDAOInstance);
			System.out.println("in tabella impiegato ci sono " + compagniaDAOInstance.list().size() + "elementi");

			testUpdate(compagniaDAOInstance);
			System.out.println("in tabella compagnia ci sono " + compagniaDAOInstance.list().size() + "elementi");

			testFindByExample(compagniaDAOInstance);
			System.out.println("in tabella compagnia ci sono " + compagniaDAOInstance.list().size() + "elementi");

			testFindAllByDataAssunzioneMaggioreDi(compagniaDAOInstance, impiegatoDAOInstance);
			System.out.println("in tabella compagnia ci sono " + compagniaDAOInstance.list().size() + "elementi");

			testFindAllByRagioneSocialeContiene(compagniaDAOInstance);
			System.out.println("in tabella compagnia ci sono " + compagniaDAOInstance.list().size() + "elementi");

			testErroriAssunzione(impiegatoDAOInstance, compagniaDAOInstance);
			System.out.println("in tabella compagnia ci sono " + compagniaDAOInstance.list().size() + "elementi");

			testCountByDataFondazioneCompagniaGreaterThan(impiegatoDAOInstance, compagniaDAOInstance);
			System.out.println("in tabella compagnia ci sono " + compagniaDAOInstance.list().size() + "elementi");

			testFindAllBycompagnia(impiegatoDAOInstance, compagniaDAOInstance);
			System.out.println("in tabella impiegato ci sono " + impiegatoDAOInstance.list().size() + "elementi");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void testInsertImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testInsertImpiegato inizio.............");
		Compagnia compagnia1 = new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29));
		int quantiElementiInseriti = -1;
		quantiElementiInseriti = impiegatoDAOInstance.insert(new Impiegato("irene", "perotti", "64fesgah",
				new java.sql.Date(2020, 05, 04), new java.sql.Date(1999, 04, 07), compagnia1));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInserImpiegato : FAILED");

		System.out.println(".......testInsertImpiegato fine: PASSED.............");

		impiegatoDAOInstance.deleteAll();
	}

	private static void testDeleteImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		// me ne creo uno al volo
		Compagnia compagnia1 = new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29));
		int quantiElementiInseriti = impiegatoDAOInstance.insert(new Impiegato("Giuseppe", "Verdi", "4599",
				new java.sql.Date(2000, 05, 14), new java.sql.Date(1999, 03, 14), compagnia1));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testDeleteImpiegato : FAILED, user da rimuovere non inserito");

		List<Impiegato> elencoVociPresenti = impiegatoDAOInstance.list();
		int numeroElementiPresentiPrimaDellaRimozione = elencoVociPresenti.size();
		if (numeroElementiPresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteImpiegao : FAILED, non ci sono voci sul DB");

		Impiegato ultimoDellaLista = elencoVociPresenti.get(numeroElementiPresentiPrimaDellaRimozione - 1);
		impiegatoDAOInstance.delete(ultimoDellaLista);

		// ricarico per vedere se sono scalati di una unità
		int numeroElementiPresentiDopoDellaRimozione = impiegatoDAOInstance.list().size();
		if (numeroElementiPresentiDopoDellaRimozione != numeroElementiPresentiPrimaDellaRimozione - 1)
			throw new RuntimeException("testDeleteImpigato : FAILED, la rimozione non è avvenuta");

		System.out.println(".......testDeleteImpiegato fine: PASSED.............");

		impiegatoDAOInstance.deleteAll();
	}

	private static void testFindByExample(ImpiegatoDAO impiegatoDAOInstance) throws Exception {

		System.out.println(".......testFindByExample inizio.............");

		// inserisco i dati che poi mi aspetto di ritrovare
		Compagnia compagnia1 = new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29));
		impiegatoDAOInstance.insert(new Impiegato("marco", "Verdi", "4599", new java.sql.Date(2000, 05, 14),
				new java.sql.Date(1999, 03, 14), compagnia1));
		impiegatoDAOInstance.insert(new Impiegato("Giuseppe", "Verdi", "4599", new java.sql.Date(2000, 05, 14),
				new java.sql.Date(1999, 03, 14), compagnia1));

		// preparo un example che ha come nome 'as' e ricerco
		List<Impiegato> risultatifindByExample = impiegatoDAOInstance.findByExample(new Impiegato("marco"));
		if (risultatifindByExample.size() != 1)
			throw new RuntimeException("testFindByExample FAILED ");

		// se sono qui il test è ok quindi ripulisco i dati che ho inserito altrimenti
		// la prossima volta non sarebbero 2 ma 4, ecc.
		for (Impiegato element : risultatifindByExample) {
			impiegatoDAOInstance.delete(element);
		}

		System.out.println(".......testFindByExample PASSED.............");

		impiegatoDAOInstance.deleteAll();
	}

	private static void testUpdate(ImpiegatoDAO impiegatoDAOInstance) throws Exception {

		Compagnia compagnia1 = new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29));

		if (impiegatoDAOInstance.insert(new Impiegato("marco", "rossi", "123456", new java.sql.Date(1999, 03, 14),
				new java.sql.Date(2020, 03, 14), compagnia1)) != 1)
			throw new RuntimeException("testUpdateImpiegato: inserimento preliminare FAILED ");

		// recupero col findbyexample e mi aspetto di trovarla
		List<Impiegato> risultatifindByExample = impiegatoDAOInstance.findByExample(new Impiegato("marco", "rossi"));
		if (risultatifindByExample.size() != 1)
			throw new RuntimeException("testUpdateImpiegato: testFindByExample FAILED ");

		// mi metto da parte l'id su cui lavorare per il test
		Long idImpiagto = risultatifindByExample.get(0).getId();

		// ricarico per sicurezza con l'id individuato e gli modifico un campo
		String nuovoNome = "irene";
		Impiegato toBeUpdated = impiegatoDAOInstance.get(idImpiagto);
		toBeUpdated.setNome(nuovoNome);
		if (impiegatoDAOInstance.update(toBeUpdated) != 1)
			throw new RuntimeException("testUpdateImpiegato FAILED ");

		for (Impiegato element : impiegatoDAOInstance.list()) {
			impiegatoDAOInstance.delete(element);
		}

		System.out.println(".......testUpdateImpiegato PASSED.............");
	}

	private static void testErroriAssunzione(ImpiegatoDAO impiegatoDAOInstance, CompagniaDAO compagniaDAOInstance)
			throws Exception {

		Date dataCompagnia = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2009");
		Date dataNascita1 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2003");
		Date dataAssunzione1 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2005");
		Date dataNascita2 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2009");
		Date dataAssunzione2 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2004");

		Compagnia compagnia2 = new Compagnia("compagnia1", 300000, dataCompagnia);
		Impiegato Impiegato1 = new Impiegato("marco", "rossi", "123456", dataNascita1, dataAssunzione1, compagnia2);
		Impiegato impiegato2 = new Impiegato("marco", "rossi", "123456", dataNascita2, dataAssunzione2, compagnia2);

		int quantiElementiInseiti = -1;
		quantiElementiInseiti = impiegatoDAOInstance.insert(Impiegato1);
		quantiElementiInseiti = impiegatoDAOInstance.insert(impiegato2);
		quantiElementiInseiti = compagniaDAOInstance.insert(compagnia2);
		if (quantiElementiInseiti < 1)
			throw new RuntimeException("testInsert FAILED ");

		List<Impiegato> impiegatiConErroriDiAssunzione = new ArrayList<Impiegato>();
		impiegatiConErroriDiAssunzione = impiegatoDAOInstance.findAllErroriAssunzione();

		for (Impiegato element : impiegatiConErroriDiAssunzione) {
			System.out.println("gli impiegati con erroti di assunzione sono " + element.toString());
		}

		impiegatoDAOInstance.deleteAll();
		compagniaDAOInstance.deleteAll();
	}

	private static void testFindAllBycompagnia(ImpiegatoDAO impiegatoDAOInstance, CompagniaDAO compagniaDAOInstance)
			throws Exception {

		Date dataCompagnia = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2020");
		Date dataNascita1 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2003");
		Date dataAssunzione1 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2005");
		Date dataNascita2 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2009");
		Date dataAssunzione2 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2004");

		Compagnia compagnia1 = new Compagnia("compagnia1", 300000, dataCompagnia);
		Compagnia compagnia2 = new Compagnia("compagnia1", 300000, dataCompagnia);
		Impiegato Impiegato1 = new Impiegato("marco", "rossi", "123456", dataNascita1, dataAssunzione1, compagnia1);
		Impiegato impiegato2 = new Impiegato("marco", "rossi", "123456", dataNascita2, dataAssunzione2, compagnia2);

		int quantiElementiInseiti = -1;
		quantiElementiInseiti = compagniaDAOInstance.insert(compagnia2);
		quantiElementiInseiti = compagniaDAOInstance.insert(compagnia1);
		quantiElementiInseiti = impiegatoDAOInstance.insert(Impiegato1);
		quantiElementiInseiti = impiegatoDAOInstance.insert(impiegato2);
		if (quantiElementiInseiti < 1)
			throw new RuntimeException("testInsert FAILED ");

		compagnia2.setId(impiegato2.getId());
		compagnia1.setId(Impiegato1.getId());

		List<Impiegato> impiegatiCheFannoParteDiCompagnia = impiegatoDAOInstance.findAllByCompagnia(compagnia2);
		for (Impiegato element : impiegatiCheFannoParteDiCompagnia) {
			System.out.println("gli impegati che fanno parte dellA compagnia sono " + element);
		}

		impiegatoDAOInstance.deleteAll();
		compagniaDAOInstance.deleteAll();
	}

	private static void testCountByDataFondazioneCompagniaGreaterThan(ImpiegatoDAO impiegatoDAOInstance,
			CompagniaDAO compagniaDAOInstance) throws Exception {

		Date dataCompagnia = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2020");
		Date dataNascita1 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2003");
		Date dataAssunzione1 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2005");
		Date dataNascita2 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2009");
		Date dataAssunzione2 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2004");

		Compagnia compagnia2 = new Compagnia("compagnia1", 300000, dataCompagnia);
		Impiegato Impiegato1 = new Impiegato("marco", "rossi", "123456", dataNascita1, dataAssunzione1, compagnia2);
		Impiegato impiegato2 = new Impiegato("marco", "rossi", "123456", dataNascita2, dataAssunzione2, compagnia2);

		int quantiElementiInseiti = -1;
		quantiElementiInseiti = impiegatoDAOInstance.insert(Impiegato1);
		quantiElementiInseiti = impiegatoDAOInstance.insert(impiegato2);
		quantiElementiInseiti = compagniaDAOInstance.insert(compagnia2);
		if (quantiElementiInseiti < 1)
			throw new RuntimeException("testInsert FAILED ");

		Date dataConfronto = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2009");

		int compagnieConDataFondzioneMagioreDi = impiegatoDAOInstance
				.countByDataFondazioneCompagniaGreaterThan(dataConfronto);
		System.out.println("le compagnie con data fondazione maggiore di quella confrontata sono "
				+ compagnieConDataFondzioneMagioreDi);

		impiegatoDAOInstance.deleteAll();
		compagniaDAOInstance.deleteAll();
	}

	// test Compagnia=======================

	private static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testInsertCompagnia inizio.............");
		int quantiElementiInseriti = -1;
		quantiElementiInseriti = compagniaDAOInstance
				.insert(new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29)));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertCompagnia : FAILED");

		System.out.println(".......testInsertCompagnia fine: PASSED.............");

		compagniaDAOInstance.deleteAll();
	}

	private static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		// me ne creo uno al volo

		int quantiElementiInseriti = compagniaDAOInstance
				.insert(new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29)));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testDeleteCompagnia : FAILED, user da rimuovere non inserito");

		List<Compagnia> elencoVociPresenti = compagniaDAOInstance.list();
		int numeroElementiPresentiPrimaDellaRimozione = elencoVociPresenti.size();
		if (numeroElementiPresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteComapgnia : FAILED, non ci sono voci sul DB");

		Compagnia ultimoDellaLista = elencoVociPresenti.get(numeroElementiPresentiPrimaDellaRimozione - 1);
		compagniaDAOInstance.delete(ultimoDellaLista);

		// ricarico per vedere se sono scalati di una unità
		int numeroElementiPresentiDopoDellaRimozione = compagniaDAOInstance.list().size();
		if (numeroElementiPresentiDopoDellaRimozione != numeroElementiPresentiPrimaDellaRimozione - 1)
			throw new RuntimeException("testDeleteCompagnia : FAILED, la rimozione non è avvenuta");

		System.out.println(".......testDeleteCompagnia fine: PASSED.............");

		compagniaDAOInstance.deleteAll();
	}

	private static void testUpdate(CompagniaDAO compagniaDAOInstance) throws Exception {

		if (compagniaDAOInstance.insert(new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29))) != 1)
			throw new RuntimeException("testUpdateCompagnia: inserimento preliminare FAILED ");

		// recupero col findbyexample e mi aspetto di trovarla
		List<Compagnia> risultatifindByExample = compagniaDAOInstance.findByExample(new Compagnia("compagnia1"));
		if (risultatifindByExample.size() != 1)
			throw new RuntimeException("testUpdateCompagnia: testFindByExample FAILED ");

		// mi metto da parte l'id su cui lavorare per il test
		Long idCompagnia = risultatifindByExample.get(0).getId();

		// ricarico per sicurezza con l'id individuato e gli modifico un campo
		String nuovoNome = "societa";
		Compagnia toBeUpdated = compagniaDAOInstance.get(idCompagnia);
		toBeUpdated.setRagioneSociale(nuovoNome);
		if (compagniaDAOInstance.update(toBeUpdated) != 1)
			throw new RuntimeException("testUpdateCompagnia FAILED ");

		compagniaDAOInstance.deleteAll();

		System.out.println(".......testUpdateCompagnia PASSED.............");
	}

	private static void testFindByExample(CompagniaDAO compagniaDAOInstance) throws Exception {

		System.out.println(".......testFindByExample inizio.............");

		// inserisco i dati che poi mi aspetto di ritrovare
		compagniaDAOInstance.insert(new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29)));
		compagniaDAOInstance.insert(new Compagnia("compagnia1", 300000, new java.sql.Date(2000, 07, 29)));

		// preparo un example che ha come nome 'as' e ricerco
		List<Compagnia> risultatifindByExample = compagniaDAOInstance.findByExample(new Compagnia("compagnia1"));
		if (risultatifindByExample.size() != 2)
			throw new RuntimeException("testFindByExample FAILED ");

		// se sono qui il test è ok quindi ripulisco i dati che ho inserito altrimenti
		// la prossima volta non sarebbero 2 ma 4, ecc.
		for (Compagnia element : risultatifindByExample) {
			compagniaDAOInstance.delete(element);
		}

		System.out.println(".......testFindByExample PASSED.............");

		compagniaDAOInstance.deleteAll();
	}

	private static void testFindAllByDataAssunzioneMaggioreDi(CompagniaDAO compagniaDAOInstance,
			ImpiegatoDAO impiegatoDAOInstance) throws Exception {

		Date dataCompagnia = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2009");
		Date dataNascita1 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2009");
		Date dataAssunzione1 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2020");
		Date dataNascita2 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2009");
		Date dataAssunzione2 = new SimpleDateFormat("dd-MM-yyyy").parse("03-01-2022");

		Compagnia compagnia2 = new Compagnia("compagnia1", 300000, dataCompagnia);
		Impiegato Impiegato1 = new Impiegato("marco", "rossi", "123456", dataNascita1, dataAssunzione1, compagnia2);
		Impiegato impiegato2 = new Impiegato("marco", "rossi", "123456", dataNascita2, dataAssunzione2, compagnia2);

		int quantiElementiInseiti = -1;
		quantiElementiInseiti = impiegatoDAOInstance.insert(Impiegato1);
		quantiElementiInseiti = impiegatoDAOInstance.insert(impiegato2);
		quantiElementiInseiti = compagniaDAOInstance.insert(compagnia2);
		if (quantiElementiInseiti < 1)
			throw new RuntimeException("testInsert FAILED ");

		Date dataConfronto = new SimpleDateFormat("dd-MM-yyyy").parse("03-07-2010");

		List<Impiegato> listaImpiegati = new ArrayList<Impiegato>();
		listaImpiegati.add(Impiegato1);
		listaImpiegati.add(impiegato2);
		compagnia2.setImpiegati(listaImpiegati);

		List<Compagnia> listaCompagniaConImpiegatiConDataAssunzioneMaggioreDi = new ArrayList<Compagnia>();
		listaCompagniaConImpiegatiConDataAssunzioneMaggioreDi = compagniaDAOInstance
				.findAllByDataAssunzioneMaggioreDi(dataConfronto);

		for (Compagnia element : listaCompagniaConImpiegatiConDataAssunzioneMaggioreDi) {
			System.out.println("le compagnie con impiegati con data assunzone maggiore di quella controllata sono "
					+ element.toString());
		}

		compagniaDAOInstance.deleteAll();
	}

	private static void testFindAllByRagioneSocialeContiene(CompagniaDAO compagniaDAOInstance) throws Exception {

		Compagnia compagnia1 = new Compagnia("societa", 300000, new Date());
		Compagnia compagnia2 = new Compagnia("compagnia1", 300000, new Date());
		Compagnia compagnia3 = new Compagnia("compagnia1", 300000, new Date());

		int quantiElementiInseiti = -1;
		quantiElementiInseiti = compagniaDAOInstance.insert(compagnia1);
		quantiElementiInseiti = compagniaDAOInstance.insert(compagnia3);
		quantiElementiInseiti = compagniaDAOInstance.insert(compagnia2);
		if (quantiElementiInseiti < 1)
			throw new RuntimeException("testInsert FAILED ");

		List<Compagnia> compagnieConRagioneSocialeCheContiene = new ArrayList<Compagnia>();
		compagnieConRagioneSocialeCheContiene = compagniaDAOInstance.findAllByRagioneSocialeContiene("co");

		for (Compagnia element : compagnieConRagioneSocialeCheContiene) {
			System.out.println(
					"le compagnie la cui ragione sociale contiene la stringa cercata sono " + element.toString());
		}

		compagniaDAOInstance.deleteAll();
	}

}
