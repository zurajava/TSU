package ge.tsu.algorithm;

import java.util.ArrayList;

/**
 * @author zurab.katsitadze
 * 
 */
public class TSP_GA {

	public static void main(String[] args) {

		// Create and add our cities
		City city = new City(1,60, 200);
		TourManager.addCity(city);
		City city2 = new City(2,180, 200);
		TourManager.addCity(city2);
		City city3 = new City(3,80, 180);
		TourManager.addCity(city3);
		City city4 = new City(4,140, 180);
		TourManager.addCity(city4);
		City city5 = new City(5,20, 160);
		TourManager.addCity(city5);
		City city6 = new City(6,100, 160);
		TourManager.addCity(city6);
		City city7 = new City(7,200, 160);
		TourManager.addCity(city7);
		City city8 = new City(8,140, 140);
		TourManager.addCity(city8);
		City city9 = new City(9,40, 120);
		TourManager.addCity(city9);
		City city10 = new City(10,100, 120);
		TourManager.addCity(city10);
		City city11 = new City(11,180, 100);
		TourManager.addCity(city11);
		City city12 = new City(12,60, 80);
		TourManager.addCity(city12);
		City city13 = new City(13,120, 80);
		TourManager.addCity(city13);
		City city14 = new City(13,180, 60);
		TourManager.addCity(city14);
		City city15 = new City(14,20, 40);
		TourManager.addCity(city15);
		City city16 = new City(15,100, 40);
		TourManager.addCity(city16);
		City city17 = new City(16,200, 40);
		TourManager.addCity(city17);
		City city18 = new City(17,20, 20);
		TourManager.addCity(city18);
		City city19 = new City(18,60, 20);
		TourManager.addCity(city19);
		City city20 = new City(19,160, 20);
		TourManager.addCity(city20);

		// Initialize population
		Population pop = new Population(400, true);
		System.out.println("Initial distance: " + pop.getFittest().getDistance());

		// Evolve population for 100 generations
		pop = GA.evolvePopulation(pop);
		for (int i = 0; i < 100; i++) {
			pop = GA.evolvePopulation(pop);
		}

		// Print final results
		System.out.println("Finished");
		System.out.println("Final distance: " + pop.getFittest().getDistance());
		System.out.println("Solution:");
		System.out.println(pop.getFittest());
		
		
		Tour t = pop.getFittest();
		System.out.println(t.getTour().size());
		ArrayList<City> l = t.getTour();
		for(int i=0;i<l.size();i++){
			System.out.println(l.get(i));
		}
	}
}