package hr.java.vjezbe.sortiranje;

import java.util.Comparator;

import hr.java.vjezbe.entitet.Profesor;

public class ProfesorSorter implements Comparator<Profesor>{

	@Override
	public int compare(Profesor p1, Profesor p2) {
		if (p1.getPrezime().compareTo(p2.getPrezime()) > 0) {
			return 1;
		} else if (p1.getPrezime().compareTo(p2.getPrezime()) < 0) {
			return -1;
		} else {
			if (p1.getIme().compareTo(p2.getIme()) > 0) {
				return 1;
			} else if (p1.getIme().compareTo(p2.getIme()) < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}

}
