
import java.util.ArrayList;
import java.util.Scanner;

public class HotelManagementSystem {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();
    static int nextReservationId = 1001;

    static class Room {
        int roomNumber;
        String type;
        double price;
        boolean available = true;

        Room(int roomNumber, String type, double price) {
            this.roomNumber = roomNumber;
            this.type = type;
            this.price = price;
        }
    }

    static class Reservation {
        int id;
        String guestName;
        String phone;
        Room room;
        int nights;
        double totalBill;
        boolean checkedOut = false;

        Reservation(int id, String guestName, String phone,
                    Room room, int nights) {
            this.id = id;
            this.guestName = guestName;
            this.phone = phone;
            this.room = room;
            this.nights = nights;
            this.totalBill = room.price * nights;
        }
    }

    public static void main(String[] args) {

        // Initialize hotel rooms
        rooms.add(new Room(101, "Single", 1000));
        rooms.add(new Room(102, "Single", 1000));
        rooms.add(new Room(201, "Double", 2000));
        rooms.add(new Room(202, "Double", 2000));
        rooms.add(new Room(301, "Deluxe", 3500));
        rooms.add(new Room(302, "Deluxe", 3500));

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println(" HOTEL ROOM RESERVATION SYSTEM");
            System.out.println("================================");
            System.out.println("1. View All Rooms");
            System.out.println("2. View Available Rooms");
            System.out.println("3. Book a Room");
            System.out.println("4. View All Reservations");
            System.out.println("5. Search Reservation");
            System.out.println("6. Check Out Guest");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    viewRooms(false);
                    break;
                case 2:
                    viewRooms(true);
                    break;
                case 3:
                    bookRoom();
                    break;
                case 4:
                    viewReservations();
                    break;
                case 5:
                    searchReservation();
                    break;
                case 6:
                    checkOut();
                    break;
                case 7:
                    System.out.println("Thank you for using our system!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 7);

        sc.close();
    }

    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    static void viewRooms(boolean availableOnly) {
        System.out.println("\n--- ROOM DETAILS ---");
        System.out.printf("%-12s %-12s %-12s %-12s%n",
                "Room No.", "Type", "Price/Night", "Status");

        for (Room room : rooms) {
            if (!availableOnly || room.available) {
                System.out.printf("%-12d %-12s Rs.%-9.2f %-12s%n",
                        room.roomNumber,
                        room.type,
                        room.price,
                        room.available ? "Available" : "Booked");
            }
        }
    }

    static void bookRoom() {
        viewRooms(true);

        System.out.print("\nEnter room number: ");
        int roomNumber = readInt();

        Room selectedRoom = null;

        for (Room room : rooms) {
            if (room.roomNumber == roomNumber && room.available) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room not available or invalid room number.");
            return;
        }

        System.out.print("Enter guest name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Guest name cannot be empty.");
            return;
        }

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine().trim();

        if (phone.isEmpty()) {
            System.out.println("Phone number cannot be empty.");
            return;
        }

        System.out.print("Enter number of nights: ");
        int nights = readInt();

        if (nights <= 0) {
            System.out.println("Number of nights must be greater than zero.");
            return;
        }

        Reservation reservation = new Reservation(
                nextReservationId++,
                name,
                phone,
                selectedRoom,
                nights
        );

        reservations.add(reservation);
        selectedRoom.available = false;

        System.out.println("\n*** ROOM BOOKED SUCCESSFULLY ***");
        System.out.println("Reservation ID : " + reservation.id);
        System.out.println("Guest Name     : " + reservation.guestName);
        System.out.println("Room Number    : " + selectedRoom.roomNumber);
        System.out.println("Room Type      : " + selectedRoom.type);
        System.out.println("Price Per Night: Rs." + selectedRoom.price);
        System.out.println("Number of Nights: " + nights);
        System.out.printf("Total Bill     : Rs.%.2f%n",
                reservation.totalBill);
    }

    static void viewReservations() {
        System.out.println("\n--- ALL RESERVATIONS ---");

        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        for (Reservation r : reservations) {
            printReservation(r);
        }
    }

    static void searchReservation() {
        System.out.print("Enter reservation ID: ");
        int id = readInt();

        Reservation found = findReservation(id);

        if (found != null) {
            printReservation(found);
        } else {
            System.out.println("Reservation not found.");
        }
    }

    static Reservation findReservation(int id) {
        for (Reservation r : reservations) {
            if (r.id == id) {
                return r;
            }
        }
        return null;
    }

    static void printReservation(Reservation r) {
        System.out.println("\n------------------------------");
        System.out.println("Reservation ID : " + r.id);
        System.out.println("Guest Name     : " + r.guestName);
        System.out.println("Phone Number   : " + r.phone);
        System.out.println("Room Number    : " + r.room.roomNumber);
        System.out.println("Room Type      : " + r.room.type);
        System.out.println("Number of Nights: " + r.nights);
        System.out.printf("Total Bill     : Rs.%.2f%n", r.totalBill);
        System.out.println("Status         : "
                + (r.checkedOut ? "Checked Out" : "Checked In"));
        System.out.println("------------------------------");
    }

    static void checkOut() {
        System.out.print("Enter reservation ID: ");
        int id = readInt();

        Reservation r = findReservation(id);

        if (r == null) {
            System.out.println("Reservation not found.");
            return;
        }

        if (r.checkedOut) {
            System.out.println("This guest has already checked out.");
            return;
        }

        r.checkedOut = true;
        r.room.available = true;

        System.out.println("\n*** CHECKOUT SUCCESSFUL ***");
        System.out.println("Guest Name : " + r.guestName);
        System.out.println("Room Number: " + r.room.roomNumber);
        System.out.printf("Final Bill : Rs.%.2f%n", r.totalBill);
        System.out.println("Room is now available for booking.");
    }
}
