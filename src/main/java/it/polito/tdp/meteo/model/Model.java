package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private List <Citta> leCitta;
	private List <Citta> best;
	

	public Model() {
		
		MeteoDAO dao= new MeteoDAO();
		this.leCitta=dao.getAllCitta();
	}
	
	public List<Citta> getLeCitta(){
		return leCitta;
	}
	
	// of course you can change the String output with what you think works best
	public Double getUmiditaMedia(int mese, Citta citta) {
		MeteoDAO  dao= new MeteoDAO();
		return dao.getUmiditaMedia(mese,citta);
		
	}
	
	public List<Citta> getAllCitta() {
		MeteoDAO dao= new MeteoDAO();
		return dao.getAllCitta();
	}
	

	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		List<Citta> parziale= new ArrayList<>();
		this.best=null;
		MeteoDAO dao= new MeteoDAO();
		for(Citta c: leCitta) {
			c.setRilevamenti(dao.getAllRilevamentiLocalitaMese(mese, c));
		}
		cerca(parziale,0);
		return best;
	}

	private void cerca(List<Citta> parziale, int livello) {
		if(livello== NUMERO_GIORNI_TOTALI) {
			//Calcolo il costo e se è migliore di best lo sostituisco
			double costo= calcolaCosto(parziale);
			if(best==null || calcolaCosto(best)>costo)
				best= new ArrayList<>(parziale);
		}else {
			//aggiungo la citta al parziale e ricalcolo
			//se verifico che questa aggiunta è valida
			for(Citta c: leCitta) {
				if(aggiuntaValida(c,parziale)) {
					parziale.add(c);
					cerca(parziale,livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}

	private boolean aggiuntaValida(Citta c, List<Citta> parziale) {
		// valida se rispetta i vincoli di:
		int conta=0;
		for(Citta precedente: parziale) {
			if(precedente.equals(c))
				conta++;
		}
		//giorni massimi
		if(conta>= NUMERO_GIORNI_CITTA_MAX )
			return false;
		
		//giorni minimi
		if(parziale.size()==0)
			//primo giorno accetto qualsiasi citta
			return true;
		if(parziale.size()==1 || parziale.size()==2)
			//secondo e terzo giorno rimango solo se la citta è la stessa
			return parziale.get(parziale.size()-1).equals(c);
		if(parziale.get(parziale.size()-1).equals(c))
			//se sono nella citta in cui ero il giorno prima per più di 3 giorni va bene
			return true;
		
		//se cambio citta devo assicurarmi di essere stato fermo nei tre giorni precedenti
		if((parziale.get(parziale.size()-1).equals(parziale.get(parziale.size()-2)) && parziale.get(parziale.size()-2).equals(parziale.get(parziale.size()-3))))
				return true;
		
		
		return false;
	}

	private double calcolaCosto(List<Citta> parziale) {
		double costo=0.0;
		
		// somma di tutte le umidita della citta
		for(int giorno=1; giorno<=NUMERO_GIORNI_TOTALI; giorno++ ) {
			Citta c= parziale.get(giorno-1); //da dove ero ieri
			double umid= c.getRilevamenti().get(giorno-1).getUmidita();//recupero l'umidità percepita
			costo+=umid;// e la aggiungo al costo
		}
		
		//più 100 per ogni volta che cambio città
	    for(int giorno=2; giorno<=NUMERO_GIORNI_TOTALI; giorno++) {
			if(!parziale.get(giorno-1).equals(parziale.get(giorno-2)))
				costo+=COST;
		}
		return costo;
	}
	

}
