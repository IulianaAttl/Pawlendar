package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import entities.Pet;
import entities.User;

public class PawlendarDAO {
	
	//connect to the persistence.xml to connect to the database
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPU"); 	
	
	//empty constructor
	public PawlendarDAO() {
		
	}
	
	//persist - create it in the database
	public void persistObject(Object object) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
		em.close();
	}
			
	//merge - update it in the database
	public void mergeObject(Object object) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Object o = em.merge(object);
		em.getTransaction().commit();
		em.close();
	}
			
	//remove - delete it from the database
	public void remove(Object object) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(em.merge(object));
		em.getTransaction().commit();
		em.close();
	}
	
	//check if the user exists using email
	public boolean checkUserExists(String email) {
		boolean found = false;
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
		em.close();
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
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
		em.close();
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
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
		em.close();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				user = u;
			}
		}
		return user;
	}

	//get the username by email
	public String getUsernameByEmail(String email) {
		String username = null;
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
		em.close();
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
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
		em.close();
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
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
		em.close();
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
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
		em.close();
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
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
		em.close();
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
		EntityManager em = emf.createEntityManager();
		List<User> users = (List<User>) em.createNamedQuery("User.findByEmail").setParameter("email", email).getResultList();
		em.close();
		for(User u : users) {
			if(u.getEmail().equals(email)) {
				user = u;
			}
		}
		return user.getPets();
	}
}
