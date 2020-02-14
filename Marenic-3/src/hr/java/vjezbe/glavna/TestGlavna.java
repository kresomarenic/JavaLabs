package hr.java.vjezbe.glavna;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.java.vjezbe.iznimke.NeispravnoPrezimeException;

public class TestGlavna {
	
	private final static Logger log = LoggerFactory.getLogger(TestGlavna.class);
	
	private final static String DATE_TIME_FORMAT = "dd.MM.yyyy. HH:mm";	
	
	
	public static void main(String[] args) {
		
			Scanner input = new Scanner(System.in);
			
			System.out.println(unosPrezimena(input));
			
			
			input.close();		
	}
	

	private static String unosPrezimena(Scanner input) {
		
		String prezime = "";
		boolean inputSuccess = false;
		
		do {
			prezime = input.nextLine();			
			try {				
				if (!prezime.matches("[A-Za-z ]+")) {					
					throw new NeispravnoPrezimeException("Prezime može sadržavati samo velika slova, mala slova i razmak. Pokušajte ponovno");
				}
				inputSuccess = true;
			} catch (NeispravnoPrezimeException ex) {
				System.out.println(ex.getMessage());
				log.error(ex.getMessage(), ex);					
			}
		} while (!inputSuccess);
		
		return prezime;
	}
	
	
	
}
