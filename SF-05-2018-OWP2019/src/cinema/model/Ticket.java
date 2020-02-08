package cinema.model;

import java.time.LocalDateTime;

public class Ticket {
	private int id;
	private Projection projection;
	private Seat seat;
	private Hall hall;
	private LocalDateTime time;
	private User user;
	private boolean active;
	
	public Ticket() {}

	public Ticket(int id, Projection projection, Seat seat, LocalDateTime time, User user, boolean active) {
		super();
		this.id = id;
		this.projection = projection;
		this.seat = seat;
		this.hall = seat.getHall();
		this.time = time;
		this.user = user;
		this.setActive(active);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Projection getProjection() {
		return projection;
	}

	public void setProjection(Projection projection) {
		this.projection = projection;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public Hall getHall() {
		return hall;
	}

//	public void setHall(Hall hall) {
//		this.hall = hall;
//	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
	

}
