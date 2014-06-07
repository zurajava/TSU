package ge.tsu.utils;

import ge.tsu.algorithm.City;
import ge.tsu.bean.ResponseFromService;
import ge.tsu.bean.Step;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class CountDistanceManager {
	public static Document getResponseDocument(URL url) throws IOException {
		HttpsURLConnection connection = (HttpsURLConnection) url
				.openConnection();

		connection.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		});

		SSLContext defaultCerts = trustAllCerts();
		connection.setSSLSocketFactory(defaultCerts.getSocketFactory());
		// connection.setDoOutput(true);
		connection.setReadTimeout(15000);

		InputStream in = connection.getInputStream();
		String response = convertStreamToString(in);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(convertStringToInputStream(response));
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SAXException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				in.close();
				connection.disconnect();
			} catch (Exception e) {

			}

		}
	}

	private static SSLContext trustAllCerts() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return sc;
	}

	private static String convertStreamToString(InputStream is)
			throws IOException {
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line = null;
			String newLine = System.getProperty("line.separator");

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				line = reader.readLine();
				do {
					sb.append(line);
					line = reader.readLine();
					if (line != null) {
						sb.append(newLine);
					}
				} while (line != null);
			} finally {
				is.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	private static InputStream convertStringToInputStream(String s)
			throws IOException {
		return new ByteArrayInputStream(s.getBytes("UTF-8"));
	}

	public static ResponseFromService getDistance(String startLat, String startLon,
			String endLat, String endLon) throws XPathExpressionException,
			IOException {
		ResponseFromService resonce=null;
		URL url = new URL(
				"https://maps.googleapis.com/maps/api/directions/xml?origin="
						+ startLat
						+ ","
						+ startLon
						+ "&destination="
						+ endLat
						+ ","
						+ endLon
						+ "&sensor=false&key=AIzaSyDv2iBh_jai__AwkgDMsJQywN5ubWz33E4");

		XPathFactory xfactory = XPathFactory.newInstance();
		XPath xpath = xfactory.newXPath();

		// firstname
		
		Document document = getResponseDocument(url);
		
		XPathExpression distanceEx = xpath.compile("/DirectionsResponse/route/leg/distance/value");
		XPathExpression startLocLat = xpath.compile("/DirectionsResponse/route/leg/start_location/lat");
		XPathExpression startLocLot = xpath.compile("/DirectionsResponse/route/leg/start_location/lng");
		
		String distance = (String) distanceEx.evaluate(document, XPathConstants.STRING);
		String stLat = (String) startLocLat.evaluate(document, XPathConstants.STRING);
		String stLot = (String) startLocLot.evaluate(document, XPathConstants.STRING);
		resonce = new ResponseFromService();
		resonce.setDistance(distance);
		resonce.setStartPosLat(stLat);
        resonce.setStartPoslong(stLot);
        
		NodeList nodeList = document.getElementsByTagName("step");
		List<Step> stepList = new ArrayList<Step>();
		Step s = null;
		for(int i=1;i<nodeList.getLength()-1;i++){
			
			Node nNode = nodeList.item(i);
			Element eElement = (Element) nNode;
			s=new Step(eElement.getElementsByTagName("lat").item(0).getTextContent(), eElement.getElementsByTagName("lng").item(0).getTextContent());
			stepList.add(s);
		}
		
		resonce.setListSteps(stepList);		
		return resonce;
	}

	public static void writeToFile(String html) {
		FileOutputStream fop = null;
		try {
			FileWriter fstream = new FileWriter("test.html");
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(html);
			  //Close the output stream
			  out.close();
//			File file = new File("test.html");
//			fop = new FileOutputStream(file);
//			fop.write(html.getBytes());
//			fop.flush();
//			fop.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<City> getListPoligon(){
		List<City> l = new ArrayList<City>();
		City c1 = new City(1,41.69372864778442,44.80142802000046);
		City c2 = new City(2,41.7020161,44.8000559);
		City c3 = new City(3,41.7864711,44.7831783);
		City c4 = new City(4,41.8147060475196,44.82178330421448);
		l.add(c1);
		l.add(c2);
		l.add(c3);
		l.add(c4);
		return l;
		
	}

}
