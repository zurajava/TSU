package ge.tsu.bean;

import ge.tsu.algorithm.City;
import ge.tsu.algorithm.FloydWarshall;
import ge.tsu.algorithm.GA;
import ge.tsu.algorithm.Population;
import ge.tsu.algorithm.Tour;
import ge.tsu.algorithm.TourManager;
import ge.tsu.utils.CountDistanceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

@ManagedBean(name = "mapBeanTest")
@ViewScoped
public class MapBeanTest implements Serializable {
	private static final long serialVersionUID = 1L;
	private static List<City> listPoligon = new ArrayList<City>();
	private MapModel mapModel;
	private List<CitiList> citiList;
	private String title;
	private double lat;
	private double lng;
	private String input;
	private Polyline polyline;
	private int count = 1;
	@ManagedProperty(value = "#{matric}")
	private Matric matric;
	private boolean testMode = true;

	private String zoom = "5";

	public MapBeanTest() {
		mapModel = new DefaultMapModel();
	}

	public void addMarker(ActionEvent actionEvent) {
		if (listPoligon == null) {
			listPoligon = new ArrayList<City>();
		}
		City city = new City(count + 1, lat, lng);

		listPoligon.add(city);

		TourManager.addCity(city);
		mapModel.addOverlay(new Marker(new LatLng(lat, lng), title));
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added",
				"Lat:" + lat + ", Lng:" + lng));
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		System.out.println(testMode);
		this.testMode = testMode;
	}

	public Matric getMatric() {
		return matric;
	}

	public void setMatric(Matric matric) {
		this.matric = matric;
	}

	public String getZoom() {
		System.out.println(zoom);
		return zoom;
	}

	public void setZoom(String zoom) {
		System.out.println(zoom);
		this.zoom = zoom;
	}

	public void submit() {
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Form submitted", "Amount markers: "
						+ mapModel.getMarkers().size() + ", Input: " + input));
	}

	public void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public static List<City> getListPoligon() {
		return listPoligon;
	}

	public static void setListPoligon(List<City> listPoligon) {
		MapBeanTest.listPoligon = listPoligon;
	}

	public List<CitiList> getCitiList() {
		return citiList;
	}

	public void setCitiList(List<CitiList> citiList) {
		this.citiList = citiList;
	}

	public Polyline getPolyline() {
		return polyline;
	}

	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public void route() {

		try {
			Population pop = new Population(400, true);
			pop = GA.evolvePopulation(pop);
			for (int i = 0; i < 100; i++) {
				pop = GA.evolvePopulation(pop);
			}
			if (polyline != null) {
				polyline = null;
			}
			polyline = new Polyline();
			Tour t = pop.getFittest();
			ArrayList<City> l = t.getTour();
			for (int i = 0; i < l.size(); i++) {
				polyline.getPaths().add(
						new LatLng(l.get(i).getX(), l.get(i).getY()));
			}
			polyline.setStrokeWeight(5);
			polyline.setStrokeColor("#FF0000");
			polyline.setStrokeOpacity(0.2);

			mapModel.addOverlay(polyline);
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Route....", "Final distance: "
							+ pop.getFittest().getDistance()));
		} catch (Exception e) {

		}
	}

	public void clean() {
		listPoligon = null;
		polyline = null;
		mapModel = null;
		mapModel = new DefaultMapModel();
		TourManager.clearList();
		getMatric().setFloydHtml("");
		getMatric().setHtml("");
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Clean Map...",
				""));
	}

	public String showMatric() {
		ResponseFromService[][] mas = null;
		try {
			citiList = new ArrayList<CitiList>();
			ResponseFromService res = null;
			StringBuffer sb = new StringBuffer();
			sb.append("<table border=\"1\"  BORDERCOLOR=\"#85A366\" style=\"width:300px\">");
			mas = new ResponseFromService[listPoligon.size()][listPoligon
					.size()];

			for (int i = 0; i < listPoligon.size(); i++) {
				sb.append("<tr>");
				for (int j = 0; j < listPoligon.size(); j++) {
					sb.append("<td>");
					res = CountDistanceManager.getDistance(
							String.valueOf(listPoligon.get(i).getX()),
							String.valueOf(listPoligon.get(i).getY()),
							String.valueOf(listPoligon.get(j).getX()),
							String.valueOf(listPoligon.get(j).getY()));
					mas[i][j] = res;
					sb.append(res.getDistance());
					System.out.print(res.getDistance());
					System.out.print(" ");
					sb.append("</td>");
				}
				System.out.println();
				sb.append("</tr>");
			}
			sb.append("<table>");
			System.out.println("Firs Html =  " + sb.toString());
			log(mas);
			getMatric().setHtml(sb.toString());
			int adjacency_matrix[][] = new int[mas.length][mas.length];
			for (int i = 0; i < mas.length; i++) {
				for (int j = 0; j < mas.length; j++) {
					if (mas[i][j].getDistance() == null
							|| mas[i][j].getDistance().isEmpty()) {
						adjacency_matrix[i][j] = 0;
					} else {
						adjacency_matrix[i][j] = Integer.parseInt(mas[i][j]
								.getDistance());
					}
				}
			}
			FloydWarshall f = new FloydWarshall(mas.length);
			System.out.println(f.floydwarshall1(adjacency_matrix));
			getMatric().setFloydHtml(f.floydwarshall1(adjacency_matrix));
			return "matric.xhtml";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "matric.xhtml";
	}

	public void log(ResponseFromService[][] mas) {
		if (polyline != null) {
			polyline = null;
		}
		polyline = new Polyline();
		for (int i = 0; i < mas.length; i++) {
			for (int j = 0; j < mas.length; j++) {

				System.out
						.println("=======================================================");
				System.out.println(mas[i][j].getDistance() + "; Start : "
						+ mas[i][j].getStartPosLat() + "; End : "
						+ mas[i][j].getStartPoslong());
				System.out
						.println("========================================================");
			}
		}

		polyline.setStrokeWeight(5);
		polyline.setStrokeColor("#FF0000");
		polyline.setStrokeOpacity(0.2);
		mapModel.addOverlay(polyline);
	}

}