package it.prova.gestioneimpiegatojdbc.dao.compagnia;

import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.IBaseDAO;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;

public interface CompagniaDAO extends IBaseDAO<Compagnia>{
	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(Date data) throws Exception;
	public List<Compagnia> findAllByRagioneSocialeContiene(String input) throws Exception;
	public int deleteAll() throws Exception;

}
