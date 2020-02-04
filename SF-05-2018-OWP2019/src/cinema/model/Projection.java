package cinema.model;

import java.time.LocalDateTime;

public class Projection {
	private int id;
	private Movie movie;
	private ProjectionType projectionType;
	private Hall hall;
	private LocalDateTime time;
	private int price;
	private User admin;
	private boolean active;
	
	public Projection() {}
	
	public Projection(int id, Movie movie, ProjectionType projectionType, Hall hall, LocalDateTime time, int price,
			User admin, boolean active) {
		super();
		this.id = id;
		this.movie = movie;
		this.projectionType = projectionType;
		this.hall = hall;
		this.time = time;
		this.price = price;
		this.admin = admin;
		this.active = active;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public ProjectionType getProjectionType() {
		return projectionType;
	}

	public void setProjectionType(ProjectionType projectionType) {
		this.projectionType = projectionType;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		if (price > 0)
			this.price = price;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}
