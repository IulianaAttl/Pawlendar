package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "Pet.findAll", query = "select o from Pet o"),
})
public class Pet {

	//about - name, gender, petID, weight, species, breed, colour, dob, moreinfo, height, age
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
		
	private String name;
	private String gender;
	private double weight;
	private String species;
	private String breed;
	private String colour;
	private String dob;
	private String moreinfo;
	private double height;
	private int age;
		
	public Pet() {
		
	}

	public Pet(String name, String gender, double weight, String species, String breed, String colour, String dob, String moreinfo, double height, int age) {
		this.name = name;
		this.gender = gender;
		this.weight = weight;
		this.species = species;
		this.breed = breed;
		this.colour = colour;
		this.dob = dob;
		this.moreinfo = moreinfo;
		this.height = height;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMoreinfo() {
		return moreinfo;
	}

	public void setMoreinfo(String moreinfo) {
		this.moreinfo = moreinfo;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	//vet - name, address, phone number, email
	//food - type, quantity, timesperday, moreinfo
	//habitat - temperature, bedding, moreinfo, cleaning schedule
	//recommendations
	//extra notes
}
