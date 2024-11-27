import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<String> tickets;
    private final int maxCapacity;
    private final int totalTicketsToRelease;
    private int totalTicketsAdded;
    private int totalTicketsSold;

    public TicketPool(int initialTickets, int maxCapacity, int totalTicketsToRelease) {
        this.tickets = new LinkedList<>();
        this.maxCapacity = maxCapacity;
        this.totalTicketsToRelease = totalTicketsToRelease;
        this.totalTicketsAdded = 0;
        this.totalTicketsSold = 0;

        int ticketsToAdd = Math.min( initialTickets,Math.min(maxCapacity,totalTicketsToRelease));
        for (int i = 1; i <= ticketsToAdd; i++) {
            tickets.add("Ticket-" + i);
            totalTicketsAdded++;
        }
        if(initialTickets > ticketsToAdd){
            System.out.println("The Tickets you Added  (" + initialTickets + ") exceed maximum capacity or total tickets to release (" + totalTicketsToRelease + "). Only " + ticketsToAdd + " tickets were added.\n");
        }
        // Display initial ticket status
        System.out.println("[ "+ ticketsToAdd+ " Tickets added to the Ticket pool ]\n");
        System.out.println("Current Tickets in the Ticket pool : " + tickets.size()+ "\n");
    }

    // Synchronized method to add a ticket

    public synchronized boolean addTicket(String ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) {
            wait(); // if the pool is full, the vendor will wait
        }
        if (totalTicketsAdded >= totalTicketsToRelease) {
            // All tickets have been added
            return false;
        }
        tickets.add(ticket);
        totalTicketsAdded++;
        System.out.println("Vendor added: " + ticket + "  [ Available Tickets in pool: " + tickets.size()+ " ]");
        notifyAll();
        return true;
    }

    // Synchronized method to remove a ticket
    public synchronized String removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            if (totalTicketsSold >= totalTicketsToRelease) {
                // All tickets have been sold
                return null;
            }
            wait();
        }
        String ticket = tickets.remove(0);
        totalTicketsSold++;
        System.out.println("Customer purchased: " + ticket + "  [ Available Tickets in pool: " + tickets.size()+ " ]");
        notifyAll(); // Notify producers that space is available
        return ticket;
    }

    // Method to get the current number of tickets
    public synchronized int getCurrentTicketCount() {
        return tickets.size();
    }

    public synchronized int getTotalTicketsAdded() {
        return totalTicketsAdded;
    }

    public synchronized int getTotalTicketsSold() {
        return totalTicketsSold;
    }






}
