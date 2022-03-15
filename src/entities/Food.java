package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "Food.findAll", query="select o from Food o")
})
public class Food {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String type;
	private String brand;
	private String timesPerDay;
	private String quantity;
	private String extraInfo;
	
	public Food() {
		
	}

	public Food(String type, String brand, String timesPerDay, String quantity, String extraInfo) {
		this.type = type;
		this.brand = brand;
		this.timesPerDay = timesPerDay;
		this.quantity = quantity;
		this.extraInfo = extraInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getTimesPerDay() {
		return timesPerDay;
	}

	public void setTimesPerDay(String timesPerDay) {
		this.timesPerDay = timesPerDay;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
}
