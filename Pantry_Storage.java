package Community_Pantry_Management_System;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Storage component (in memory only for Version 1)
 *
 *  - ArrayList  items            : keeps items in registration order for display
 *  - HashMap    itemIndex        : item lookup by ID
 *  - ArrayList  history          : every request ever registered, never removed
 *  - HashMap    requestIndex     : duplicate-ID check / lookup by request ID
 *  - Queue      pending          : FIFO order of PENDING requests (LinkedList)
 *
 * The history, the index and the queue all hold the SAME Request objects, so a status change is visible everywhere
 *
 * Values arriving here are already validated by Validator
 * This class enforces the rules that depend on stored data: uniqueness and "item must exist"
 * A rejected registration changes nothing
 */

public class Pantry_Storage {
	private final List <Item> items = new ArrayList <> ();
    private final Map <String, Item> itemIndex = new HashMap <> ();
    private final List <Request> history = new ArrayList<>();
    private final Map <String, Request> requestIndex = new HashMap <> ();
    private final Queue <Request> pending = new LinkedList <> ();
    private int lastArrivalNumber = 0;

    // ---------- items ----------

    public Item registerItem (String id, String name, int stock) {
        if (itemIndex.containsKey (id)) {
            throw new IllegalArgumentException ("Item ID " + id + " already exists.");
        }
        Item item = new Item (id, name, stock);
        items.add (item);
        itemIndex.put (id, item);
        return item;
    }

    // Returns the item, or null if the ID is unknown
    public Item findItem (String id) {
        return itemIndex.get (id);
    }

    public List <Item> getItems () {
        return Collections.unmodifiableList (items);
    }

    // ---------- requests ----------

    public Request registerRequest (String id, String itemId, int quantity) {
        if (requestIndex.containsKey (id)) {
            throw new IllegalArgumentException(
                    "Request ID " + id + " has already been used in this session.");
        }
        if (!itemIndex.containsKey (itemId)) {
            throw new IllegalArgumentException ("Item ID " + itemId + " does not exist.");
        }
        lastArrivalNumber++;                         // only after every check has passed
        Request request = new Request (id, itemId, quantity, lastArrivalNumber);
        history.add (request);
        requestIndex.put (id, request);
        pending.add (request);                        // rear of the queue
        return request;
    }

    public boolean hasPending () {
        return !pending.isEmpty ();
    }

    // Removes and returns the oldest pending request (front of the queue), or null
    public Request pollNextPending () {
        return pending.poll ();
    }

    public int pendingCount () {
        return pending.size ();
    }

    public List <Request> getHistory () {
        return Collections.unmodifiableList (history);
    }
}