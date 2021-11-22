package application.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "schedule")
public class Schedule {

	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(canBeNull = false)
	Date scheduled_date;
	@DatabaseField(canBeNull = true)
	Date arrival_date;
	@DatabaseField(canBeNull = true)
	Date departure_date;
	@DatabaseField(canBeNull = true)
	String type;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	int trailer_id;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	int driver_id;
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	int truck_id;

	public Schedule() {

	}

	public Schedule(Date scheduled_date, Date arrival_date, Date departure_date, int trailer_id, int driver_id,
			int truck_id) {
		this.scheduled_date = scheduled_date;
		this.arrival_date = arrival_date;
		this.departure_date = departure_date;
		this.trailer_id = trailer_id;
		this.driver_id = driver_id;
		this.truck_id = truck_id;
	}

}
