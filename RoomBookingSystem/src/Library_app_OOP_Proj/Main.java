package Library_app_OOP_Proj;

//Main.java
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Main {
 public static void main(String[] args) {
     try {
         new File("data").mkdirs();

         ArrayList<Admin> admins = Admin.getAdminList();
         ArrayList<User> users = User.getUserList();
         ArrayList<Room> rooms = Room.getRoomList();
         ArrayList<Booking> bookings = Booking.getBookingList();

         if (admins.isEmpty()) {
             new Admin("admin", "admin123");
         }

         if (rooms.isEmpty()) {
             String[] defaultRooms = {"R001", "R002", "R003"};
             for (String roomId : defaultRooms) {
                 new Room(roomId);
             }
         }

         new LoginPage().setVisible(true);

     } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Error starting application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
     }
 }
}








