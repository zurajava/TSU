package ge.tsu.algorithm;

import java.util.ArrayList;

/**
 * @author zurab.katsitadze
 *
 */
public class TourManager{

    // Holds our cities
    private static ArrayList<City> destinationCities = new ArrayList<City>();

    // Adds a destination city
    public static void addCity(City city) {
    	if(destinationCities==null){
    		destinationCities = new ArrayList<City>();
    	}
        destinationCities.add(city);
    }
    
    // Get a city
    public static City getCity(int index){
        return (City)destinationCities.get(index);
    }
    
    // Get the number of destination cities
    public static int numberOfCities(){
        return destinationCities.size();
    }
    public static void clearList(){
    	destinationCities=null;
    }
    public static ArrayList<City> getCitys(){
    	return destinationCities;
    }
}