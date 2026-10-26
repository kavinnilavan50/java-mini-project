import java.util.ArrayList;
import java.util.Scanner;

class Room {
    private final int roomNumber;
    private final String roomType;
    private final double pricePerNight;
    private boolean available;

    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.available = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void displayRoom() {
        System.out.printf("%-12d %-15s Rs.%-12.2f %-12s%n",
                roomNumber, roomType, pricePerNight,
                available ? "Available" : "Occupied");
    }
}

class Guest {
    private final String name;
    private final String phone;
    private final String address;

    public Guest(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}

class Reservation {
    private final int reservationId;
    private final Guest guest;
    private final Room room;
    private final int nights;

    public Reservation(int reservationId, Guest guest,
                       Room room, int nights) {
        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.nights = nights;
    }

    public int getReservationId() {
        return reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public double calculateBill() {
        return room.getPricePerNight() * nights;
    }

    public void displayReservation() {
        System.out.println("\n========== RESERVATION DETAILS ==========");
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guest.getName());
        System.out.println("Phone Number   : " + guest.getPhone());
        System.out.println("Guest Address  : " + guest.getAddress());
        System.out.println("Room Number    : " + room.getRoomNumber());
        System.out.println("Room Type      : " + room.getRoomType());
        System.out.println("Price Per Night: Rs."
                + String.format("%.2f", room.getPricePerNight()));
        System.out.println("Number of Nights: " + nights);
        System.out.printf("Total Bill     : Rs.%.2f%n", calculateBill());
        System.out.println("=========================================");
    }
}

public class HotelReservationSystem {

    private static final Scanner sc = new Scanner(System.in);

    private static final ArrayList<Room> rooms = new ArrayList<>();
    private static final ArrayList<Reservation> reservations =
            new ArrayList<>();

    private static int nextReservationId = 1001;

    public static void main(String[] args) {
        initializeRooms();

        System.out.println("Welcome to the Hotel Reservation System!");

        while (true) {
            displayMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    displayRooms();
                    break;

                case 2:
                    reserveRoom();
                    break;

                case 3:
                    viewReservations();
                    break;

                case 4:
                    searchReservation();
                    break;

                case 5:
                    checkOut();
                    break;

                case 6:
                    System.out.println("Thank you for visiting our hotel!");
                    sc.close();
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please enter a number from 1 to 6.");
            }
        }
    }

    private static void initializeRooms() {
        rooms.add(new Room(101, "Single", 1000.00));
        rooms.add(new Room(102, "Single", 1000.00));
        rooms.add(new Room(103, "Double", 1800.00));
        rooms.add(new Room(104, "Double", 1800.00));
        rooms.add(new Room(105, "Deluxe", 3000.00));
        rooms.add(new Room(106, "Deluxe", 3000.00));
    }

    private static void displayMenu() {
        System.out.println("\n======================================");
        System.out.println(" HOTEL ROOM RESERVATION AND");
        System.out.println(" GUEST MANAGEMENT SYSTEM");
        System.out.println("======================================");
        System.out.println("1. View All Rooms");
        System.out.println("2. Reserve a Room");
        System.out.println("3. View All Reservations");
        System.out.println("4. Search Reservation / Guest");
        System.out.println("5. Guest Check-out");
        System.out.println("6. Exit");
        System.out.println("======================================");
    }

    private static void displayRooms() {
        System.out.println("\n================ HOTEL ROOMS ================");
        System.out.printf("%-12s %-15s %-15s %-12s%n",
                "Room Number", "Room Type", "Price/Night", "Status");

        for (Room room : rooms) {
            room.displayRoom();
        }
    }

    private static Room findRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    private static Reservation findReservation(int id) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == id) {
                return reservation;
            }
        }
        return null;
    }

    private static void reserveRoom() {
        displayRooms();

        int roomNumber = readInt("\nEnter room number: ");
        Room room = findRoom(roomNumber);

        if (room == null) {
            System.out.println("Error: Room number does not exist.");
            return;
        }

        if (!room.isAvailable()) {
            System.out.println("Error: This room is already occupied.");
            return;
        }

        String name = readNonEmpty("Enter guest name: ");
        String phone = readPhoneNumber();
        String address = readNonEmpty("Enter guest address: ");
        int nights = readPositiveInt("Enter number of nights: ");

        Guest guest = new Guest(name, phone, address);

        Reservation reservation = new Reservation(
                nextReservationId, guest, room, nights);

        reservations.add(reservation);
        room.setAvailable(false);
        nextReservationId++;

        System.out.println("\nRoom reserved successfully!");
        reservation.displayReservation();
    }

    private static void viewReservations() {
        if (reservations.isEmpty()) {
            System.out.println("\nNo active reservations found.");
            return;
        }

        System.out.println("\n========== ALL ACTIVE RESERVATIONS ==========");

        for (Reservation reservation : reservations) {
            reservation.displayReservation();
        }
    }

    private static void searchReservation() {
        int id = readInt("\nEnter reservation ID: ");
        Reservation reservation = findReservation(id);

        if (reservation == null) {
            System.out.println("No active reservation found with ID " + id);
        } else {
            reservation.displayReservation();
        }
    }

    private static void checkOut() {
        int id = readInt("\nEnter reservation ID for check-out: ");
        Reservation reservation = findReservation(id);

        if (reservation == null) {
            System.out.println("No active reservation found with ID " + id);
            return;
        }

        reservation.displayReservation();

        String confirmation = readNonEmpty(
                "Confirm check-out? (yes/no): ");

        if (confirmation.equalsIgnoreCase("yes")) {
            Room room = reservation.getRoom();

            room.setAvailable(true);
            reservations.remove(reservation);

            System.out.println("\nCheck-out completed successfully!");
            System.out.println("Guest: " + reservation.getGuest().getName());
            System.out.println("Room " + room.getRoomNumber()
                    + " is now available.");
            System.out.printf("Final Bill: Rs.%.2f%n",
                    reservation.calculateBill());
        } else {
            System.out.println("Check-out cancelled.");
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static int readPositiveInt(String message) {
        while (true) {
            int value = readInt(message);

            if (value > 0) {
                return value;
            }

            System.out.println("Value must be greater than zero.");
        }
    }

    private static String readNonEmpty(String message) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    private static String readPhoneNumber() {
        while (true) {
            String phone = readNonEmpty("Enter guest phone number: ");
            String digitsOnly = phone.replaceAll("\\D", "");

            if (digitsOnly.length() >= 7
                    && digitsOnly.length() <= 15
                    && phone.matches("[0-9+() .-]+")) {
                return phone;
            }

            System.out.println(
                    "Enter a valid phone number containing 7 to 15 digits.");
        }
    }
}
