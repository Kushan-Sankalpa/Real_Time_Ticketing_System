
/**
 * Abstract base class describing a user in the ticketing system.
 * Can be a Vendor or a Customer.
 */
public abstract class User implements Runnable {
    private static int userCount = 0;
    protected final int userId;
    protected final TicketPool ticketPool;
    protected volatile boolean running = true; // Control flag

    /**
     * Constructs a new User with a shared ticket pool.
     *
     * @param ticketPool the shared ticket pool.
     */
    public User(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
        this.userId = ++userCount;
    }




    /**
     * Stops the user's thread
     */
    public void stop() {
        running = false;
    }

    /**
     * The run method to be implemented by subclasses.
     */
    public abstract void run();
}
