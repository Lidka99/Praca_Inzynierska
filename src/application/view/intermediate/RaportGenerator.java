package application.view.intermediate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import application.Main;

public class RaportGenerator {

	public static List<TimeRaportEntry> generateTimeRaport(List<ScheduleIntermediate> schedules) {

		List<TimeRaportEntry> raport = new ArrayList<TimeRaportEntry>();

		Map<Date, List<ScheduleIntermediate>> groupSchedules = groupByDate(schedules);

		

		for (Entry<Date, List<ScheduleIntermediate>> entry : groupSchedules.entrySet()) {
			float min = Float.MAX_VALUE;
			float max = Float.MIN_VALUE;
			float sum = 0;
			
			for (ScheduleIntermediate schedule : entry.getValue()) {
				Date arrivalDate = null;

				try {
					arrivalDate = Main.getDateTimeFormat().parse(schedule.getArrival_date());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Date departureDate = null;

				try {
					departureDate = Main.getDateTimeFormat().parse(schedule.getDeparture_date());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				float time = departureDate.getTime() - arrivalDate.getTime(); // zwraca czas w milisekundach

				if (min > time) {

					min = time;
				}

				if (max < time) {

					max = time;
				}
				
				sum += time;

			}
			
			float avg = sum/entry.getValue().size();
			
			raport.add(new TimeRaportEntry(entry.getKey(), min, max, avg));
		}

		return raport;
	}
	
	
	
	public static List<CountRaportEntry> generateCountRaport(List<ScheduleIntermediate> schedules) {

		List<CountRaportEntry> raport = new ArrayList<CountRaportEntry>();

		Map<Date, List<ScheduleIntermediate>> groupSchedules = groupByDate(schedules);

		

		for (Entry<Date, List<ScheduleIntermediate>> entry : groupSchedules.entrySet()) {
			int arrivalCount = 0;
			
			for (ScheduleIntermediate schedule : entry.getValue()) {
				
				if (schedule.getArrival_date() != null) {
					arrivalCount++;
				}
			}
			
					
			raport.add(new CountRaportEntry(entry.getKey(), arrivalCount));
		}

		return raport;
	}

	private static Map<Date, List<ScheduleIntermediate>> groupByDate(List<ScheduleIntermediate> schedules) {

		Map<Date, List<ScheduleIntermediate>> raport = new HashMap<Date, List<ScheduleIntermediate>>();

		

		for (ScheduleIntermediate schedule : schedules) {
			Date arrivalDate = null;

			try {
				arrivalDate = Main.getDateTimeFormat().parse(schedule.getArrival_date());
				arrivalDate = Main.getDateFormat().parse(Main.getDateFormat().format(arrivalDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (arrivalDate != null) {
				if (raport.containsKey(arrivalDate)) {
					raport.get(arrivalDate).add(schedule);

				} else {
					List<ScheduleIntermediate> list = new ArrayList<ScheduleIntermediate>();
					list.add(schedule);
					raport.put(arrivalDate, list);
				}
			}
		}

		return raport;

	}

}
