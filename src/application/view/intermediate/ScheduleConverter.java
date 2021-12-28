package application.view.intermediate;

import java.util.ArrayList;
import java.util.List;

import application.model.Schedule;
import application.model.Trucks;

public class ScheduleConverter {

	// zamiana obiektów schedule na obiekty poœrednie do wyœwietlenia

	public static ScheduleIntermediate convert(Schedule schedule) {

		return new ScheduleIntermediate(schedule);

	}

	// zamiana listy obiektów schedule na listê obiektów poœrednich do wyœwietlenia

	public static List<ScheduleIntermediate> convert(List<Schedule> schedules) {

		List<ScheduleIntermediate> result = new ArrayList<ScheduleIntermediate>();

		for (Schedule schedule : schedules) {
			result.add(convert(schedule));
		}

		return result;

	}
	

	}




