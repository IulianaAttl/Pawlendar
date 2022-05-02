package webservice;

import java.util.Comparator;

import entities.Task;

public class sortTasks implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {
		return o1.getDate().compareTo(o2.getDate());
	}
}
