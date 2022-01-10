package application.view.intermediate;

import java.util.Comparator;

public class CountRaportEntryComparator implements Comparator<CountRaportEntry> {

	@Override
	public int compare(CountRaportEntry o1, CountRaportEntry o2) {
		// TODO Auto-generated method stub
		return o1.date.compareTo(o2.date);
	}

}
