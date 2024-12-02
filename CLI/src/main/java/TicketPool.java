import java.util.LinkedList;
import java.util.List;


/**
 * Describes the ticket pool shared between vendors and customers.
 * Manages ticket addition and removal with synchronization.
 */
public class TicketPool {
    private final List<String> tickets;
    private final int maxCapacity;
    private final int totalTicketsToRelease;
    private int totalTicketsAdded;
    private int totalTicketsSold;


    /**
     * Constructs a new TicketPool.
     *
     * @param initialTickets      the initial number of tickets to add.
     * @param maxCapacity         the maximum capacity of the ticket pool.
     * @param totalTicketsToRelease the total number of tickets to be released.
     */
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

    /**
     * Adds a ticket to the pool if capacity allows.
     * Waits if the pool is full.
     *
     * @param ticket the ticket to add.
     * @return true if the ticket was added, false if all tickets have been released.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public synchronized boolean addTicket(String ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) {
            wait();
        }
        if (totalTicketsAdded >= totalTicketsToRelease) {
            return false;
        }
        tickets.add(ticket);
        totalTicketsAdded++;
        System.out.println("Vendor added: " + ticket + "  [ Available Tickets in pool: " + tickets.size()+ " ]");
        notifyAll();
        return true;
    }



    /**
     * Removes a ticket from the pool for a customer.
     * Waits if the pool is empty.
     *
     * @return the ticket removed, or null if all tickets have been sold.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    public synchronized String removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            if (totalTicketsSold >= totalTicketsToRelease) {
                return null;
            }
            wait();
        }
        String ticket = tickets.remove(0);
        totalTicketsSold++;
        System.out.println("Customer purchased: " + ticket + "  [ Available Tickets in pool: " + tickets.size()+ " ]");
        notifyAll();
        return ticket;
    }


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
