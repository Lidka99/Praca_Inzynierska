package application.view.intermediate;

import java.util.Comparator;

public class TimeRaportEntryComparator implements Comparator<TimeRaportEntry> {

	@Override
	public int compare(TimeRaportEntry o1, TimeRaportEntry o2) {
		// TODO Auto-generated method stub
		return o1.date.compareTo(o2.date);
	}

}
