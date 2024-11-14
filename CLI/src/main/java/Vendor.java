// Vendor.java

public class Vendor extends User {
    private final int ticketReleaseRate; // in milliseconds
    private int ticketNumber = 1; // To track the number of tickets added by this vendor

    public Vendor(TicketPool ticketPool, int ticketReleaseRate) {
        super(ticketPool);
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        System.out.println("Vendor - " + userId + " started.");

        while (running) { // Check the control flag
            try {
                String ticket = "Ticket -" + userId + "-" + ticketNumber++;
                boolean added = ticketPool.addTicket(ticket);
                if (!added) {
                    // No more tickets can be added
                    System.out.println("Vendor -" + userId + " has released all tickets.");
                    break; // Exit the loop to terminate the thread
                }
                // Simulate processing time
                Thread.sleep(ticketReleaseRate);
            } catch (InterruptedException e) {
                System.out.println("Vendor-" + userId + " interrupted.");
                Thread.currentThread().interrupt(); // Preserve interrupt status
                break; // Exit the loop to terminate the thread
            } catch (Exception e) {
                System.out.println("Vendor-" + userId + " encountered an error: " + e.getMessage());
            }
        }
        System.out.println("Vendor-" + userId + " stopped.");
    }
}
