package it.polito.tdp.meteo.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		Citta genova= new Citta("Genova");
		
        List<Citta> citta= m.getAllCitta();
		
        /*
		System.out.println(citta);
		
		System.out.println(m.getUmiditaMedia(12, genova));
		*/
		System.out.println(m.trovaSequenza(06));
		

	}

}
