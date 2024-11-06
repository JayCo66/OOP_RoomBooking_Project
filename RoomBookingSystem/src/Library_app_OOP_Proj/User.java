package Library_app_OOP_Proj;

//User.java 
import java.util.ArrayList;

public abstract class User {
	protected String username;
	protected String password;
	protected String userType;
	protected int maxHours; // จำนวนชั่วโมงที่จองได้ต่อวัน
	protected int maxAdvanceDays; // จำนวนวันที่จองล่วงหน้าได้
	private static ArrayList<User> userList = new ArrayList<>();

	static {
	   userList = FileHandler.loadUsers();
	}

	protected User(String username, String password, String userType) {
	   if (isUsernameAvailable(username)) {
	       this.username = username;
	       this.password = password;
	       this.userType = userType;
	       setUserLimits();
	       userList.add(this);
	       FileHandler.saveUsers(userList);
	   } else {
	       throw new IllegalArgumentException("Username already exists");
	   }
	}

	private void setUserLimits() {
	   switch (userType) {
	       case "STUDENT":
	           maxHours = 3;
	           maxAdvanceDays = 1;
	           break;
	       case "LECTURER":
	           maxHours = 5;
	           maxAdvanceDays = 7;
	           break;
	       case "GENERAL":
	           maxHours = 3;
	           maxAdvanceDays = 1;
	           break;
	   }
	}

	private boolean isUsernameAvailable(String username) {
	    for (User user : userList) {
	        if (user.username.equals(username)) {
	            return false;
	        }
	    }
	    return true;
	}

	// Getters
	public String getUsername() { 
		return username;
	}
	public String getPassword() { 
		return password; 
	}
	public String getUserType() { 
		return userType; 
	}
	public int getMaxHours() {
		return maxHours; 
	}
	public int getMaxAdvanceDays() {
		return maxAdvanceDays;
	}
	public static ArrayList<User> getUserList() {
		return userList; 
	}
}
