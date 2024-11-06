import java.io.*;
import java.util.*;

// Abstract class User

// Student class

// Lecturer class

// General class

// Room class
class Room implements Serializable {
    private String roomID;
    private boolean[] availability;

    public Room(String roomID) {
        this.roomID = roomID;
        this.availability = new boolean[10]; // 10 ชั่วโมงต่อวัน
        Arrays.fill(this.availability, true); // ทุกช่วงเวลาว่างในตอนแรก
    }

    public boolean isAvailable(int startHour, int hours) {
        for (int i = startHour; i < startHour + hours; i++) {
            if (!availability[i]) return false;
        }
        return true;
    }

    public void bookRoom(int startHour, int hours) {
        for (int i = startHour; i < startHour + hours; i++) {
            availability[i] = false;
        }
    }

    public String getRoomID() {
        return roomID;
    }
}

// Booking class


// BookingSystem class

// Main class to run the program
