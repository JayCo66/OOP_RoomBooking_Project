package Library_app_OOP_Proj;

//Room.java
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class Room {
	private String roomId;
	private String status;
    private ArrayList<TimeSlot> bookings;
    private static ArrayList<Room> roomList = new ArrayList<>();

    static {
        roomList = FileHandler.loadRooms();
    }

    public Room(String roomId) {
        if (isRoomIDAvailable(roomId)) {
            this.roomId = roomId;
            this.status = "true";
            this.bookings = new ArrayList<>();
            roomList.add(this);
            FileHandler.saveRooms(roomList);
        } else {
            throw new IllegalArgumentException("Room ID already exists");
        }
    }
    
    private Room(String roomId, boolean isLoading) {
        this.roomId = roomId;
        this.bookings = new ArrayList<>();
        if (!isLoading) {
            roomList.add(this);
            FileHandler.saveRooms(roomList);
        }
    }

	public static void removeRoom(String roomId) {
	    Room roomToRemove = null;
	    for (Room room : roomList) {
	        if (room.getRoomId().equals(roomId)) {
	            roomToRemove = room;
	            break;
	        }
	    }
	    if (roomToRemove != null) {
	        roomList.remove(roomToRemove);
	        FileHandler.saveRooms(roomList);
	    }
	}

	private boolean isRoomIDAvailable(String roomId) {
        for (Room room : roomList) {
            if (room.roomId.equals(roomId)) {
                return false;
            }
        }
        return true;
    }
	
	public static Room createFromFile(String roomId) {
        Room room = new Room(roomId, true);
        roomList.add(room);
        return room;
    }

	public boolean isAvailable(String date, int startHour, int duration) {
        try {
            validateBookingTime(date, startHour, duration);
            TimeSlot slot = findTimeSlot(date);
            if (slot == null) return true;

            // ตรวจสอบช่วงเวลาที่ต้องการจอง
            for (int hour = startHour; hour < startHour + duration; hour++) {
                if (hour < 9 || hour >= 21) {  // ปรับเงื่อนไขให้อยู่ในช่วง 9:00-20:00
                    return false;
                }
                if (slot.isTimeBooked(hour)) return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	private void validateBookingTime(String date, int startHour, int duration) {
        try {
            LocalDate bookingDate = LocalDate.parse(date);
            LocalDate today = LocalDate.now();
            
            if (bookingDate.isBefore(today)) {
                throw new IllegalArgumentException("Cannot book past dates");
            }
            
            if (startHour < 9 || startHour >= 20) {  // ปรับเงื่อนไขเวลา
                throw new IllegalArgumentException("Booking must start between 9:00 and 20:00");
            }
            
            if (startHour + duration > 20) {  // ปรับเงื่อนไขเวลาสิ้นสุด
                throw new IllegalArgumentException("Booking cannot end after 20:00");
            }
            
            if (duration < 1 || duration > 5) {
                throw new IllegalArgumentException("Duration must be between 1 and 5 hours");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }


	public void addBooking(String date, int startHour, int duration) {
        TimeSlot slot = findOrCreateTimeSlot(date);
        for (int hour = startHour; hour < startHour + duration; hour++) {
            slot.bookTime(hour);
        }
        FileHandler.saveRooms(roomList);  // บันทึกข้อมูลทันทีที่มีการจอง
    }

	 public void cancelBooking(String date, int startHour, int duration) {
	        TimeSlot slot = findTimeSlot(date);
	        if (slot != null) {
	            for (int hour = startHour; hour < startHour + duration; hour++) {
	                slot.cancelTime(hour);
	            }
	            FileHandler.saveRooms(roomList);  // บันทึกข้อมูลทันทีที่มีการยกเลิก
	        }
	 }

	private TimeSlot findTimeSlot(String date) {
	   for (TimeSlot slot : bookings) {
	       if (slot.getDate().equals(date)) {
	           return slot;
	       }
	   }
	   return null;
	}

	private TimeSlot findOrCreateTimeSlot(String date) {
	   TimeSlot slot = findTimeSlot(date);
	   if (slot == null) {
	       slot = new TimeSlot(date);
	       bookings.add(slot);
	   }
	   return slot;
	}

	// TimeSlot inner class
	 public static class TimeSlot {
	        private String date;
	        private boolean[] timeSlots;  // 12 ชั่วโมง (9:00-20:00)

	        public TimeSlot(String date) {
	            this.date = date;
	            this.timeSlots = new boolean[12];  // เปลี่ยนเป็น 12 ช่องเวลา
	            Arrays.fill(timeSlots, false);
	        }

	        public TimeSlot(String date, boolean[] slots) {
	            this.date = date;
	            this.timeSlots = slots;
	        }

	        public String getDate() { 
	            return date; 
	        }

	        public boolean[] getTimeSlots() {
	            return timeSlots.clone();
	        }

	        public boolean isTimeBooked(int hour) { 
	            int index = hour - 9;  // แปลงจากชั่วโมงจริงเป็น index (9:00 = 0, 10:00 = 1, ...)
	            if (index < 0 || index >= timeSlots.length) {
	                throw new IllegalArgumentException("Hour must be between 9 and 20");
	            }
	            return timeSlots[index];
	        }

	        public void bookTime(int hour) { 
	            int index = hour - 9;
	            if (index < 0 || index >= timeSlots.length) {
	                throw new IllegalArgumentException("Hour must be between 9 and 20");
	            }
	            timeSlots[index] = true;
	        }

	        public void cancelTime(int hour) { 
	            int index = hour - 9;
	            if (index < 0 || index >= timeSlots.length) {
	                throw new IllegalArgumentException("Hour must be between 9 and 20");
	            }
	            timeSlots[index] = false;
	        }
	    }


	// Getters
	public String getRoomId() { 
		return roomId; 
	}
	public ArrayList<TimeSlot> getBookings() {
		return bookings; 
	}
	public static ArrayList<Room> getRoomList() {
		return roomList;
	}
	public String getStatus() {
		return this.status;
	}
	
	public static void setRoomStatus(String roomId) {
		for(Room R : roomList) {
			if(R.getRoomId().equals(roomId)) {
				R.setStatus("false");
			}
		}
	}
	
	public void setStatus(String a) {
		this.status = a;
	}
	
}
