package entities;
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
//imports
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@NamedQueries({
	@NamedQuery(name = "Pet.findAll", query = "select o from Pet o")
})
public class Pet {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//documents
	@Lob
	private byte[] microchipCertificate;
	
	@Lob
	private byte[] profileImage;
	
	@Lob
	private byte[] ownerLicence;
	
	@Lob
	private byte[] healthCard;
	
	//variables
	private String name;
	private String gender;
	private String weight;
	private String species;
	private String breed;
	private String colour;
	private String dob;
	private String moreinfo;
	private String height;
	private int age;
	
	//a pet has one or more types of food
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Food> foods = new ArrayList<Food>();
	
	//a pet has one or more habitats
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Habitat> habitats = new ArrayList<Habitat>();
	
	//a pet has one vet
	@OneToOne
	Vet vet;
	
	//a pet has one or more recommendations
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Recommendation> recommendations = new ArrayList<Recommendation>();
		
	public Pet() {
		
	}

	public Pet(String name, String gender, String weight, String species, String breed, String colour, String dob, String moreinfo, String height, int age) {
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

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
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

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Food> getFoods() {
		return foods;
	}

	public void setFoods(List<Food> foods) {
		this.foods = foods;
	}

	public List<Habitat> getHabitats() {
		return habitats;
	}

	public void setHabitats(List<Habitat> habitats) {
		this.habitats = habitats;
	}

	public Vet getVet() {
		return vet;
	}

	public void setVet(Vet vet) {
		this.vet = vet;
	}

	public List<Recommendation> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<Recommendation> recommendations) {
		this.recommendations = recommendations;
	}

	public byte[] getMicrochipCertificate() {
		return microchipCertificate;
	}

	public void setMicrochipCertificate(byte[] microchipCertificate) {
		this.microchipCertificate = microchipCertificate;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	public byte[] getHealthCard() {
		return healthCard;
	}

	public void setHealthCard(byte[] healthCard) {
		this.healthCard = healthCard;
	}

	public byte[] getOwnerLicence() {
		return ownerLicence;
	}

	public void setOwnerLicence(byte[] ownerLicence) {
		this.ownerLicence = ownerLicence;
	}
	
	
}
