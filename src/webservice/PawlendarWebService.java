package webservice;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import dao.PawlendarDAO;
import entities.Pet;
import entities.User;

@Path("/")
public class PawlendarWebService{

	//declare the dao
	PawlendarDAO dao = new PawlendarDAO();

	//register the user
	@POST
  	@Path("createUser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createUser(@FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("username") String username, @FormParam("email") String email, @FormParam("password") String password, @FormParam("location") String location, @FormParam("bio") String bio) throws URISyntaxException{
		if(dao.checkUserExists(email) == true) {
			URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/error.html");
			return Response.temporaryRedirect(targetURIForRedirection).build();
  		}
		else {
			if(firstName.equals("") || lastName.equals("") || username.equals("") || email.equals("") || password.equals("")) {
				URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/error.html");
				return Response.temporaryRedirect(targetURIForRedirection).build();
			}
			else {
				if(location.equals("") && !(bio.equals(""))) {
					User user = new User(firstName, lastName, username, email, password, "n/a", bio);
				
					dao.persistObject(user);
				}
				else if(bio.equals("") && !(location.equals(""))) {
					User user = new User(firstName, lastName, username, email, password, location, "n/a");
					
					dao.persistObject(user);
				}
				else if(location.equals("") && bio.equals("")) {
					User user = new User(firstName, lastName, username, email, password, "n/a", "n/a");
				
					dao.persistObject(user);
				}
				else {
					User user = new User(firstName, lastName, username, email, password, location, bio);
				
					dao.persistObject(user);
				}
				URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/profile.html");
				return Response.seeOther(targetURIForRedirection).build();
			}
		}
	}
	
	//login user - check if user exists
  	@POST
  	@Path("loginUser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response loginUser(@FormParam("email") String email, @FormParam("password") String password) throws URISyntaxException{
  		if(dao.getUserWithEmail(email, password) == false) {
  			URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/error.html");
			return Response.temporaryRedirect(targetURIForRedirection).build();
  		}
  		else {
  			URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/profile.html");
			return Response.seeOther(targetURIForRedirection).build();
  		}
	}  	
	
  	//update user info
  	@POST
  	@Path("updateUser/{email}")
  	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  	public void updateUser(@PathParam("email") String email, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("username") String username, @FormParam("location") String location, @FormParam("bio") String bio){
  		User user = dao.getUserByEmail(email);
  		
  		if(!(firstName.equals(""))) {
  			user.setFirstName(firstName);
  		}
  		else if(!(lastName.equals(""))) {
  			user.setLastName(lastName);
  		}
  		else if(!(username.equals(""))) {
  			user.setUsername(username);
  		}
  		else if(!(location.equals(""))) {
  			user.setLocation(location);
  		}
  		else if(!(bio.equals(""))) {
  			user.setBio(bio);
  		}
  		dao.mergeObject(user);
  	}
  	
  	//get user first name
  	@GET
  	@Path("getFirstName/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFirstName(@PathParam("email") String email){
  		return dao.getFirstNameByEmail(email);
  	}
  	
  	//get user last name
  	@GET
  	@Path("getLastName/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getLastName(@PathParam("email") String email){
  		return dao.getLastNameByEmail(email);
  	}
  	
  	//get user username
  	@GET
  	@Path("getUsername/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getUsername(@PathParam("email") String email){
  		return dao.getUsernameByEmail(email);
  	}
  	
  	//get user location
  	@GET
  	@Path("getLocation/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getLocation(@PathParam("email") String email){
  		return dao.getLocationByEmail(email);
  	}
  	
  	//get user first name
  	@GET
  	@Path("getBio/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getBio(@PathParam("email") String email){
  		return dao.getBioByEmail(email);
  	}

  	//create a pet
  	@POST
  	@Path("createPet/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public Response createPet(@PathParam("email") String email, @FormParam("name") String name, @FormParam("gender") String gender, @FormParam("weight") double weight, @FormParam("species") String species, @FormParam("breed") String breed, @FormParam("colour") String colour, @FormParam("dob") String dob, @FormParam("moreinfo") String moreinfo, @FormParam("height") double height, @FormParam("age") int age) throws URISyntaxException{
  		Pet pet = new Pet(name, gender, weight, species, breed, colour, dob, moreinfo, height, age);
  		User user = dao.getUserByEmail(email);
  		List<Pet> pets = user.getPets();
  		dao.persistObject(pet);
  		pets.add(pet);
  		user.setPets(pets);
  		dao.mergeObject(user);  		
  		URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/profile.html");
		return Response.seeOther(targetURIForRedirection).build();
  	}

  	//get the pets for a user
  	@GET
  	@Path("getPets/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPets(@PathParam("email") String email){
  		String message = null;
  		List<Pet> pets = new ArrayList<Pet>();
  		pets = dao.getPetsByEmail(email);
  		for(Pet p : pets) {
  			message += "<div class=\"card\" style=\"background-color:red\">\r\n"+ p.getName() + "</div>";
  		}
  		return message;
  	}
}
