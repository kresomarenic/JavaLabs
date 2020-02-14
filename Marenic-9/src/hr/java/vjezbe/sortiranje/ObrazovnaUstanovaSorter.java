package hr.java.vjezbe.sortiranje;

import java.util.Comparator;

import hr.java.vjezbe.entitet.ObrazovnaUstanova;

public class ObrazovnaUstanovaSorter implements Comparator<ObrazovnaUstanova>{

	@Override
	public int compare(ObrazovnaUstanova u1, ObrazovnaUstanova u2) {
		return ((Integer)u1.getStudenti().size()).compareTo((Integer)u2.getStudenti().size()) * (-1);
	}

}
