package application.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "schedule")
public class Schedule {

	@DatabaseField(generatedId = true)
	Integer id;
	@DatabaseField(canBeNull = false)
	Date scheduled_date;
	@DatabaseField(canBeNull = true)
	Date arrival_date;
	@DatabaseField(canBeNull = true)
	Date departure_date;
	@DatabaseField(canBeNull = true)
	String type;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	Trailers trailer;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	Drivers driver;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	Trucks truck;

	public Schedule() {

	}

	public Schedule(Date scheduled_date, Date arrival_date, Date departure_date, Trailers trailer_id, Drivers driver_id,
			Trucks truck_id) {
		this.scheduled_date = scheduled_date;
		this.arrival_date = arrival_date;
		this.departure_date = departure_date;
		this.trailer = trailer_id;
		this.driver = driver_id;
		this.truck = truck_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getScheduled_date() {
		return scheduled_date;
	}

	public void setScheduled_date(Date scheduled_date) {
		this.scheduled_date = scheduled_date;
	}

	public Date getArrival_date() {
		return arrival_date;
	}

	public void setArrival_date(Date arrival_date) {
		this.arrival_date = arrival_date;
	}

	public Date getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(Date departure_date) {
		this.departure_date = departure_date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Trailers getTrailer_id() {
		return trailer;
	}

	public void setTrailer_id(Trailers trailer_id) {
		this.trailer = trailer_id;
	}

	public Drivers getDriver_id() {
		return driver;
	}

	public void setDriver_id(Drivers driver_id) {
		this.driver = driver_id;
	}

	public Trucks getTruck_id() {
		return truck;
	}

	public void setTruck_id(Trucks truck_id) {
		this.truck = truck_id;
	}

}
