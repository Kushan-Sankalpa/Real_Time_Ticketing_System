

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Configuration {
    private int totalTickets;
    private int initialTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int numberOfVendors;
    private int numberOfCustomers;

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getInitialTickets() {
        return initialTickets;
    }

    public void setInitialTickets(int initialTickets) {
        this.initialTickets = initialTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getNumberOfVendors() {
        return numberOfVendors;
    }

    public void setNumberOfVendors(int numberOfVendors) {
        this.numberOfVendors = numberOfVendors;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    public void configure (Scanner scanner){
        System.out.println("*****************************************");
        System.out.println("*   Real- Time Event Ticketing System   *");
        System.out.println("*****************************************");


        while (true){
            System.out.print("Enter Ticket Release Rate (ms): ");
            String input = scanner.nextLine();
            try{
                int ticketReleaseRate = Integer.parseInt(input);
                if (ticketReleaseRate > 0 ){
                    setTicketReleaseRate(ticketReleaseRate);

                    break;
                }else{
                    System.out.println("Ticket release rate must be a positive integer! ");
                }
            }catch (NumberFormatException e){
                System.out.println("Error!, Invalid input!.. Please enter a positive integer! ");
            }
        }

        // Prompt for customerRetrievalRate

        while(true){
            System.out.print("Enter Customer Retrieval Rate (ms): ");
            String input = scanner.nextLine();
            try{
                int customerRetrivealRate = Integer.parseInt(input);
                if (customerRetrivealRate > 0 ){
                    setCustomerRetrievalRate(customerRetrivealRate);
                    break;
                }else{

                    System.out.println("Customer retrival rate must be a positive integer! \n");
                }
            }catch(NumberFormatException e){
                System.out.println("Error!, Invalid input!..Please enter a positive integer\n");

            }
        }

        // Prompt for maxTicketCapacity
        while(true){
            System.out.print("Enter Maximum Ticket Capacity : ");
            String input = scanner.nextLine();
            try{
                int maxTicketCap = Integer.parseInt(input);
                if(maxTicketCap > 0){
                    setMaxTicketCapacity(maxTicketCap);
                    break;
                }else{
                    System.out.println("Maximum ticket capacity must be a positive integer\n");
                }

            }catch(NumberFormatException e){
                System.out.println("Error!, Invalid input!..Please enter a positive integer\n");
            }

        }

        // Prompt for totalTickets
        while(true){
            System.out.print("Enter the Total Number of tickets " + "( <= " + getMaxTicketCapacity()+ ")  : "  );
            String input = scanner.nextLine();
            try{
                int totalTickets = Integer.parseInt(input);
                if(totalTickets > 0 && totalTickets <= getMaxTicketCapacity()){
                    setTotalTickets(totalTickets);
                    break;
                }else{
                    System.out.println("Total number of tickets must be a positive integer and not exceed maximum capacity (" + getMaxTicketCapacity() + ").\n");
                }
            }catch(NumberFormatException e){
                System.out.println("Error!, Invalid input!..Please enter a positive integer\n");
            }
        }

        // Prompt for initialTickets
        while(true){
            int maxInitialTickets = Math.min(getMaxTicketCapacity(),getTotalTickets());
            System.out.print("Enter the number of tickets to add to the Ticket Pool  " + " <= " + maxInitialTickets + "): ");
            String input = scanner.nextLine();
            try{
                int initialTickets = Integer.parseInt(input);
                if(initialTickets > 0 && initialTickets <= maxInitialTickets){
                    setInitialTickets(initialTickets);
                    break;
                }else{
                    System.out.print("Initial tickets (" + initialTickets + ") exceed maximum capacity (" + getMaxTicketCapacity() + "). Please try again!!\n");
                }
            }catch(NumberFormatException e){
                System.out.println("Error!, Invalid input!...Please enter a positive integer\n");
            }

        }

        // Prompt for number of vendors
        while(true){
            System.out.print("Enter the number of Vendors: ");
            String input = scanner.nextLine();
            try{
                int numOfVendors = Integer.parseInt(input);
                if(numOfVendors > 0){
                    setNumberOfVendors(numOfVendors);
                    break;
                }else{
                    System.out.println("Number of vendors must be a positive integer!\n");
                }
            }catch(NumberFormatException e){
                System.out.println("Error!, Invalid input!..Please enter a positive integer\n");
            }
        }

        //prompt for customers
        while(true){
            System.out.print("Enter the Number of Customers : ");
            String input = scanner.nextLine();
            try{
                int numOfCustomers = Integer.parseInt(input);
                if (numOfCustomers > 0){
                    setNumberOfCustomers(numOfCustomers);
                    break;
                }else{
                    System.out.println("Number of customers must be a positive integer!\n");
                }

            }catch(NumberFormatException e){
                System.out.println("Error!, Invalid input!..Please enter a positive integer\n");
            }
        }
        System.out.println("Configuration Completed. \n");
    }

    public void saveConfiguration(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(this, writer);
            System.out.println("Configuration saved to " + filename + "\n");
        } catch (IOException e) {
            System.out.println("Error saving configuration: " + e.getMessage() + "\n");
        }
    }

    public static Configuration loadConfiguration(String filename) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            Configuration config = gson.fromJson(reader, Configuration.class);
            System.out.println("Configuration loaded from " + filename);
            return config;
        } catch (IOException e) {
            System.out.println("Error loading configuration: " + e.getMessage());
            return null;
        }
    }

}
