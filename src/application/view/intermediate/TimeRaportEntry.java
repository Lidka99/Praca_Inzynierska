package application.view.intermediate;

import java.util.Date;

public class TimeRaportEntry {

	public String dateString;
	public Date date;
	public Float minTime;
	public Float maxTime;
	public Float avgTime;
	public String minTimeString;
	public String maxTimeString;
	public String avgTimeString;

	public String toCSV() {
		return date.toString() + ";" + minTime + ";" + maxTime + ";" + avgTime;
	}

	public TimeRaportEntry(Date date, Float minTime, Float maxTime, Float avgTime) {
		super();
		this.date = date;
		this.dateString = date.toString();
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.avgTime = avgTime;
		this.avgTimeString = avgTime.toString();
		
	}
	
	

}
