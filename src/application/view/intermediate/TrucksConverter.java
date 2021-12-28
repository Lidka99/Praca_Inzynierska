package application.view.intermediate;

import java.util.ArrayList;
import java.util.List;

import application.model.Trucks;

public class TrucksConverter {

	// zamiana obiektów schedule na obiekty poœrednie do wyœwietlenia

	public static TrucksIntermediate convert(Trucks truck) {

		return new TrucksIntermediate(truck);

	}

	// zamiana listy obiektów schedule na listê obiektów poœrednich do wyœwietlenia

	public static List<TrucksIntermediate> convert(List<Trucks> trucks) {

		List<TrucksIntermediate> result = new ArrayList<TrucksIntermediate>();

		for (Trucks truck : trucks) {
			result.add(convert(truck));
		}

		return result;

	}

}
