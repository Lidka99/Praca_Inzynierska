package application.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "trailers")


public class Trailers {

	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField(canBeNull = false)
	int trailer_number;
	@DatabaseField(canBeNull = false)
	int trailer_type;
	
	public Trailers() {

	}
	
	public Trailers(int trailer_number, int trailer_type) {
		this.trailer_number = trailer_number;
		this.trailer_type = trailer_type;
		
	}
}
