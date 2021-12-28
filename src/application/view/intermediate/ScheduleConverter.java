package application.view.intermediate;

import java.util.ArrayList;
import java.util.List;

import application.model.Schedule;
import application.model.Trucks;

public class ScheduleConverter {

	// zamiana obiekt�w schedule na obiekty po�rednie do wy�wietlenia

	public static ScheduleIntermediate convert(Schedule schedule) {

		return new ScheduleIntermediate(schedule);

	}

	// zamiana listy obiekt�w schedule na list� obiekt�w po�rednich do wy�wietlenia

	public static List<ScheduleIntermediate> convert(List<Schedule> schedules) {

		List<ScheduleIntermediate> result = new ArrayList<ScheduleIntermediate>();

		for (Schedule schedule : schedules) {
			result.add(convert(schedule));
		}

		return result;

	}
	

	}




