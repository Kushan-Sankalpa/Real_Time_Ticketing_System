// User.java

public abstract class User implements Runnable {
    private static int userCount = 0;
    protected final int userId;
    protected final TicketPool ticketPool;
    protected volatile boolean running = true; // Control flag

    public User(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
        this.userId = ++userCount;
    }

    // Method to stop the thread
    public void stop() {
        running = false;
    }

    // Abstract run method to be implemented by subclasses
    public abstract void run();
}
// this new