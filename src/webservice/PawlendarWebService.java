package webservice;

//imports
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import dao.PawlendarDAO;
import entities.Badge;
import entities.Food;
import entities.Habitat;
import entities.Pet;
import entities.Task;
import entities.User;
import entities.Vet;

//web service class that communicates with the database manager and the frontend
@Path("/")
public class PawlendarWebService{

	//declare the dao
	PawlendarDAO dao = new PawlendarDAO();

	/*
	 * User methods
	 */

	//register a user
	//if the user doesnt already exist in the database then create a new user with the information entered, save it in the database and redirect the user to their profile page
	@POST
  	@Path("createUser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createUser(@FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("username") String username, @FormParam("email") String email, @FormParam("password") String password, @FormParam("location") String location, @FormParam("bio") String bio) throws URISyntaxException{
		if(dao.checkUserExists(email) == true || dao.checkUsernameExists(username)) {
			URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/register.html?error=true");
			return Response.temporaryRedirect(targetURIForRedirection).build();
  		}
		else {
			if(dao.createdBadges() == false) {
				Badge badge1 = new Badge("Beginner","");
				dao.persistObject(badge1);
				Badge badge2 = new Badge("Advanced Beginner","");
				dao.persistObject(badge2);
				Badge badge3 = new Badge("Competent","");
				dao.persistObject(badge3);
				Badge badge4 = new Badge("Proficient","");
				dao.persistObject(badge4);
				Badge badge5 = new Badge("Expert","");
				dao.persistObject(badge5);
				Badge badge6 = new Badge("Active","");
				dao.persistObject(badge6);
			}
			User user = new User(firstName, lastName, username, email, password, location, bio, 1);
			List<Badge> badges = new ArrayList<Badge>();
			badges = user.getBadges();
			badges.add(dao.getBadge(1));
			user.setBadges(badges);
			dao.persistObject(user);
			URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/profile.html");
			return Response.seeOther(targetURIForRedirection).build();
		}
	}
	
	//login a user
	//if the user exists in the database then add to the number of times they logged in for badges and then redirect the user to their profile page
  	@POST
  	@Path("loginUser")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response loginUser(@FormParam("email") String email, @FormParam("password") String password) throws URISyntaxException{
  		if(dao.getUserWithEmail(email, password) == false) {
  			URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar?error=true");
			return Response.temporaryRedirect(targetURIForRedirection).build();
  		}
  		else {
  			User u = dao.getUserByEmail(email);
  			u.setTimesLoggedIn(u.getTimesLoggedIn() + 1);
  			dao.mergeObject(u);
  			URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/profile.html");
			return Response.seeOther(targetURIForRedirection).build();
  		}
	}  	
	
  	//update user info if any of the fields have been completed
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
  	
  	//get users in the system and display them
  	@GET
  	@Path("getUsers/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getUsers(@PathParam("email") String email){
  		List<User> users = dao.getAllUsers();
  		String result = " ";
  		for(User u : users) {
  			if(!(u.getEmail().equals(email))) {
  				result += "<div class=\"card\" style=\"width: 18rem;\"><div class=\"card-body\"><h5 class=\"card-title\">" + u.getUsername() + "</h5><p class=\"card-text\">";
  	  			
  	  			List<Badge> badges = u.getBadges();
  	  			for(Badge b : badges) {
  	  				result += "<span class=\"badge rounded-pill bg-primary\">" + b.getName() + "</span>&nbsp;";
  	  			}
  	  			
  	  			List<Pet> pets = u.getPets();
	  			for(Pet p : pets) {
	  				result += "<span class=\"badge rounded-pill bg-secondary\">" + p.getSpecies() + "</span>&nbsp;";
	  			}
  	  			
  	  			result += "<a href=\"http://localhost:8080/Pawlendar/viewUser.html?email=" + u.getEmail() + "\" class=\"btn btn-primary\">View user profile</a></div></div>&nbsp;";
  			}
  		}
  		return result;
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
  	
  	//get user bio
  	@GET
  	@Path("getBio/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getBio(@PathParam("email") String email){
  		return dao.getBioByEmail(email);
  	}
  	
  	//get user badges
  	@GET
  	@Path("getBadges/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getBadges(@PathParam("email") String email){
  		String result = "<h3>Badges</h3>";
  		List<Badge> badges = new ArrayList<Badge>();
  		badges = dao.getBadgesByEmail(email);
  		for(Badge b : badges) {
  			result += "<span class=\"badge rounded-pill bg-secondary\">" + b.getName() + "</span>&nbsp;<div class=\"vr\"></div>&nbsp;";
  		}
  		return result;
  	}
  	
  	/*
  	 * Task methods
  	 */
  	
  	//create a task for the logged in user
	@POST
  	@Path("createTask/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public Response createTask(@PathParam("email") String email, @FormParam("title") String title, @FormParam("date") String date, @FormParam("time") String time, @FormParam("content") String content, @FormParam("petList") String petList) throws URISyntaxException{
  		Pet pet = dao.getPetFromUser(email, petList);
  	  	Task task = new Task(title, date, time, content, pet, false);
  	  	User user = dao.getUserByEmail(email);
  	  	List<Task> tasks = user.getTasks();
  	  	dao.persistObject(task);
  	  	tasks.add(task);
  	  	user.setTasks(tasks);
  	  	dao.mergeObject(user);  		
  	  	URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/profile.html");
  		return Response.seeOther(targetURIForRedirection).build();	
  	}
  	
	//delete a task for the logged in user
  	@DELETE
  	@Path("removeTask/{taskId}/{email}")
  	public void removeTask(@PathParam("email") String email, @PathParam("taskId") int taskId){
  	  	User user = dao.getUserByEmail(email);
  	  	List<Task> tasks = user.getTasks();
  	  	for(Task t : tasks) {
  	  		if(t.getId() == taskId) {
  	  			dao.remove(t);
  	  		}
  	  	}
  	  	user.setTasks(tasks);
  	  	dao.mergeObject(user);  		
  	}
  	
  	//add tasks from the viewed user to the logged in user
  	@GET
  	@Path("addTaskFromUser/{taskId}/{email}/{emailLoggedIn}")
  	@Produces(MediaType.TEXT_HTML)
  	public void addTaskFromUser(@PathParam("email") String email, @PathParam("emailLoggedIn") String emailLoggedIn, @PathParam("taskId") int taskId){
  	  	User viewUser = dao.getUserByEmail(email);
  	  	User loggedUser = dao.getUserByEmail(emailLoggedIn);
  	  	List<Task> viewUserTasks = viewUser.getTasks();
  	  	List<Task> loggedUserTasks = loggedUser.getTasks();
  	  	
  	  	for(Task t : viewUserTasks) {
  	  		if(t.getId() == taskId) {
  	  			Task task = new Task(t.getTitle(), t.getDate(), t.getTime(), t.getContent(), t.getPet(), true);
  	  			dao.persistObject(task);
  	  			loggedUserTasks.add(task);
  	  		}
  	  	}
  	  	loggedUser.setTasks(loggedUserTasks);
  	  	dao.mergeObject(loggedUser);  		
  	}
  	
  	//get tasks for the logged in user
  	@GET
  	@Path("getTasks/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getTasks(@PathParam("email") String email){
  		String result = " ";
  		List<Task> tasks = new ArrayList<Task>();
  		tasks = dao.getTasksByEmail(email);
  		for(Task t : tasks) {
  			if(t.isCopied() == true) {
  				Pet pet = t.getPet();
  	  			result += "<tr bgcolor=\"green\"><td><div class=\"overflow-auto\">" + t.getTitle() + "</div></td><td>" + t.getDate() + "</td><td>" + t.getTime() + "</td><td><div class=\"overflow-auto\">" + pet.getName() + "</div></td><td><div class=\"overflow-auto\">" + t.getContent() + "</div><td><button onClick=\"removeTask(" + t.getId() + ")\" type=\"submit\" class=\"btn btn-danger\">Delete</button><button type=\"submit\" class=\"btn btn-success ms-1\">Completed</button></td></tr>";
  			}
  			else {
  				Pet pet = t.getPet();
  	  			result += "<tr><td><div class=\"overflow-auto\">" + t.getTitle() + "</div></td><td>" + t.getDate() + "</td><td>" + t.getTime() + "</td><td><div class=\"overflow-auto\">" + pet.getName() + "</div></td><td><div class=\"overflow-auto\">" + t.getContent() + "</div><td><button onClick=\"removeTask(" + t.getId() + ")\" type=\"submit\" class=\"btn btn-danger\">Delete</button><button type=\"submit\" class=\"btn btn-success ms-1\">Completed</button></td></tr>";
  			}
  		}
  		
  		return result;
  	}
  	
	//get tasks for the user the logged in user is looking at
  	@GET
  	@Path("getTasksViewUser/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getTasksViewUser(@PathParam("email") String email){
  		String result = " ";
  		List<Task> tasks = new ArrayList<Task>();
  		tasks = dao.getTasksByEmail(email);
  		for(Task t : tasks) {
  			Pet pet = t.getPet();
  			result += "<tr><td><div class=\"overflow-auto\">" + t.getTitle() + "</div></td><td>" + t.getDate() + "</td><td>" + t.getTime() + "</td><td><div class=\"overflow-auto\">" + pet.getName() + "</div></td><td><div class=\"overflow-auto\">" + t.getContent() + "</div><td><button onClick=\"addToMine(" + t.getId() + ")\" type=\"submit\" class=\"btn btn-success\">Add to my tasks</button></td></tr>";
  		}
  		
  		return result;
  	}
  	
  	//get the pets for tasks
  	@GET
  	@Path("getPetsForTask/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetsForTask(@PathParam("email") String email){
  		String result = "<option selected disabled value = \"\">Choose a pet ... </option>";
  		List<Pet> pets = new ArrayList<Pet>();
  		pets = dao.getPetsByEmail(email);
  		for(Pet p : pets) {
  			result += "<option svalue = \"" + p.getName() + "\">" + p.getName() + "</option>";
  		}
  		return result;
  	}
  	
  	/*
  	 * Pet methods
  	 */
  	
  	//create a pet
  	@POST
  	@Path("createPet/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public void createPet(@PathParam("email") String email, @FormParam("name") String name, @FormParam("gender") String gender, @FormParam("weight") String weight, @FormParam("species") String species, @FormParam("breed") String breed, @FormParam("colour") String colour, @FormParam("dob") String dob, @FormParam("moreinfo") String moreinfo, @FormParam("height") String height){
  		Pet pet = new Pet(name, gender, weight, species, breed, colour, dob, moreinfo, height, 0);
  		User user = dao.getUserByEmail(email);
  		List<Pet> pets = user.getPets();
  		boolean found = false;
  		for(Pet p : pets) {
  			if(p.getName().equals(name)) {
  				 found = true;
  			}
  		} 		
  		if(found == false) {
  			dao.persistObject(pet);
		  	pets.add(pet);
		  	user.setPets(pets);
		  	dao.mergeObject(user); 
		  	
  		}
  	}
  	
  	//create food for the pet
  	@POST
  	@Path("createFood/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public void createFood(@PathParam("email") String email, @PathParam("petName") String petName, @FormParam("type") String type, @FormParam("brand") String brand, @FormParam("timesPerDay") String timesPerDay, @FormParam("quantity") String quantity, @FormParam("moreinfo") String moreinfo){
  	 	Food food = new Food(type, brand, timesPerDay, quantity, moreinfo);
  		Pet pet = dao.getPetFromUser(email, petName);
  		List<Food> foods = pet.getFoods();
  		boolean found = false;
  		for (Food f : foods) {
  			if(f.getType().equals(type) && f.getBrand().equals(brand) && f.getExtraInfo().equals(moreinfo) && f.getQuantity().equals(quantity) && f.getTimesPerDay().equals(timesPerDay)) {
  				found = true;
  			}
  		}
  		
  		if(found == false) {
  			dao.persistObject(food);
  			foods.add(food);
  			pet.setFoods(foods);
  			dao.mergeObject(pet);
  		}
  	}
  	
  	//get a pets food
  	@POST
  	@Path("getFood/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFood(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		List<Food> foods = pet.getFoods();
  		String answer = "Foods of this pet: \n";
  		for(Food f : foods) {
  			answer = answer +  f.getType() + " " + f.getBrand() + "\n";
  		}
		return answer;
  	}
  	
  	//create a habitat for the pet
  	@POST
  	@Path("createHabitat/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public void createHabitat(@PathParam("email") String email, @PathParam("petName") String petName, @FormParam("type") String type, @FormParam("size") String size, @FormParam("bedding") String bedding, @FormParam("temperature") String temperature, @FormParam("timesCleaned") String timesCleaned, @FormParam("moreinfo") String moreinfo){
  			Habitat habitat = new Habitat(type, size, bedding, temperature, timesCleaned, moreinfo);
  			Pet pet = dao.getPetFromUser(email, petName);
  			List<Habitat> habitats = pet.getHabitats();
  			boolean found = false;
  			for (Habitat h : habitats) {
  				if(h.getBedding().equals(bedding) && h.getMoreInfo().equals(moreinfo) && h.getSize().equals(size) && h.getTemperature().equals(temperature) && h.getTimesCleaned().equals(timesCleaned) && h.getTypeRequired().equals(type)) {
  					found = true;
  				}
  			}
  			if(found == false) {
  				dao.persistObject(habitat);
  				habitats.add(habitat);
  				pet.setHabitats(habitats);
  				dao.mergeObject(pet);
  			}
  	}
  	
  	//get the pets habitat
  	@POST
  	@Path("getHabitat/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getHabitat(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		List<Habitat> habitats = pet.getHabitats();
  		String answer = "Habitats of this pet: \n";
  		for(Habitat h : habitats) {
  			answer = answer +  h.getTypeRequired() + " " + h.getBedding() + "\n";
  		}
		return answer;
  	}
  	
  	//create the vet for the pet
  	@POST
  	@Path("createVet/{email1}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public Response createVet(@PathParam("email1") String email1, @PathParam("petName") String petName, @FormParam("name") String name, @FormParam("address") String address, @FormParam("phoneNumber") String phoneNumber, @FormParam("email") String email, @FormParam("webUrl") String webUrl) throws URISyntaxException{
  		Vet vet = new Vet(name, address, phoneNumber, email, webUrl);
  		Pet pet = dao.getPetFromUser(email1, petName);
  		dao.persistObject(vet);
  		pet.setVet(vet);
  		dao.mergeObject(pet);
  		URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/profile.html");
		return Response.seeOther(targetURIForRedirection).build();
  	}

  	//get the pets for a user
  	@GET
  	@Path("getPets/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPets(@PathParam("email") String email){
  		String result = "<h4>My Pets</h4><br>";
  		List<Pet> pets = new ArrayList<Pet>();
  		pets = dao.getPetsByEmail(email);
  		int i = 0;
  		for(Pet p : pets) {
  			result += "<div class=\"card\" style=\"width: 18rem;\">\r\n"
  					+ "  <div class=\"card-body\">\r\n"
  					+ "    <h5 id=\"cardTitle\" class=\"card-title\">" + p.getName() + "</h5>\r\n"
  					+ "    <p class=\"card-text\"><span class=\"badge rounded-pill bg-secondary\">" + p.getSpecies() + "</span>&nbsp;<span class=\"badge rounded-pill bg-secondary\">" + p.getBreed() + "</span></p>\r\n"
  					+ "    <a href=\"http://localhost:8080/Pawlendar/viewPet.html?petName=" + p.getName() + "\" class=\"btn btn-primary\">View pet</a>\r\n"
  					+ "  </div>\r\n"
  					+ "</div>&nbsp;";
  			i = i + 1;
  		}
  		return result;
  	}
  	
  	@GET
  	@Path("getPetInformation/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetInformation(@PathParam("email") String email, @PathParam("petName") String petName) {
  		Pet pet = dao.getPetFromUser(email, petName);
  		String result = "Pet Information\n";
  		result = result + "Name: " + pet.getName() + "\nGender: " + pet.getGender() + "\nWeight: " + pet.getWeight() + "\nSpecies: " + pet.getSpecies() + "\nBreed: " + pet.getBreed() + "\nColour: " + pet.getColour() + "\nDOB: " + pet.getDob() + "\nHeight: " + pet.getHeight() + "\nMore Info: " + pet.getMoreinfo();
  		return result;
  	}
  	
  	@GET
  	@Path("getFoodInformation/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFoodInformation(@PathParam("email") String email, @PathParam("petName") String petName) {
  		Pet pet = dao.getPetFromUser(email, petName);
  		String result = "Food Information\n";
  		List<Food> foods = pet.getFoods();
  		for(Food f : foods) {
  			result = result + "Type: " + f.getType() + "\nBrand: " + f.getBrand() + "\nTimes per Day: " + f.getTimesPerDay() + "\nQuantity: " + f.getQuantity() + "\nMore information: " + f.getExtraInfo();
  		}
  		return result;
  	}
  	
  	@GET
  	@Path("getHabitatInformation/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getHabitatInformation(@PathParam("email") String email, @PathParam("petName") String petName) {
  		Pet pet = dao.getPetFromUser(email, petName);
  		String result = "Habitat Information\n";
  		List<Habitat> habitats = pet.getHabitats();
  		for(Habitat h : habitats) {
  			result = result + "Type: " + h.getTypeRequired() + "\nSize: " + h.getSize() + "\nBedding: " + h.getBedding() + "\nTemperature: " + h.getTemperature() + "\nTimes Cleaned: " + h.getTimesCleaned() + "\nMore info: " + h.getMoreInfo();
  		}
  		return result;
  	}
  	
  	@GET
  	@Path("getVetInformation/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getVetInformation(@PathParam("email") String email, @PathParam("petName") String petName) {
  		Pet pet = dao.getPetFromUser(email, petName);
  		String result = "Vet information\n";
  		Vet vet = pet.getVet();
  		result = result + "Name: " + vet.getName() + "\nAddress: " + vet.getAddress() + "\nPhone number: " + vet.getPhoneNumber() + "\nEmail : " + vet.getEmail() + "\nWeb URL: " + vet.getWebUrl();
  		return result;
  	}
  	
  	@GET
  	@Path("getDocumentsInformation/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getDocumentsInformation(@PathParam("email") String email, @PathParam("petName") String petName) {
  		//none yet
  		return "not yet";
  	}
  	
  	@GET
  	@Path("getRecommendationsInformation/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getRecommendationsInformation(@PathParam("email") String email, @PathParam("petName") String petName) {
  		//none yet
  		return "not yet";
  	}
}
