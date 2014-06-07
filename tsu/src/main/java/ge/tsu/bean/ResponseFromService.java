package ge.tsu.bean;

import java.util.List;

public class ResponseFromService {

	private String distance;
	private String startPosLat;
	private String startPoslong;
	private List<Step> listSteps;
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public List<Step> getListSteps() {
		return listSteps;
	}
	public void setListSteps(List<Step> listSteps) {
		this.listSteps = listSteps;
	}
	public String getStartPosLat() {
		return startPosLat;
	}
	public void setStartPosLat(String startPosLat) {
		this.startPosLat = startPosLat;
	}
	public String getStartPoslong() {
		return startPoslong;
	}
	public void setStartPoslong(String startPoslong) {
		this.startPoslong = startPoslong;
	}
	
	
	
}
