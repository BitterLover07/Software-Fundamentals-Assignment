package Community_Pantry_Management_System;

import java.util.ArrayList;
import java.util.List;

/**
 * Processing component: FIFO processing and the stock decision.
 * Rule: fulfill only if the WHOLE quantity is available, otherwise REJECT and leave stock unchanged.
 */

public class Request_Processor {
	private final Pantry_Storage storage;

    public Request_Processor (Pantry_Storage storage) {
        this.storage = storage;
    }

    /**
     * Processes every pending request, oldest first.
     * Processed requests leave the queue but stay in the history with their new status,
     * so calling this again finds nothing to do and cannot issue stock twice.
     */
    
    public List <Process_Result> processAllPending () {
        List <Process_Result> results = new ArrayList <> ();
        while (storage.hasPending ()) {
            Request request = storage.pollNextPending ();
            if (request.getStatus () != Status.PENDING) {
                continue;                            // safety check: only PENDING is processed
            }
            results.add (processOne (request));
        }
        return results;
    }

    private Process_Result processOne (Request request) {
        Item item = storage.findItem (request.getItemId ());
        if (item == null) {                          // defensive: cannot happen while deletion is out of scope
            request.setStatus (Status.REJECTED);
            return new Process_Result (request, 0, 0, "Item not found");
        }
        int before = item.getStock ();
        if (item.hasEnough (request.getQuantity ())) {
            item.reduceStock (request.getQuantity ());
            request.setStatus (Status.FULFILLED);
            return new Process_Result (request, before, item.getStock(), "Request fulfilled");
        }
        request.setStatus (Status.REJECTED);          // stock untouched
        return new Process_Result (request, before, before, "Insufficient stock");
    }
}