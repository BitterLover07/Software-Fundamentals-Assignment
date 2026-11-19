package Community_Pantry_Management_System;

import java.util.ArrayList;
import java.util.List;

// Reporting component: builds text for the user; never changes stored data

public class Report_Service {
	private final Pantry_Storage storage;

    public Report_Service (Pantry_Storage storage) {
        this.storage = storage;
    }

    public String formatItem (Item item) {
        return String.format ("%-6s %-25s Stock: %d", item.getId (), item.getName (), item.getStock ());
    }

    public String formatRequest (Request r) {
        return String.format ("#%-3d %-6s Item: %-6s Qty: %-5d %s",
                r.getArrivalNumber (), r.getId (), r.getItemId (), r.getQuantity (), r.getStatus ());
    }

    public String formatResult (Process_Result p) {
        Request r = p.request ();
        return String.format ("%s (%s x%d) -> %s | %s | stock %d -> %d",
                r.getId (), r.getItemId (), r.getQuantity (), r.getStatus (),
                p.message (), p.stockBefore (), p.stockAfter ());
    }

    // Items whose stock is LESS THAN OR EQUAL TO the threshold
    public List <Item> lowStockItems (int threshold) {
        List <Item> low = new ArrayList <> ();
        for (Item item : storage.getItems ()) {
            if (item.getStock () <= threshold) {
                low.add (item);
            }
        }
        return low;
    }
}