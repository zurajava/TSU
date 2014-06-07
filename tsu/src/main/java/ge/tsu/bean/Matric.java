package ge.tsu.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "matric")
@SessionScoped
public class Matric implements Serializable {

	private static final long serialVersionUID = 1L;
	private String html;
	private String floydHtml;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getFloydHtml() {
		return floydHtml;
	}

	public void setFloydHtml(String floydHtml) {
		this.floydHtml = floydHtml;
	}

}
