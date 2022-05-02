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
	@NamedQuery(name = "Task.findAll", query = "select o from Task o")
})
public class Task {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String title;
	private String date;
	private String time;
	private String content;
	private boolean copied;
	
	//task references a pet
	@OneToOne
	private Pet pet;
	
	public Task() {
		
	}

	public Task(String title, String date, String time, String content, Pet pet, boolean copied) {
		this.title = title;
		this.date = date;
		this.time = time;
		this.content = content;
		this.pet = pet;
		this.copied = copied;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public boolean isCopied() {
		return copied;
	}

	public void setCopied(boolean copied) {
		this.copied = copied;
	}
}
