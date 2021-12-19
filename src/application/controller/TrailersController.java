package application.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

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

			if(trailersNumber.toLowerCase().equals(trailer.getTrailer_number().toLowerCase())) {
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
	

}
