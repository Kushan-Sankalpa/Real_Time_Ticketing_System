
/**
 * Describes a vendor in the ticketing system.
 * Vendors add tickets to the ticket pool.
 */
public class Vendor extends User {
    private final int ticketReleaseRate;// in milliseconds
    private int ticketNumber = 1; // To track the number of tickets added by this vendor


    /**
     * Constructs a new Vendor with the specified ticket pool and release rate.
     *
     * @param ticketPool        the shared ticket pool.
     * @param ticketReleaseRate the rate at which the vendor releases tickets.
     */
    public Vendor(TicketPool ticketPool, int ticketReleaseRate) {
        super(ticketPool);
        this.ticketReleaseRate = ticketReleaseRate;
    }


    /**
     * The run method for the vendor thread.
     * Adds tickets to the pool at the specified release rate.
     */
    @Override
    public void run() {
        System.out.println("Vendor-" + userId + " started.");

        while (running) {
            try {
                String ticket = "Ticket-" + userId + "-" + ticketNumber++;
                boolean added = ticketPool.addTicket(ticket);
                if (!added) {

                    System.out.println("Vendor-" + userId + " has released all tickets.");
                    break;
                }

                System.out.println("*** Vendor-" + userId + " Released " + ticket +"  *** \n");
                Thread.sleep(ticketReleaseRate);
            } catch (InterruptedException e) {
                System.out.println("Vendor-" + userId + " interrupted.");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.out.println("Vendor-" + userId + " encountered an error: " + e.getMessage());
            }
        }
        System.out.println("Vendor-" + userId + " stopped.");
    }
}
