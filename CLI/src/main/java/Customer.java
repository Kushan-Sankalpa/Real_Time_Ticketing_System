// Customer.java

public class Customer extends User {
    private final int customerRetrievalRate; // in milliseconds

    public Customer(TicketPool ticketPool, int customerRetrievalRate) {
        super(ticketPool);
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        System.out.println("Customer -" + userId + " started.");

        while (running) { // Check the control flag
            try {
                String ticket = ticketPool.removeTicket();
                if (ticket == null) {
                    // All tickets have been sold
                    System.out.println("Customer -" + userId + " found no tickets available. Exiting.");
                    break;
                }
                // Simulate processing the ticket purchase
                System.out.println("Customer -" + userId + " purchased " + ticket);
                Thread.sleep(customerRetrievalRate);
            } catch (InterruptedException e) {
                System.out.println("Customer -" + userId + " interrupted.");
                Thread.currentThread().interrupt(); // Preserve interrupt status
                break; // Exit the loop to terminate the thread
            } catch (Exception e) {
                System.out.println("Customer -" + userId + " encountered an error: " + e.getMessage());
            }
        }
        System.out.println("Customer -" + userId + " stopped.");
    }
}
