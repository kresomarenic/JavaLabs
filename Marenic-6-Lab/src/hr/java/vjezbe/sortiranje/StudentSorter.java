package hr.java.vjezbe.sortiranje;

import java.util.Comparator;

import hr.java.vjezbe.entitet.Student;

public class StudentSorter implements Comparator<Student>{

	@Override
	public int compare(Student s1, Student s2) {
		if (s1.getPrezime().compareTo(s2.getPrezime()) > 0) {
			return 1;
		} else if (s1.getPrezime().compareTo(s2.getPrezime()) < 0) {
			return -1;
		} else {
			if (s1.getIme().compareTo(s2.getIme()) > 0) {
				return 1;
			} else if (s1.getIme().compareTo(s2.getIme()) < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}

}
