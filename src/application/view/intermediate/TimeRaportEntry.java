package application.view.intermediate;

import java.util.Date;

import application.Main;

public class TimeRaportEntry {

	public String dateString;
	public Date date;
	public Float minTime;
	public Float maxTime;
	public Float avgTime;
	
	public Float minTimeMinutes;
	public Float maxTimeMinutes;
	public Float avgTimeMinutes;
	

	public String toCSV() {
		return dateString + ";" + minTimeMinutes + ";" + maxTimeMinutes + ";" + avgTimeMinutes;
	}

	public TimeRaportEntry(Date date, Float minTime, Float maxTime, Float avgTime) {
		super();
		this.date = date;
		this.dateString = Main.getDateFormat().format(date);
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.avgTime = avgTime;
		this.minTimeMinutes = (minTime/1000)/60;
		this.maxTimeMinutes = (maxTime/1000)/60;
		this.avgTimeMinutes = (avgTime/1000)/60;
		
		
		
	}
	
	public static String getCSVHeader() {
		return "Data; Minimalny czas; Maksymalny czas; �redni czas";
	}

	public String getDateString() {
		return dateString;
	}

	public Date getDate() {
		return date;
	}

	public Float getMinTime() {
		return minTime;
	}

	public Float getMaxTime() {
		return maxTime;
	}

	public Float getAvgTime() {
		return avgTime;
	}

	public Float getMinTimeMinutes() {
		return minTimeMinutes;
	}

	public Float getMaxTimeMinutes() {
		return maxTimeMinutes;
	}

	public Float getAvgTimeMinutes() {
		return avgTimeMinutes;
	}

	
		
	

}
