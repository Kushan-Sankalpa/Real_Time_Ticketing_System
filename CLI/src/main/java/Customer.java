
public class Customer extends User {
    private final int customerRetrievalRate; // in milliseconds

    public Customer(TicketPool ticketPool, int customerRetrievalRate) {
        super(ticketPool);
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        System.out.println("Customer-" + userId + " started.");

        while (running) {
            try {
                String ticket = ticketPool.removeTicket();
                if (ticket == null) {

                    System.out.println("Customer-" + userId + " found no tickets available. Exiting.");
                    break;
                }

                System.out.println("*** Customer-" + userId + " purchased " + ticket  +"  *** \n");
                Thread.sleep(customerRetrievalRate);
            } catch (InterruptedException e) {
                System.out.println("Customer-" + userId + " interrupted.");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.out.println("Customer-" + userId + " encountered an error: " + e.getMessage());
            }
        }
        System.out.println("Customer-" + userId + " stopped.");
    }
}
