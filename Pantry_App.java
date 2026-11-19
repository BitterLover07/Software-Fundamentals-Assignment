package Community_Pantry_Management_System;

import java.util.List;
import java.util.Scanner;

/**
 * Main class: console menu for the Community Pantry Management System (Version 1)
 * It only reads input, calls the components and prints; the rules live in the components
 */

public class Pantry_App {
	private final Scanner in = new Scanner (System.in);
    private final Pantry_Storage storage = new Pantry_Storage ();
    private final Request_Processor processor = new Request_Processor (storage);
    private final Report_Service reports = new Report_Service (storage);

    public static void main (String [] args) {
        new Pantry_App ().run();
    }

    private void run () {
        System.out.println ("=== Community Pantry Management System (Version 1) ===");
        boolean running = true;
        while (running) {
            printMenu ();
            String choice = ask ("Enter choice: ");
            if (choice == null) {                    // input stream closed -> exit safely
                break;
            }
            switch (choice.trim ()) {
                case "1" -> registerItem ();
                case "2" -> findItem ();
                case "3" -> registerRequest ();
                case "4" -> processPending ();
                case "5" -> showHistory ();
                case "6" -> showLowStock ();
                case "7" -> showAllItems ();
                case "8" -> loadSampleData ();
                case "0" -> running = false;
                default  -> System.out.println ("Invalid choice. Please enter 0-8.");
            }
        }
        System.out.println ("Goodbye. All data held in memory has been discarded.");
    }

    private void printMenu () {
        System.out.println ();
        System.out.println ("1. Register item");
        System.out.println ("2. Find item");
        System.out.println ("3. Register request");
        System.out.println ("4. Process pending requests (FIFO)");
        System.out.println ("5. Display all request statuses / history");
        System.out.println ("6. List low-stock items");
        System.out.println ("7. Display all items");
        System.out.println ("8. Load assignment sample data");
        System.out.println ("0. Exit");
    }

    // ---------- menu actions ----------

    private void registerItem () {
        try {
            String id = Validator.validateItemId (ask ("Item ID: "));
            String name = Validator.validateItemName (ask ("Item name: "));
            int stock = Validator.validateStock (ask ("Initial stock (0-1000): "));
            Item item = storage.registerItem (id, name, stock);
            System.out.println ("Item registered: " + reports.formatItem (item));
        } catch (IllegalArgumentException e) {
            System.out.println ("Error: " + e.getMessage () + " Nothing was changed.");
        }
    }

    private void findItem () {
        try {
            String id = Validator.validateItemId (ask ("Item ID to find: "));
            Item item = storage.findItem (id);
            System.out.println (item == null ? "Item " + id + " not found." : reports.formatItem (item));
        } catch (IllegalArgumentException e) {
            System.out.println ("Error: " + e.getMessage ());
        }
    }

    private void registerRequest () {
        try {
            String id = Validator.validateRequestId (ask ("Request ID: "));
            String itemId = Validator.validateItemId (ask ("Item ID: "));
            int qty = Validator.validateQuantity (ask ("Quantity (1-1000): "));
            Request r = storage.registerRequest (id, itemId, qty);
            System.out.println ("Request registered: " + reports.formatRequest (r));
        } catch (IllegalArgumentException e) {
            System.out.println ("Error: " + e.getMessage () + " Nothing was changed.");
        }
    }

    private void processPending () {
        List <Process_Result> results = processor.processAllPending ();
        if (results.isEmpty ()) {
            System.out.println ("There are no pending requests to process.");
            return;
        }
        for (Process_Result p : results) {
            System.out.println (reports.formatResult (p));
        }
        System.out.println ("All pending requests have been processed.");
    }

    private void showHistory () {
        List <Request> history = storage.getHistory ();
        if (history.isEmpty ()) {
            System.out.println ("No requests registered yet.");
            return;
        }
        System.out.println ("--- Request history ---");
        for (Request r : history) {
            System.out.println (reports.formatRequest(r));
        }
    }

    private void showLowStock () {
        try {
            int threshold = Validator.validateThreshold (ask ("Low-stock threshold (0 or more): "));
            List <Item> low = reports.lowStockItems (threshold);
            System.out.println ("--- Items with stock <= " + threshold + " ---");
            if (low.isEmpty ()) {
                System.out.println("No items at or below this threshold.");
            }
            for (Item item : low) {
                System.out.println (reports.formatItem (item));
            }
        } catch (IllegalArgumentException e) {
            System.out.println ("Error: " + e.getMessage ());
        }
    }

    private void showAllItems () {
        if (storage.getItems ().isEmpty ()) {
            System.out.println ("No items registered yet.");
            return;
        }
        System.out.println ("--- All items ---");
        for (Item item : storage.getItems ()) {
            System.out.println (reports.formatItem(item));
        }
    }

    // Convenience for the demo: loads the dataset from the assignment brief
    private void loadSampleData () {
        try {
            storage.registerItem ("R001", "Rice packs", 10);
            storage.registerItem ("O001", "Oil bottles", 4);
            storage.registerRequest ("Q001", "R001", 3);
            storage.registerRequest ("Q002", "O001", 5);
            storage.registerRequest ("Q003", "R001", 7);
            System.out.println ("Sample data loaded: 2 items, 3 pending requests.");
        } catch (IllegalArgumentException e) {
            System.out.println ("Sample data not loaded: " + e.getMessage ());
        }
    }

    // ---------- input helper ----------

    // Prompts and returns the typed line, or null when there is no more input
    private String ask (String prompt) {
        System.out.print (prompt);
        return in.hasNextLine () ? in.nextLine () : null;
    }
}