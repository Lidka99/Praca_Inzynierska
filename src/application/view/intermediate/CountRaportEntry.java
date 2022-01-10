package application.view.intermediate;

import java.util.Date;

import application.Main;

public class CountRaportEntry {

	public String dateString;
	public Date date;
	public Integer arrivalsCount;
	
	

	public String toCSV() {
		return dateString + ";" + arrivalsCount;
	}

	public CountRaportEntry(Date date, Integer arrivalsCount) {
		super();
		this.date = date;
		this.dateString = Main.getDateFormat().format(date);
		this.arrivalsCount = arrivalsCount;
		
	
		
		
	}
	
	public static String getCSVHeader() {
		return "Data; Liczba przyjazdów";
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getArrivalsCount() {
		return arrivalsCount;
	}

	public void setArrivalsCount(Integer arrivalsCount) {
		this.arrivalsCount = arrivalsCount;
	}

	
	

}
