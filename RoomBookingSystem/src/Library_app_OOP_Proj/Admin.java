package Library_app_OOP_Proj;

//Admin.java
import java.util.ArrayList;

public class Admin {
	private String username;
	private String password;
	private static ArrayList<Admin> adminList = new ArrayList<>();

	static {
	   adminList = FileHandler.loadAdmins();
	}

	public Admin(String username, String password) {
	   if (isUsernameAvailable(username)) {
	       this.username = username;
	       this.password = password;
	       adminList.add(this);
	       FileHandler.saveAdmins(adminList);
	   } else {
	       throw new IllegalArgumentException("Admin username already exists");
	   }
	}

	private boolean isUsernameAvailable(String username) {
	    for (Admin admin : adminList) {
	        if (admin.username.equals(username)) {
	            return false;
	        }
	    }
	    return true;
	}

	public void addRoom(String roomId) {
	   new Room(roomId);
	}

	public void removeRoom(String roomId) {
	   Room.removeRoom(roomId);
	}

	public void cancelBooking(String roomId, String date, int startTime) {
	    for (Booking b : Booking.getBookingList()) {
	        if (b.getRoom().getRoomId().equals(roomId) && b.getDate().equals(date) && b.getStartHour() == startTime) {
	        	b.cancel();
	            break;
	        }
	    }
	}
	
	public void setStatus(String roomId) {
		Room.setRoomStatus(roomId);
	}

	// Getters
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password; 
	}
	public static ArrayList<Admin> getAdminList() {
		return adminList; 
	}
}