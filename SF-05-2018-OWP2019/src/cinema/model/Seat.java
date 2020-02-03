package cinema.model;

public class Seat {
	private int number;
	private Hall hall;
	
	public Seat() {}
	
	public Seat(int number, Hall hall) {
		super();
		this.number = number;
		this.hall = hall;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}
	
}
