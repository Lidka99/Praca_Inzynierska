package application.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "trailers")

public class Trailers {

	@DatabaseField(generatedId = true)
	Integer id;
	@DatabaseField(columnName = "trailer_number", canBeNull = false)
	String trailerNumber;
	@DatabaseField(columnName = "trailer_type", canBeNull = false)
	String trailerType;
	@DatabaseField(columnName = "max_load", canBeNull = false)
	Integer maxLoad;

	public Trailers() {

	}

	public Trailers(String trailer_number, String trailer_type, Integer max_load) {
		this.trailerNumber = trailer_number;
		this.trailerType = trailer_type;
		this.maxLoad = max_load;

	}

	public Trailers(Trailers trailer) {
		this.id = trailer.getId();
		this.trailerNumber = trailer.getTrailerNumber();
		this.trailerType = trailer.getTrailerType();
		this.maxLoad = trailer.getMaxLoad();

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTrailerNumber() {
		return trailerNumber;
	}

	public void setTrailerNumber(String trailer_number) {
		this.trailerNumber = trailer_number;
	}

	public String getTrailerType() {
		return trailerType;
	}

	public void setTrailerType(String trailer_type) {
		this.trailerType = trailer_type;
	}

	public Integer getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(Integer maxLoad) {
		this.maxLoad = maxLoad;
	}

}
