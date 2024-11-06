package Library_app_OOP_Proj;

// FileHandler.java
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class FileHandler {
    private static final String DATA_DIR = "data";
    private static final String ROOMS_FILE = DATA_DIR + "/rooms.txt";
    private static final String BOOKINGS_FILE = DATA_DIR + "/bookings.txt";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String ADMINS_FILE = DATA_DIR + "/admins.txt";

    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            createFileIfNotExists(ROOMS_FILE);
            createFileIfNotExists(BOOKINGS_FILE);
            createFileIfNotExists(USERS_FILE);
            createFileIfNotExists(ADMINS_FILE);
        } catch (IOException e) {
            System.err.println("Error initializing data files: " + e.getMessage());
        }
    }

    private static void createFileIfNotExists(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    
    // Save methods
    public static void saveRooms(ArrayList<Room> rooms) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ROOMS_FILE))) {
            for (Room room : rooms) {
                writer.println(room.getRoomId() + "," + room.getStatus());
                for (Room.TimeSlot slot : room.getBookings()) {
                    writer.println(slot.getDate() + "," + Arrays.toString(slot.getTimeSlots()));
                }
                writer.println("---");  // ใช้เป็นตัวแบ่งระหว่างห้อง
            }
        } catch (IOException e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }


    public static void saveBookings(ArrayList<Booking> bookings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking booking : bookings) {
                writer.printf("%s,%s,%s,%d,%d,%s\n", booking.getUser().getUsername(), booking.getRoom().getRoomId(), booking.getDate(), booking.getStartHour(), booking.getDuration(), booking.getStatus());
            }
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    public static void saveUsers(ArrayList<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.printf("%s,%s,%s\n", user.getUsername(), user.getPassword(), user.getUserType());
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static void saveAdmins(ArrayList<Admin> admins) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ADMINS_FILE))) {
            for (Admin admin : admins) {
                writer.printf("%s,%s\n", admin.getUsername(), admin.getPassword());
            }
        } catch (IOException e) {
            System.err.println("Error saving admins: " + e.getMessage());
        }
    }

    // Load methods
    public static ArrayList<Room> loadRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        try {
            createFileIfNotExists(ROOMS_FILE);
            
            try (BufferedReader reader = new BufferedReader(new FileReader(ROOMS_FILE))) {
                String line;
                Room currentRoom = null;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    
                    if (line.equals("---")) {
                        currentRoom = null;
                        continue;
                    }
                    
                    if (currentRoom == null) {
                        // ใช้ createFromFile แทนการสร้าง Room ใหม่โดยตรง
                    	String p[] = line.split(",");
                        currentRoom = Room.createFromFile(p[0]);
                        currentRoom.setStatus(p[1]);
                        rooms.add(currentRoom);
                    } else {
                        String[] parts = line.split(",");
                        if (parts.length >= 2) {
                            String date = parts[0];
                            boolean[] slots = parseTimeSlots(parts[1]);
                            currentRoom.addBooking(date, findFirstBookedHour(slots), countBookedHours(slots));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading rooms: " + e.getMessage());
        }
        return rooms;
    }

    public static ArrayList<Booking> loadBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    User user = findUser(parts[0]);
                    Room room = findRoom(parts[1]);
                    if (user != null && room != null) {
                        bookings.add(new Booking(
                            user, room,
                            parts[2],
                            Integer.parseInt(parts[3]),
                            Integer.parseInt(parts[4])
                        ));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
        return bookings;
    }

    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    switch (parts[2]) {
                        case "STUDENT":
                            users.add(new Student(parts[0], parts[1]));
                            break;
                        case "LECTURER":
                            users.add(new Lecturer(parts[0], parts[1]));
                            break;
                        case "GENERAL":
                            users.add(new General(parts[0], parts[1]));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    public static ArrayList<Admin> loadAdmins() {
        ArrayList<Admin> admins = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMINS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    admins.add(new Admin(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading admins: " + e.getMessage());
        }
        return admins;
    }

    // Helper methods
    private static boolean[] parseTimeSlots(String str) {
        str = str.substring(1, str.length() - 1);
        String[] parts = str.split(", ");
        boolean[] slots = new boolean[12];  // เปลี่ยนเป็น 12 ช่อง
        for (int i = 0; i < Math.min(parts.length, 12); i++) {
            slots[i] = Boolean.parseBoolean(parts[i]);
        }
        return slots;
    }

    private static int findFirstBookedHour(boolean[] slots) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i]) return i + 9;  // แปลง index เป็นชั่วโมงจริง
        }
        return 9;  // เริ่มที่ 9:00 หากไม่พบการจอง
    }

    private static int countBookedHours(boolean[] slots) {
        int count = 0;
        for (boolean slot : slots) {
            if (slot) count++;
        }
        return count;
    }

    private static User findUser(String username) {
        for (User user : User.getUserList()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private static Room findRoom(String roomId) {
        for (Room room : Room.getRoomList()) {
            if (room.getRoomId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }
    
}	
            