package cinema.model;

import java.util.ArrayList;
import java.util.List;

public class Hall {
	private int id;
	private String name;
	private List<ProjectionType> projectionTypes;
	
	public Hall() { this.projectionTypes = new ArrayList<ProjectionType>(); }
	
	public Hall(int id, String name, List<ProjectionType> projectionTypes) {
		super();
		this.id = id;
		this.name = name;
		this.projectionTypes = projectionTypes;
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

	public List<ProjectionType> getProjectionTypes() {
		return projectionTypes;
	}

	public void setProjectionTypes(List<ProjectionType> projectionTypes) {
		this.projectionTypes = projectionTypes;
	}
	
}
