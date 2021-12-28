package application.view.intermediate;

import java.util.ArrayList;
import java.util.List;

import application.model.Trucks;

public class TrucksConverter {

	// zamiana obiekt�w schedule na obiekty po�rednie do wy�wietlenia

	public static TrucksIntermediate convert(Trucks truck) {

		return new TrucksIntermediate(truck);

	}

	// zamiana listy obiekt�w schedule na list� obiekt�w po�rednich do wy�wietlenia

	public static List<TrucksIntermediate> convert(List<Trucks> trucks) {

		List<TrucksIntermediate> result = new ArrayList<TrucksIntermediate>();

		for (Trucks truck : trucks) {
			result.add(convert(truck));
		}

		return result;

	}

}
