package webservice;

//imports
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
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
  			if(u.getTimesLoggedIn() == 30) {
  				if(!(u.getBadges().contains(dao.getBadge(6)))) {
  					u.getBadges().add(dao.getBadge(6));
  				}
  			}
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
  		if(!(lastName.equals(""))) {
  			user.setLastName(lastName);
  		}
  		if(!(username.equals(""))) {
  			user.setUsername(username);
  		}
  		if(!(location.equals(""))) {
  			user.setLocation(location);
  		}
  		if(!(bio.equals(""))) {
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
  	  			
  	  			result += "<br><br><a href=\"http://localhost:8080/Pawlendar/viewUser.html?email=" + u.getEmail() + "\" class=\"btn btn-primary\" onClick=\"localStorage.setItem(\"emailOfUser\"," + u.getEmail() + ");\">View user profile</a></div></div>&nbsp;";
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
  			result += "<span class=\"badge rounded-pill bg-secondary\">" + b.getName() + "</span>&nbsp;&nbsp;";
  		}
  		return result;
  	}
  	
  	//get the pets for my list in following another pet
  	@GET
  	@Path("getPetsForMyList/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetsForMyList(@PathParam("email") String email){
  		String result = "<option selected disabled value = \"\">Choose a pet ... </option>";
  		List<Pet> pets = new ArrayList<Pet>();
  		pets = dao.getPetsByEmail(email);
  		for(Pet p : pets) {
  			result += "<option svalue = \"" + p.getName() + "\">" + p.getName() + "</option>";
  		}
  		return result;
  	}
  	
  	//create a task for the logged in user
	@POST
  	@Path("createTask/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public Response createTask(@PathParam("email") String email, @FormParam("title") String title, @FormParam("day") int day, @FormParam("month") int month, @FormParam("year") int year, @FormParam("time") String time, @FormParam("content") String content, @FormParam("petList") String petList, @FormParam("repeat") String repeat) throws URISyntaxException{
  		Pet pet = dao.getPetFromUser(email, petList);
  	  	User user = dao.getUserByEmail(email);
  	  	List<Task> tasks = user.getLiveTasks();
  	  	List<Task> toAdd = new ArrayList<Task>();
  	  	int dayChosen = day;
		int monthChosen = month;
		int yearChosen = year;
  	  	if(repeat.equals("everyDay")) {
  	  		for(int i = 0; i < 365; i++) {
  	  			if(dayChosen == 29 && monthChosen == 2) {
  	  				String date = yearChosen + "-" + monthChosen + "-" + dayChosen;
  	  				Task task = new Task(title, date, time, content, pet, false);
  	  				dao.persistObject(task);
  	  				toAdd.add(task);
  	  				tasks.add(task);
  	  				dayChosen = 1;
	  				monthChosen = monthChosen + 1;
  	  			}
  	  			else if(dayChosen == 30 && (monthChosen == 9 || monthChosen == 4 || monthChosen == 6 || monthChosen == 11)) {
  	  			String date = yearChosen + "-" + monthChosen + "-" + dayChosen;
	  				Task task = new Task(title, date, time, content, pet, false);
	  	  			dao.persistObject(task);
	  	  			toAdd.add(task);
	  	  			tasks.add(task);
	  	  			dayChosen = 1;
	  				monthChosen = monthChosen + 1;
  	  			}
  	  			else if(dayChosen == 31 && (monthChosen == 1 || monthChosen == 3 || monthChosen == 5 || monthChosen == 7 || monthChosen == 8 || monthChosen == 10)) {
  	  				
  	  			String date = yearChosen + "-" + monthChosen + "-" + dayChosen;
	  				Task task = new Task(title, date, time, content, pet, false);
	  	  			dao.persistObject(task);
	  	  			toAdd.add(task);
	  	  			tasks.add(task);
	  	  			dayChosen = 1;
	  				monthChosen = monthChosen + 1;
  	  			}
  	  			else if(dayChosen == 31 && monthChosen == 12) {
  	  				
  	  			String date = yearChosen + "-" + monthChosen + "-" + dayChosen;
	  				Task task = new Task(title, date, time, content, pet, false);
	  	  			dao.persistObject(task);
	  	  			toAdd.add(task);
	  	  			tasks.add(task);
	  	  			dayChosen = 1;
	  				monthChosen = 1;
	  				yearChosen = yearChosen + 1;
  	  			}
  	  			else {
  	  				
  	  			String date = yearChosen + "-" + monthChosen + "-" + dayChosen;
	  				Task task = new Task(title, date, time, content, pet, false);
	  	  			dao.persistObject(task);
	  	  			toAdd.add(task);
	  	  			tasks.add(task);
	  	  			dayChosen = dayChosen + 1;
  	  			}
  	  		}
  	  	}
  	  	else if(repeat.equals("everyMonth")) {
	  		for(int i = 0; i < 12; i++) {
	  			if(monthChosen == 13) {
	  				monthChosen = 1;
	  				yearChosen = yearChosen + 1;
	  			}
	  			String date = yearChosen + "-" + monthChosen + "-" + dayChosen;
	  			Task task = new Task(title, date, time, content, pet, false);
	  			dao.persistObject(task);
	  			toAdd.add(task);
	  			tasks.add(task);
	  			monthChosen = monthChosen + 1;
	  		}
  	  	}
  	  	else if(repeat.equals("everyYear")) {
	  	  	for(int i = 0; i < 5; i++) {
	  			
	  	  	String date = yearChosen + "-" + monthChosen + "-" + dayChosen;
				Task task = new Task(title, date, time, content, pet, false);
	  			dao.persistObject(task);
	  			toAdd.add(task);
	  			tasks.add(task);
	  			yearChosen = yearChosen + 1;
	  		}
  	  	}
  	  	else if(repeat.equals("never")) {
  	  	String date = yearChosen + "-" + monthChosen + "-" + dayChosen;
			Task task = new Task(title, date, time, content, pet, false);
  	  		dao.persistObject(task);
  	  		toAdd.add(task);
  	  		tasks.add(task);
  	  	}
  	  	
  	  	user.setLiveTasks(tasks);
  	  	dao.mergeObject(user);  
  	  	
  	  	List<Pet> followers = pet.getFollowersPets();
      
  	  	for(Pet p : followers) {
	  	  	for(User u : dao.getAllUsers()) {
	  	  		for(Pet pe : u.getPets()) {
	  	  			if(pe == p) {
	  	  			List<Task> userTasks = u.getLiveTasks();
		  	  			for(Task t : toAdd) {
		  		    		Task newTask = new Task();
		  		    		newTask.setContent(t.getContent());
		  		    		newTask.setCopied(true);
		  		    		newTask.setDate(t.getDate());
		  		    		newTask.setPet(pe);
		  		    		newTask.setTime(t.getTime());
		  		    		newTask.setTitle(t.getTitle());
		  		    		dao.persistObject(newTask);
		  		    		userTasks.add(newTask);
		  		    		dao.mergeObject(u);
		  		    	}
	  	  			}
	  	  		}
	  	  	}
  	  	}
  	  		
  	  	URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/profile.html");
  		return Response.seeOther(targetURIForRedirection).build();	
  	}
  	
	//delete a task for the logged in user
  	@DELETE
  	@Path("removeTask/{taskId}/{email}")
  	public void removeTask(@PathParam("email") String email, @PathParam("taskId") int taskId){
  	  	User user = dao.getUserByEmail(email);
  	  	List<Task> tasks = user.getLiveTasks();
  	  	for(Task t : tasks) {
  	  		if(t.getId() == taskId) {
  	  			dao.remove(t);
  	  			tasks.remove(t);
  	  		}
  	  	}		
  	  	if(!(user.getBadges().contains(dao.getBadge(3)))) {
			user.getBadges().add(dao.getBadge(3));
		}
  	  	user.setLiveTasks(tasks);
  	  	dao.mergeObject(user);
  	}
  	
  	//get tasks for the logged in user
  	@GET
  	@Path("getTasks/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getTasks(@PathParam("email") String email){
  		String result = " ";
  		List<Task> tasks = dao.getTasksByEmail(email);
  		Collections.sort(tasks, new sortTasks());
  		for(Task t : tasks) {
  			Pet pet = t.getPet();
  	  		result += "<tr><td><div class=\"overflow-auto\">" + t.getTitle() + "</div></td><td>" + t.getDate() + "</td><td>" + t.getTime() + "</td><td><div class=\"overflow-auto\">" + pet.getName() + "</div></td><td><div class=\"overflow-auto\">" + t.getContent() + "</div><td><button onClick=\"removeTask(" + t.getId() + ")\" type=\"submit\" class=\"btn btn-danger\">Delete</button></td></tr>";	
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
  			result += "<tr><td><div class=\"overflow-auto\">" + t.getTitle() + "</div></td><td>" + t.getDate() + "</td><td>" + t.getTime() + "</td><td><div class=\"overflow-auto\">" + pet.getName() + "</div></td><td><div class=\"overflow-auto\">" + t.getContent() + "</div><td></td></tr>";
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
  	
  	//create a pet
  	@POST
  	@Path("createPet/{email1}")
  	@Produces(MediaType.TEXT_HTML)
  	public Response createPet(@PathParam("email1") String email1, @FormParam("petName") String petName, @FormParam("gender") String gender, @FormParam("weight") String weight, @FormParam("species") String species, @FormParam("breed") String breed, @FormParam("colour") String colour, @FormParam("dob") String dob, @FormParam("moreinfoPet") String moreinfoPet, @FormParam("height") String height, @FormParam("type") String type, @FormParam("brand") String brand, @FormParam("timesPerDay") String timesPerDay, @FormParam("quantity") String quantity, @FormParam("moreinfoFood") String moreinfoFood, @FormParam("habitatType") String habitatType, @FormParam("size") String size, @FormParam("bedding") String bedding, @FormParam("temperature") String temperature, @FormParam("timesCleaned") String timesCleaned, @FormParam("moreinfo") String moreinfo, @FormParam("name") String name, @FormParam("address") String address, @FormParam("phoneNumber") String phoneNumber, @FormParam("email") String email, @FormParam("webUrl") String webUrl) throws URISyntaxException{
  		Pet pet = new Pet(petName, gender, weight, species, breed, colour, dob, moreinfoPet, height, 0);
  		User user = dao.getUserByEmail(email1);
  		List<Pet> pets = user.getPets();
  		boolean found = false;
  		for(Pet p : pets) {
  			if(p.getName().equals(petName)) {
  				 found = true;
  			}
  		} 		
  		if(found == false) {
		  	if(!(type.equals("") && brand.equals("") && timesPerDay.equals("") && quantity.equals("") && moreinfoFood.equals(""))) {
		  		Food food = new Food(type, brand, timesPerDay, quantity, moreinfoFood);
		  		dao.persistObject(food);
		  		pet.setFood(food);
		  	}
		  	
		  	if(!(habitatType.equals("") && size .equals("") && bedding.equals("") && temperature.equals("") && timesCleaned.equals("") && moreinfo.equals(""))) {
		  		Habitat habitat = new Habitat(habitatType, size, bedding, temperature, timesCleaned, moreinfo);
	  			dao.persistObject(habitat);
	  			pet.setHabitat(habitat);
		  	}
  			
		  	if(!(name.equals("") && address.equals("") && phoneNumber.equals("") && email.equals("") && webUrl.equals(""))) {
		  		Vet vet = new Vet(name, address, phoneNumber, email, webUrl);
	  	  		dao.persistObject(vet);
	  	  		pet.setVet(vet);
		  	}
		  	
		  	if(!(user.getBadges().contains(dao.getBadge(2)))) {
				user.getBadges().add(dao.getBadge(2));
			}
		  	
		  	dao.persistObject(pet);
		  	pets.add(pet);
		  	user.setPets(pets);
		  	dao.mergeObject(user);
  			
		  	URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/addPet.html?error=false");
			return Response.temporaryRedirect(targetURIForRedirection).build();
		  	
  		}
  		else {
  			URI targetURIForRedirection = new URI("http://localhost:8080/Pawlendar/addPet.html?error=true");
			return Response.temporaryRedirect(targetURIForRedirection).build();
  		}
  	}
  	 	
  	//get the pets for a user
  	@GET
  	@Path("getPets/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPets(@PathParam("email") String email){
  		String result = "<h4>My Pets</h4><br>";
  		List<Pet> pets = new ArrayList<Pet>();
  		pets = dao.getPetsByEmail(email);
  		for(Pet p : pets) {
  			result += "<div class=\"card\" style=\"width: 23rem;\">\r\n"
  					+ "  <div class=\"card-body\">\r\n"
  					+ "    <h5 id=\"cardTitle\" class=\"card-title\">" + p.getName() + "</h5>\r\n"
  					+ "    <p class=\"card-text\"><span class=\"badge rounded-pill bg-secondary\">" + p.getSpecies() + "</span>&nbsp;<span class=\"badge rounded-pill bg-secondary\">" + p.getBreed() + "</span></p>\r\n"
  					+ "    <a href=\"http://localhost:8080/Pawlendar/viewPet.html?petName=" + p.getName() + "\" class=\"btn btn-primary\">View pet</a>\r\n "
  					+ "    <button onClick=\"removePet(" + p.getId() + ")\" type=\"submit\" class=\"btn btn-danger\">Delete pet</button>"
  					+ "	   <a href=\"http://localhost:8080/Pawlendar/findusers.html?findExpertSpecies=" + p.getSpecies() +"\" class=\"btn btn-secondary\">Find an expert</a>\r\n"					
  					+ "  </div>\r\n"
  					+ "</div>&nbsp;";
  			
  		}
  		return result;
  	}
  	
  	//get the followers for a pet
  	@GET
  	@Path("getFollowers/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFollowers(@PathParam("email") String email){
  		String result = "<h4>My Followers</h4><br>";
  		User user = dao.getUserByEmail(email);
  		List<Pet> pets = user.getPets();
  		List<Pet> followers = new ArrayList<Pet>();
  		int number = 0;
  		for(Pet p : pets) {
  			for(Pet u : p.getFollowersPets()) {
  				followers.add(u);
  				number = number + 1;
  			}
  		}
  		
  		if(number >= 10) {
  			if(!(user.getBadges().contains(dao.getBadge(5)))) {
					user.getBadges().add(dao.getBadge(5));
			}
  		}
  		
  		for(Pet u : followers) {
  			result += "<div class=\"card\" style=\"width: 23rem;\">\r\n"
  					+ "  <div class=\"card-body\">\r\n"
  					+ "    <h5 id=\"cardTitle\" class=\"card-title\">" + u.getName()+ "</h5>\r\n"	
  					+ "    <a href=\"http://localhost:8080/Pawlendar/viewOtherPet.html?email=" + dao.getUserByPet(u.getId()).getEmail() + "&petName=" + u.getName() + "\" class=\"btn btn-primary\">View pet</a>\r\n "
  					+ "  </div>\r\n"
  					+ "</div>&nbsp;";
  		}
  		
  		return result;
  	}
  	
  	//get the pet they are following
  	@GET
  	@Path("getFollowing/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFollowing(@PathParam("email") String email){
  		String result = "<h4>Who I am following</h4><br>";
  		User user = dao.getUserByEmail(email);
  		List<Pet> pets = user.getPets();
  		
  		for(Pet p : pets) {
  			int followingId = p.getFollowingPetId();
  			for(Pet pe : dao.getAllPets()) {
  				if(pe.getId() == followingId) {
  	  				result += "<div class=\"card\" style=\"width: 23rem;\">\r\n"
  	  	  					+ "  <div class=\"card-body\">\r\n"
  	  	  					+ "    <h5 id=\"cardTitle\" class=\"card-title\">" + pe.getName() + "</h5>\r\n"	
  	  	  					+ "    <a href=\"http://localhost:8080/Pawlendar/viewOtherPet.html?email=" + dao.getUserByPet(followingId).getEmail() + "&petName=" + pe.getName() + "\" class=\"btn btn-primary\">View pet my pet follows</a>\r\n "
  	  	  					+ "    <button onClick=\"unfollowPet(" + pe.getId() + ")\" type=\"submit\" class=\"btn btn-danger\">Unfollow pet</button>"
  	  	  					+ "  </div>\r\n"
  	  	  					+ "</div>&nbsp;";
  	  			}
  			}
  		}
  		return result;
  	}
  	
  //get the pet they are following
  	@GET
  	@Path("getFollowingView/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFollowingView(@PathParam("email") String email){
  		String result = "<h4>Who I am following</h4><br>";
  		User user = dao.getUserByEmail(email);
  		List<Pet> pets = user.getPets();
  		
  		for(Pet p : pets) {
  			int followingId = p.getFollowingPetId();
  			for(Pet pe : dao.getAllPets()) {
  				if(pe.getId() == followingId) {
  	  				result += "<div class=\"card\" style=\"width: 23rem;\">\r\n"
  	  	  					+ "  <div class=\"card-body\">\r\n"
  	  	  					+ "    <h5 id=\"cardTitle\" class=\"card-title\">" + pe.getName() + "</h5>\r\n"	
  	  	  					+ "    <a href=\"http://localhost:8080/Pawlendar/viewOtherPet.html?email=" + dao.getUserByPet(followingId).getEmail() + "&petName=" + pe.getName() + "\" class=\"btn btn-primary\">View pet my pet follows</a>\r\n "
  	  	  					+ "  </div>\r\n"
  	  	  					+ "</div>&nbsp;";
  	  			}
  			}
  		}
  		return result;
  	}
  	
  	//get the pets for a user to view - find a user
  	@GET
  	@Path("getPetsToView/{email}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetsToView(@PathParam("email") String email){
  		String result = "<h4>My Pets</h4><br>";
  		List<Pet> pets = new ArrayList<Pet>();
  		pets = dao.getPetsByEmail(email);
  		for(Pet p : pets) {
  			result += "<div class=\"card\" style=\"width: 23rem;\">\r\n"
  					+ "  <div class=\"card-body\">\r\n"
  					+ "    <h5 id=\"cardTitle\" class=\"card-title\">" + p.getName() + "</h5>\r\n"
  					+ "    <p class=\"card-text\"><span class=\"badge rounded-pill bg-secondary\">" + p.getSpecies() + "</span>&nbsp;<span class=\"badge rounded-pill bg-secondary\">" + p.getBreed() + "</span></p>\r\n"
  					+ "    <a href=\"http://localhost:8080/Pawlendar/viewOtherPet.html?email=" + email + "&petName=" + p.getName() + "\" class=\"btn btn-primary\">View pet</a>\r\n "			
  					+ "  </div>\r\n"
  					+ "</div>&nbsp;";
  		}
  		return result;
  	}
  	
  	//get users in the system that are experts and have the same species and display them
  	@GET
  	@Path("getUsersWithSpecies/{email}/{species}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getUsersWithSpecies(@PathParam("species") String species, @PathParam("email") String email){
  		List<User> users = dao.getAllUsers();
  		String result = " ";
  		for(User u : users) {
  			for(Badge b : u.getBadges()) {
  				if(b.getName().equals("Expert")) {
  					for(Pet p : u.getPets()) {
  						if(p.getSpecies().equals(species) && !(u.getEmail().equals(email))) {
	  						result += "<div class=\"card\" style=\"width: 18rem;\"><div class=\"card-body\"><h5 class=\"card-title\">" + u.getUsername() + "</h5><p class=\"card-text\">";
	  		  	  			List<Badge> badges = u.getBadges();
	  		  	  			for(Badge a : badges) {
	  		  	  				result += "<span class=\"badge rounded-pill bg-primary\">" + a.getName() + "</span>&nbsp;";
	  		  	  			}
	  		  	  			List<Pet> pets = u.getPets();
	  			  			for(Pet e : pets) {
	  			  				result += "<span class=\"badge rounded-pill bg-secondary\">" + e.getSpecies() + "</span>&nbsp;";
	  			  			}
	  		  	  			result += "<br><br><a href=\"http://localhost:8080/Pawlendar/viewUser.html?email=" + u.getEmail() + "\" class=\"btn btn-primary\">View user profile</a></div></div>&nbsp;";
	  					}
	  				}
	  			}
  			}
  		}
  		return result;
  	}
  	
  	//follow a pet
  	@POST
    @Path("followPet/{emailLoggedIn}/{emailViewUser}")
    @Produces(MediaType.TEXT_HTML)
    public void followPet(@PathParam("emailLoggedIn") String emailLoggedIn,@PathParam("emailViewUser") String emailViewUser, @FormParam("theirPetList") String theirPetList, @FormParam("myPetList") String myPetList) throws URISyntaxException{
    	Pet myPet = dao.getPetFromUser(emailLoggedIn, myPetList);
    	Pet theirPet = dao.getPetFromUser(emailViewUser, theirPetList);
      
      	if(myPet.getFollowingPetId() != theirPet.getId()) {
      		myPet.setFollowingPetId(theirPet.getId());
          	dao.mergeObject(myPet);
          	List<Pet> followers = theirPet.getFollowersPets();
      		followers.add(myPet);
          	theirPet.setFollowersPets(followers);
          	dao.mergeObject(theirPet);
          	List<Task> myTasks = dao.getUserByEmail(emailLoggedIn).getLiveTasks();
      		List<Task> theirTasks = dao.getUserByEmail(emailViewUser).getLiveTasks();
      		
      		for(Task t : myTasks) {
      			if(t.isCopied()) {
      				myTasks.remove(t);
      				dao.remove(t);
      			}
      		}
      		
      		for(Task t : theirTasks) {
      			Task newTask = new Task();
      			newTask.setContent(t.getContent());
      			newTask.setCopied(true);
      			newTask.setDate(t.getDate());
      			newTask.setPet(myPet);
      			newTask.setTime(t.getTime());
      			newTask.setTitle(t.getTitle());
      			dao.persistObject(newTask);
      			myTasks.add(newTask);
      		}
      		User me = dao.getUserByEmail(emailLoggedIn);
      		me.setLiveTasks(myTasks);
  			dao.mergeObject(me);
      	}	
    }	
  	
  	//delete a pet for the logged in user
  	@DELETE
  	@Path("removePet/{petId}/{email}")
  	public void removePet(@PathParam("email") String email, @PathParam("petId") int petId){
  	  	User user = dao.getUserByEmail(email);
  	  	List<Pet> pets = user.getPets();
  	  	for(Pet p : pets) {
  	  		if(p.getId() == petId) {
  	  			dao.remove(p);
  	  		}
  	  	}	
  	}
  	
  	//delete a pet for the logged in user
  	@DELETE
  	@Path("unfollowPet/{petId}/{email}")
  	public void unfollowPet(@PathParam("email") String email, @PathParam("petId") int petId){
  	  	User user = dao.getUserByEmail(email);
  	  	List<Pet> pets = user.getPets();
  	  	for(Pet p : pets) {
  	  		if(p.getFollowingPetId() == petId) {
  	  			p.setFollowingPetId(0);
  	  			dao.mergeObject(p);
  	  		}
  	  	}	
  	}
  	
  	//update pet info if any of the fields have been completed
  	@POST
  	@Path("updatePetInfo/{useremail}/{petName}")
  	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  	public void updatePetInfo(@PathParam("useremail") String useremail, @PathParam("petName") String petName, @FormParam("name") String name, @FormParam("gender") String gender, @FormParam("weight") String weight, @FormParam("species") String species, @FormParam("breed") String breed, @FormParam("colour") String colour, @FormParam("dob") String dob, @FormParam("petmoreinfo") String petmoreinfo, @FormParam("height") String height, @FormParam("foodtype") String foodtype, @FormParam("brand") String brand, @FormParam("timesPerDay") String timesPerDay, @FormParam("quantity") String quantity, @FormParam("foodmoreinfo") String foodmoreinfo, @FormParam("habitattype") String habitattype, @FormParam("size") String size, @FormParam("bedding") String bedding, @FormParam("temperature") String temperature, @FormParam("timesCleaned") String timesCleaned, @FormParam("habitatmoreinfo") String habitatmoreinfo, @FormParam("vetname") String vetname, @FormParam("address") String address, @FormParam("phoneNumber") String phoneNumber, @FormParam("vetemail") String vetemail, @FormParam("webUrl") String webUrl){
  		User u = dao.getUserByEmail(useremail);
  		Pet pet = dao.getPetFromUser(useremail, petName);
  		Food food = pet.getFood();
  		Habitat habitat = pet.getHabitat();
  		Vet vet = pet.getVet();
  		
  		if(!(gender.equals("") && weight.equals("") && species.equals("") && breed.equals("") && colour.equals("") && dob.equals(""))) {
	  		if(!(gender.equals(""))) {
	  			pet.setGender(gender);
	  		}
	  		if(!(weight.equals(""))) {
	  			pet.setWeight(weight);
	  		}
	  		if(!(species.equals(""))) {
	  			pet.setSpecies(species);
	  		}
	  		if(!(breed.equals(""))) {
	  			pet.setBreed(breed);
	  		}
	  		if(!(colour.equals(""))) {
	  			pet.setColour(colour);
	  		}
	  		if(!(dob.equals(""))) {
	  			pet.setDob(dob);
	  		}
	  		if(!(petmoreinfo.equals(""))) {
	  			pet.setMoreinfo(petmoreinfo);
	  		}
	  		if(!(height.equals(""))) {
	  			pet.setHeight(height);
	  		}
	  		dao.mergeObject(pet);
  		}
  		
		if(!(foodtype.equals("") && brand.equals("") && timesPerDay.equals("") && quantity.equals("") && foodmoreinfo.equals(""))) {
			if(food == null) {
				Food newFood = new Food();
				if(!(foodtype.equals(""))) {
					newFood.setType(foodtype);
				}
				if(!(brand.equals(""))) {
					newFood.setBrand(brand);
				}
				if(!(timesPerDay.equals(""))) {
					newFood.setTimesPerDay(timesPerDay);
				}
				if(!(quantity.equals(""))) {
					newFood.setQuantity(quantity);
				}
				if(!(foodmoreinfo.equals(""))) {
					newFood.setExtraInfo(foodmoreinfo);
				}
				dao.persistObject(newFood);
				pet.setFood(newFood);
				dao.mergeObject(pet);
			}
			else {
				if(!(foodtype.equals(""))) {
					food.setType(foodtype);
				}
				if(!(brand.equals(""))) {
					food.setBrand(brand);
				}
				if(!(timesPerDay.equals(""))) {
					food.setTimesPerDay(timesPerDay);
				}
				if(!(quantity.equals(""))) {
					food.setQuantity(quantity);
				}
				if(!(foodmoreinfo.equals(""))) {
					food.setExtraInfo(foodmoreinfo);
				}
				dao.mergeObject(food);
			}
		}
		
  		if(!(habitattype.equals("") && size.equals("") && bedding.equals("") && temperature.equals("") && timesCleaned.equals("") && habitatmoreinfo.equals(""))) {
	  		if(habitat == null) {
	  			Habitat newHabitat = new Habitat();
	  			if(!(habitattype.equals(""))) {
	  				newHabitat.setTypeRequired(habitattype);
		  		}
		  		if(!(size.equals(""))) {
		  			newHabitat.setSize(size);
		  		}
		  		if(!(bedding.equals(""))) {
		  			newHabitat.setBedding(bedding);
		  		}
		  		if(!(temperature.equals(""))) {
		  			newHabitat.setTemperature(temperature);
		  		}
		  		if(!(timesCleaned.equals(""))) {
		  			newHabitat.setTimesCleaned(timesCleaned);
		  		}
		  		if(!(habitatmoreinfo.equals(""))) {
		  			newHabitat.setMoreInfo(habitatmoreinfo);
		  		}
		  		dao.persistObject(newHabitat);
		  		pet.setHabitat(newHabitat);
		  		dao.mergeObject(pet);
	  		}
	  		else {
	  			if(!(habitattype.equals(""))) {
		  			habitat.setTypeRequired(habitattype);
		  		}
		  		if(!(size.equals(""))) {
		  			habitat.setSize(size);
		  		}
		  		if(!(bedding.equals(""))) {
		  			habitat.setBedding(bedding);
		  		}
		  		if(!(temperature.equals(""))) {
		  			habitat.setTemperature(temperature);
		  		}
		  		if(!(timesCleaned.equals(""))) {
		  			habitat.setTimesCleaned(timesCleaned);
		  		}
		  		if(!(habitatmoreinfo.equals(""))) {
		  			habitat.setMoreInfo(habitatmoreinfo);
		  		}
		  		dao.mergeObject(habitat);
	  		}
  		}
  		
  		if(!(vetname.equals("") && address.equals("") && phoneNumber.equals("") && vetemail.equals("") && webUrl.equals(""))) {
  			if(vet == null) {
  				Vet newVet = new Vet();
  				if(!(vetname.equals(""))) {
  					newVet.setName(vetname);
		  		}
		  		if(!(address.equals(""))) {
		  			newVet.setAddress(address);
		  		}
		  		if(!(phoneNumber.equals(""))) {
		  			newVet.setPhoneNumber(phoneNumber);
		  		}
		  		if(!(vetemail.equals(""))) {
		  			newVet.setEmail(vetemail);
		  		}
		  		if(!(webUrl.equals(""))) {
		  			newVet.setWebUrl(webUrl);
		  		}
		  		dao.persistObject(newVet);
		  		pet.setVet(newVet);
		  		dao.mergeObject(pet);
  			}
  			else {
		  		if(!(vetname.equals(""))) {
		  			vet.setName(vetname);
		  		}
		  		if(!(address.equals(""))) {
		  			vet.setAddress(address);
		  		}
		  		if(!(phoneNumber.equals(""))) {
		  			vet.setPhoneNumber(phoneNumber);
		  		}
		  		if(!(vetemail.equals(""))) {
		  			vet.setEmail(vetemail);
		  		}
		  		if(!(webUrl.equals(""))) {
		  			vet.setWebUrl(webUrl);
		  		}
		  		dao.mergeObject(vet);
		  		if(!(u.getBadges().contains(dao.getBadge(4)))) {
  					u.getBadges().add(dao.getBadge(4));
  				}
  			}
  		}
  		if(!(u.getBadges().contains(dao.getBadge(4)))) {
			u.getBadges().add(dao.getBadge(4));
		}
  	}
  	
  	//get pet name
  	@GET
  	@Path("getPetName/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetName(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		return pet.getName();
  	}
  	
  	//get pet gender
  	@GET
  	@Path("getPetGender/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetGender(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		return pet.getGender();
  	}
  	
  	//get pet weight
  	@GET
  	@Path("getPetWeight/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetWeight(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		return pet.getWeight();
  	}
  	
  	//get pet species
  	@GET
  	@Path("getPetSpecies/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetSpecies(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		return pet.getSpecies();
  	}
  	
  	//get pet breed
  	@GET
  	@Path("getPetBreed/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetBreed(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		return pet.getBreed();
  	}
  	
  	//get pet colour
  	@GET
  	@Path("getPetColour/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetColour(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		return pet.getColour();
  	}
  	
  	//get pet dob
  	@GET
  	@Path("getPetDOB/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetDOB(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		return pet.getDob();
  	}
  	
  	//get pet height
  	@GET
  	@Path("getPetHeight/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetHeight(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		return pet.getHeight();
  	}
  	
  	//get pet more info
  	@GET
  	@Path("getPetMoreInfo/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getPetMoreInfo(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		return pet.getMoreinfo();
  	}
  	
  	//get food type
  	@GET
  	@Path("getFoodType/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFoodType(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Food food = pet.getFood();
  		return food.getType();
  	}
  	
  	//get food brand
  	@GET
  	@Path("getFoodBrand/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFoodBrand(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Food food = pet.getFood();
  		return food.getBrand();
  	}
  	
  	//get food times per day
  	@GET
  	@Path("getFoodTimesPerDay/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFoodTimesPerDay(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Food food = pet.getFood();
  		return food.getTimesPerDay();
  	}
  	
  	//get food quantity
  	@GET
  	@Path("getFoodQuantity/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFoodQuantity(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Food food = pet.getFood();
  		return food.getQuantity();
  	}
  	
  	//get more information about food
  	@GET
  	@Path("getFoodMoreInfo/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getFoodMoreInfo(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Food food = pet.getFood();
  		return food.getExtraInfo();
  	}
  	
  	//get habitat type
  	@GET
  	@Path("getHabitatType/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getHabitatType(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Habitat habitat = pet.getHabitat();
  		return habitat.getTypeRequired();
  	}
  	
  	//get habitat size
  	@GET
  	@Path("getHabitatSize/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getHabitatSize(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Habitat habitat = pet.getHabitat();
  		return habitat.getSize();
  	}
  	
  	//get habitat bedding
  	@GET
  	@Path("getHabitatBedding/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getHabitatBedding(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Habitat habitat = pet.getHabitat();
  		return habitat.getBedding();
  	}
  	
  	//get habitat temperature
  	@GET
  	@Path("getHabitatTemperature/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getHabitatTemperature(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Habitat habitat = pet.getHabitat();
  		return habitat.getTemperature();
  	}
  	
  	//get times cleaned
  	@GET
  	@Path("getTimesCleaned/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getTimesCleaned(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Habitat habitat = pet.getHabitat();
  		return habitat.getTimesCleaned();
  	}
  	
  	//get more information about habitat
  	@GET
  	@Path("getHabitatMoreInfo/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getHabitatMoreInfo(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Habitat habitat = pet.getHabitat();
  		return habitat.getMoreInfo();
  	}
  	
  	//get vet name
  	@GET
  	@Path("getVetName/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getVetName(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Vet vet = pet.getVet();
  		return vet.getName();
  	}
  	
  	//get vet address
  	@GET
  	@Path("getVetAddress/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getVetAddress(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Vet vet = pet.getVet();
  		return vet.getAddress();
  	}
  	
  	//get vet phone number
  	@GET
  	@Path("getVetPhoneNumber/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getVetPhoneNumber(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Vet vet = pet.getVet();
  		return vet.getPhoneNumber();
  	}
  	
  	//get vet email
  	@GET
  	@Path("getVetEmail/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getVetEmail(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Vet vet = pet.getVet();
  		return vet.getEmail();
  	}
  	
  	//get vet url
  	@GET
  	@Path("getVetURL/{email}/{petName}")
  	@Produces(MediaType.TEXT_HTML)
  	public String getVetURL(@PathParam("email") String email, @PathParam("petName") String petName){
  		Pet pet = dao.getPetFromUser(email, petName);
  		Vet vet = pet.getVet();
  		return vet.getWebUrl();
  	}
}
