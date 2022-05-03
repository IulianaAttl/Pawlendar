package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import entities.Badge;
import entities.Food;
import entities.Habitat;
import entities.Pet;
import entities.Task;
import entities.User;
import entities.Vet;

//DAO class communicates with the database
public class PawlendarDAO {
	
	//connect to the persistence.xml to connect to the database
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU"); 	
	
	//empty constructor
	public PawlendarDAO() {
		
	}
	
	/**
	 * CRUD Object operations
	 */
	
	//persist - create the object in the database
	public void persistObject(Object object) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
		em.close();
	}
			
	//merge - update the object in the database
	public void mergeObject(Object object) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Object o = em.merge(object);
		em.getTransaction().commit();
		em.close();
	}
			
	//remove - delete the object from the database
	public void remove(Object object) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(em.merge(object));
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * Get a list of all items in all entities
	 */
	
	//get a list of all the users
	public List<User> getAllUsers(){
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findAll").getResultList();
		em.close();
		return users;
	}
	
	//get a list of all the badges
	public List<Badge> getAllBadges(){
		EntityManager em = emf.createEntityManager();
		List<Badge> badges = (List<Badge>) em.createNamedQuery("Badge.findAll").getResultList();
		em.close();
		return badges;
	}
	
	//get a list of all the pets
	public List<Pet> getAllPets(){
		EntityManager em = emf.createEntityManager();
		List<Pet> pets = (List<Pet>) em.createNamedQuery("Pet.findAll").getResultList();
		em.close();
		return pets;
	}
	
	//get a list of all foods
	public List<Food> getAllFoods(){
		EntityManager em = emf.createEntityManager();
		List<Food> foods = (List<Food>) em.createNamedQuery("Food.findAll").getResultList();
		em.close();
		return foods;
	}
	
	//get a list of all habitats
	public List<Habitat> getAllHaitats(){
		EntityManager em = emf.createEntityManager();
		List<Habitat> habitats = (List<Habitat>) em.createNamedQuery("Habitat.findAll").getResultList();
		em.close();
		return habitats;
	}
	
	//get a list of all vets
	public List<Vet> getAllvets(){
		EntityManager em = emf.createEntityManager();
		List<Vet> vets = (List<Vet>) em.createNamedQuery("Vet.findAll").getResultList();
		em.close();
		return vets;
	}
	
	
	//get a list of all the tasks
	public List<Task> getAllTasks(){
		EntityManager em = emf.createEntityManager();
		List<Task> tasks = (List<Task>) em.createNamedQuery("Task.findAll").getResultList();
		em.close();
		return tasks;
	}
	
	/**
	 * Methods for the user entity.
	 **/
	
	//check if the user exists using email
	public boolean checkUserExists(String email) {
		boolean found = false;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				found = true;
			}
		}
		return found;	
	}
	
	//check if email and password are correct
	public boolean getUserWithEmail(String email, String password) {
		boolean found=false;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getEmail().equals(email) && u.getPassword().equals(password)) {
				found = true;
			}
		}
		return found;
	}
	
	//get the user using email
	public User getUserByEmail(String email) {
		User user = null;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				user = u;
			}
		}
		return user;
	}

	//get the username using email
	public String getUsernameByEmail(String email) {
		String username = null;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				username = u.getUsername();
			}
		}
		return username;
	}

	//get the location by email
	public String getLocationByEmail(String email) {
		String location = null;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				location = u.getLocation();
			}
		}
		return location;
	}

	//get the bio by email
	public String getBioByEmail(String email) {
		String bio = null;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				bio = u.getBio();
			}
		}
		return bio;
	}

	//get the first name by email
	public String getFirstNameByEmail(String email) {
		String firstname = null;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				firstname = u.getFirstName();
			}
		}
		return firstname;
	}

	//get the last name by email
	public String getLastNameByEmail(String email) {
		String lastname = null;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				lastname = u.getLastName();
			}
		}
		return lastname;
	}

	//get the list of pets a user has
	public List<Pet> getPetsByEmail(String email) {
		User user = null;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				user = u;
			}
		}
		return user.getPets();
	}
	
	/**
	 * Methods for the pet entity
	 **/

	//get the pet id from the name of the pet
	public String getPetID(String selectedPet) {
		Pet pet = null;
		List<Pet> pets = getAllPets();
		for(Pet p : pets) {
			if(p.getName().equalsIgnoreCase(selectedPet)) {
				pet = p;
			}
		}
		return String.valueOf(pet.getId());
	}

	//get the pet information from the user
	public Pet getPetFromUser(String email, String petName) {
		List<User> users = getAllUsers();
		List<Pet> pets = getAllPets();
		User user = null;
		Pet pet = null;
		
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				user = u;
			}
		}
		
		for(Pet p : user.getPets()) {
			if(p.getName().equals(petName)){
				pet = p;
			}
		}
		return pet;
	}

	//get the badges using email
	public List<Badge> getBadgesByEmail(String email) {
		List<User> users = getAllUsers();
		User user = null;
		
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				user = u;
			}
		}
		
		return user.getBadges();
	}

	//get a specific badge using id
	public Badge getBadge(int i) {
		List<Badge> badges = getAllBadges();
		Badge badge = null;
		
		for(Badge b : badges) {
			if(b.getId() == i) {
				badge = b;
			}
		}
		return badge;
	}

	//check if the badges were created
	public boolean createdBadges() {
		List<Badge> existingBadges = getAllBadges();
		boolean found = false;
		for(Badge b : existingBadges) {
			if(b.getName().equals("Beginner")) {
				found = true;
			}
		}
		if(found == false) {
			return found;
		}
		else {
			return found;
		}
	}

	//get tasks by email
	public List<Task> getTasksByEmail(String email) {
		List<User> users = getAllUsers();
		User user = null;
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				user = u;
			}
		}
		return user.getLiveTasks();
	}

	//check if the username exists
	public boolean checkUsernameExists(String username) {
		boolean found = false;
		List<User> users = getAllUsers();
		for(User u : users) {
			if(u.getUsername().equals(username)) {
				found = true;
			}
		}
		return found;
	}

	//get the pet name using id
	public String getPetNameWithId(int followingPetId) {
		List<Pet> pets = getAllPets();
		Pet pet = null;
		for(Pet p : pets) {
			if(p.getId() == followingPetId) {
				pet = p;
			}
		}
		return pet.getName();
	}

	public User getUserByPet(int followingId) {
		List<User> users = getAllUsers();
		User user = null;
		for(User u : users) {
			for(Pet p : u.getPets()) {
				if(p.getId() == followingId) {
					user = u;
				}
			}
		}
		return user;
	}

}
