package ge.tsu.bean;

import ge.tsu.algorithm.City;
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

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

/**
 * @author zurab.katsitadze
 * 
 */
@ManagedBean(name = "mapBean")
@ViewScoped
public class MapBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static List<City> listPoligon = new ArrayList<City>();
	private MapModel emptyModel;
	private Marker marker;
	private Polyline polyline;
	private String title;
	private String html;
	private double lat;
	private List<CitiList> citiList;
	private double lng;
	private int count = 1;
	
	@ManagedProperty(value="#{matric}")
	private Matric matric;
	
	
	public Matric getMatric() {
		return matric;
	}

	public void setMatric(Matric matric) {
		this.matric = matric;
	}

	public List<CitiList> getCitiList() {
		return citiList;
	}

	public void setCitiList(List<CitiList> citiList) {
		this.citiList = citiList;
	}

	public MapBean() {
		emptyModel = new DefaultMapModel();
	}

	public MapModel getEmptyModel() {
		return emptyModel;
	}

	public void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<City> getListPoligon() {
		return listPoligon;
	}

	public Polyline getPolyline() {
		return polyline;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
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

	public void onPointSelect(PointSelectEvent event) {
		LatLng latlng = event.getLatLng();
		if (listPoligon == null) {
			listPoligon = new ArrayList<City>();
		}
		City city = new City(count + 1, latlng.getLat(), latlng.getLng());
		listPoligon.add(city);
		Marker marker = new Marker(latlng, title);
		emptyModel.addOverlay(marker);
		TourManager.addCity(city);
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added",
				"Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng()));
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Marker Selected", marker.getTitle() + " Lat:"
						+ marker.getLatlng().getLat() + " Lng:"
						+ marker.getLatlng().getLng()));
	}

	public void route() {

		try {
//			String res = "";
//			StringBuffer sb = new StringBuffer();
//			sb.append("<table style=\"width:300px\">");
//
//			for (int i = 0; i < listPoligon.size(); i++) {
//				sb.append("<tr>");
//				for (int j = 0; j < listPoligon.size(); j++) {
//					sb.append("<td>");
//					res = CountDistanceManager.getDistance(
//							String.valueOf(listPoligon.get(i).getX()),
//							String.valueOf(listPoligon.get(i).getY()),
//							String.valueOf(listPoligon.get(j).getX()),
//							String.valueOf(listPoligon.get(j).getY()));
//					sb.append(res);
//					System.out.print(res);
//					System.out.print(" ");
//					sb.append("</td>");
//				}
//				System.out.println();
//				sb.append("</tr>");
//			}
//			sb.append("<table>");
//			this.html = sb.toString();
//			System.out.println(sb.toString());
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

			emptyModel.addOverlay(polyline);
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Route....", "Final distance: "
							+ pop.getFittest().getDistance()));
		} catch (Exception e) {

		}
	}

	public void clean() {
		listPoligon = null;
		marker = null;
		polyline = null;
		emptyModel = null;
		emptyModel = new DefaultMapModel();
		TourManager.clearList();
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Clean Map...",
				""));
	}

	public String showMatric() {
		try {
			citiList = new ArrayList<CitiList>();
			ResponseFromService res = null;
			StringBuffer sb = new StringBuffer();
			sb.append("<table border=\"1\"  BORDERCOLOR=\"#85A366\" style=\"width:300px\">");

			for (int i = 0; i < listPoligon.size(); i++) {
				sb.append("<tr>");
				for (int j = 0; j < listPoligon.size(); j++) {
					sb.append("<td>");
					
					res = CountDistanceManager.getDistance(
							String.valueOf(listPoligon.get(i).getX()),
							String.valueOf(listPoligon.get(i).getY()),
							String.valueOf(listPoligon.get(j).getX()),
							String.valueOf(listPoligon.get(j).getY()));
					sb.append(res.getDistance());
					System.out.print(res.getDistance());
					System.out.print(" ");
					sb.append("</td>");
				}
				System.out.println();
				sb.append("</tr>");
			}
			sb.append("<table>");
			System.out.println(sb.toString());
			getMatric().setHtml(sb.toString());
			return "matric.xhtml";
		} catch (Exception e) {

		}
		return "matric.xhtml";
	}
}
