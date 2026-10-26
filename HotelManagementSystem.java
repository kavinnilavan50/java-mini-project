import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();
    static int nextId = 1001;

    public static void main(String[] args) {
        rooms.add(new Room(101, "Single", 1000));
        rooms.add(new Room(102, "Double", 1800));
        rooms.add(new Room(103, "Deluxe", 3000));

        while (true) {
            System.out.println("\n=== HOTEL RESERVATION SYSTEM ===");
            System.out.println("1. View Rooms");
            System.out.println("2. Reserve Room");
            System.out.println("3. View Reservations");
            System.out.println("4. Check Out");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    for (Room r : rooms) {
                        System.out.println(r);
                    }
                    break;

                case 2:
                    System.out.print("Enter room number: ");
                    int number = readInt();
                    Room room = findRoom(number);

                    if (room == null || !room.available) {
                        System.out.println("Room unavailable or invalid.");
                        break;
                    }

                    System.out.print("Guest name: ");
                    String name = sc.nextLine().trim();

                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty.");
                        break;
                    }

                    System.out.print("Phone number: ");
                    String phone = sc.nextLine().trim();

                    System.out.print("Number of nights: ");
                    int nights = readInt();

                    if (nights <= 0) {
                        System.out.println("Nights must be positive.");
                        break;
                    }

                    Reservation res = new Reservation(
                        nextId++, name, phone, room, nights
                    );
                    reservations.add(res);
                    room.available = false;

                    System.out.println("Reservation successful!");
                    System.out.println(res);
                    break;

                case 3:
                    if (reservations.isEmpty()) {
                        System.out.println("No reservations found.");
                    } else {
                        for (Reservation r : reservations) {
                            System.out.println(r);
                        }
                    }
                    break;

                case 4:
                    System.out.print("Enter reservation ID: ");
                    int id = readInt();
                    Reservation found = null;

                    for (Reservation r : reservations) {
                        if (r.id == id) {
                            found = r;
                            break;
                        }
                    }

                    if (found == null) {
                        System.out.println("Reservation not found.");
                    } else {
                        found.room.available = true;
                        System.out.printf("Checkout successful. Bill: Rs.%.2f%n",
                                found.room.price * found.nights);
                        reservations.remove(found);
                    }
                    break;

                case 5:
                    System.out.println("Thank you!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
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

    static Room findRoom(int number) {
        for (Room r : rooms) {
            if (r.number == number) {
                return r;
            }
        }
        return null;
    }
}

class Room {
    int number;
    String type;
    double price;
    boolean available = true;

    Room(int number, String type, double price) {
        this.number = number;
        this.type = type;
        this.price = price;
    }

    public String toString() {
        return "Room " + number + " | " + type
                + " | Rs." + price + " per night | "
                + (available ? "Available" : "Occupied");
    }
}

class Reservation {
    int id;
    String name;
    String phone;
    Room room;
    int nights;

    Reservation(int id, String name, String phone,
                Room room, int nights) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.room = room;
        this.nights = nights;
    }

    public String toString() {
        return "Reservation ID: " + id
                + " | Guest: " + name
                + " | Phone: " + phone
                + " | Room: " + room.number
                + " | Nights: " + nights
                + " | Bill: Rs." + (room.price * nights);
    }
}
