package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;


//POJO to correctly model Users
@Entity
@NamedQueries({
	@NamedQuery(name = "User.findAll", query = "select o from User o"), 
	@NamedQuery(name = "User.findByEmail", query = "select o from User o where o.email=:email"),
})
public class User {
	
	//primary key in the database
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//declare the variables 
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String location;
	private String bio;
	
	//a user has a list of badges
	
	//a user can have many pets
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private List<Pet> pets = new ArrayList<Pet>();
	
//	//a user can create many posts
//	@OneToMany
//	private List<Post> posts = new ArrayList<Post>();
//	
//	//a user can create many tasks
//	@OneToMany
//	private List<Task> tasks = new ArrayList<Task>();
//	
//	//a user can create and have many comments
//	@OneToMany
//	private List<Comment> comments = new ArrayList<Comment>();
	
	//empty constructor
	public User() {
		
	}
	
	//constructor
	public User(String firstName, String lastName, String username, String email, String password, String location, String bio) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
		this.location = location;
		this.bio = bio;
	}

	//getters and setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}
}