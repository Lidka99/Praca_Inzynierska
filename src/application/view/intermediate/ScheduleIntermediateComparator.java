package application.view.intermediate;

import java.util.Comparator;

public class ScheduleIntermediateComparator implements Comparator<ScheduleIntermediate> {

	@Override
	public int compare(ScheduleIntermediate a, ScheduleIntermediate b) {

		return a.scheduleDateAsDate.compareTo(b.scheduleDateAsDate);
	}

}
