package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

@Entity
@NamedQueries({
	@NamedQuery(name = "Account.findAll", query = "select o from Account o")
})
public class Account {

	//primary key in the database
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	//declare variables
	private int timesLoggedIn;
	private String dateCreated;
	private boolean isAddAPetUsed;
	private boolean isViewPetDetailsUsed;
	private boolean isCreateATaskUsed;
	
	//an account is linked to a user
	@OneToOne
	private User user;
	
}
