package semato.semato_med.util;

import semato.semato_med.model.Visit;

import java.util.Comparator;

public class VisitComparator implements Comparator<Visit> {

    @Override
    public int compare(Visit v1, Visit v2) {
        return v1.compareTo(v2);
    }

}
