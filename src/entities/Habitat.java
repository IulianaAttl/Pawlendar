package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "Habitat.findAll", query = "select o from Habitat o")
})
public class Habitat {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String typeRequired;
	private String size;
	private String bedding;
	private String temperature;
	private String timesCleaned;
	private String moreInfo;
	
	public Habitat() {
		
	}
	
	public Habitat(String typeRequired, String size, String bedding, String temperature, String timesCleaned, String moreInfo) {
		this.typeRequired = typeRequired;
		this.size = size;
		this.bedding = bedding;
		this.temperature = temperature;
		this.timesCleaned = timesCleaned;
		this.moreInfo = moreInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeRequired() {
		return typeRequired;
	}

	public void setTypeRequired(String typeRequired) {
		this.typeRequired = typeRequired;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getBedding() {
		return bedding;
	}

	public void setBedding(String bedding) {
		this.bedding = bedding;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getTimesCleaned() {
		return timesCleaned;
	}

	public void setTimesCleaned(String timesCleaned) {
		this.timesCleaned = timesCleaned;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}
}
