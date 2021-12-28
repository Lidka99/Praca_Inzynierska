package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.password4j.BCryptFunction;
import com.password4j.types.BCrypt;

import application.model.*;

public class TrailersController {
	
	private Dao<Trailers, Integer> trailersDao;

	private JdbcConnectionSource dataSourceConnection;

	public TrailersController(JdbcConnectionSource dataSourceConnection) {

		this.dataSourceConnection = dataSourceConnection;

		try {
			trailersDao = DaoManager.createDao(dataSourceConnection, Trailers.class);
			System.out.println(trailersDao.countOf());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Trailers getByTrailerNumber(String trailersNumber) {
		for (Trailers trailer : trailersDao) {

			if(trailersNumber.toLowerCase().equals(trailer.getTrailerNumber().toLowerCase())) {
				return trailer;
			}
		}
		
		return null;
	}
	
	public List<Trailers> getAllTrailers() {

		List<Trailers> allTrailers = new ArrayList<Trailers>();

		for (Trailers trailer : trailersDao) {

			allTrailers.add(trailer);
		}

		return allTrailers;
	}

	// tworzenie u¿ytkowników

	public boolean create(String trailerNumber, String trailerType, Integer maxLoad) {
		Trailers newTrailer = new Trailers(trailerNumber, trailerType, maxLoad);
		try {
			trailersDao.create(newTrailer);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// edycja naczep
	
	public boolean update(int id,  String trailerNumber, String trailerType, Integer maxLoad) {

		Trailers trailer = getTrailer(id);
		trailer.setTrailerNumber(trailerNumber);
		trailer.setTrailerType(trailerType);
		trailer.setMaxLoad(maxLoad);
		

		try {
			trailersDao.update(trailer);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// usuwanie naczepy

	public boolean delete(int id) {

		Trailers trailer = getTrailer(id);
		return delete(trailer);

	}

	public boolean delete(Trailers trailer) {

		if (trailer != null) {

			try {
				trailersDao.delete(trailer);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;

		}
		return false;
	}

	public Trailers getTrailer(int id) {

		for (Trailers trailer : trailersDao) {

			if (id == trailer.getId()) {

				return trailer;
			}
		}
		return null;
	}


	public List<Trailers> searchTrailersByTrailerNumber(String text) {

		List<Trailers> allTrailers = new ArrayList<Trailers>();

		for (Trailers trailer : trailersDao) {

			if (trailer.getTrailerNumber().toLowerCase().startsWith(text.toLowerCase()))

				allTrailers.add(new Trailers(trailer));
		}

		return allTrailers;
	}


	// sprawdzanie czy istnieje naczepa o danym nr rejestracyjnym

	public boolean checkUsername(String trailerNumber, int ignoredId) {

		for (Trailers trailer : trailersDao) {

			if (trailerNumber.equals(trailer.getTrailerNumber()) && trailer.getId() != ignoredId) {

				return true;
			}

		}

		return false;
	}

}
