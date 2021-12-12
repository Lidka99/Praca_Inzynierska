package application.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "trailers")


public class Trailers {

	@DatabaseField(generatedId = true)
	Integer id;
	@DatabaseField(canBeNull = false)
	String trailer_number;
	@DatabaseField(canBeNull = false)
	String trailer_type;
	
	public Trailers() {

	}
	
	public Trailers(String trailer_number, String trailer_type) {
		this.trailer_number = trailer_number;
		this.trailer_type = trailer_type;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTrailer_number() {
		return trailer_number;
	}

	public void setTrailer_number(String trailer_number) {
		this.trailer_number = trailer_number;
	}

	public String getTrailer_type() {
		return trailer_type;
	}

	public void setTrailer_type(String trailer_type) {
		this.trailer_type = trailer_type;
	}
	
	
	
}
