package Community_Pantry_Management_System;

import java.util.regex.Pattern;

/**
 * Input and Validation component
 * Every method either returns a clean (trimmed / uppercased / parsed) value
 * or throws IllegalArgumentException with a message that is safe to show to the user
 * Nothing here touches stored data
 */

public final class Validator {
	private static final Pattern ITEM_ID    = Pattern.compile ("[A-Z][0-9]{3}");
    private static final Pattern REQUEST_ID = Pattern.compile ("Q[0-9]{3}");

    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 40;
    public static final int MIN_STOCK = 0;
    public static final int MAX_STOCK = 1000;
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 1000;

    private Validator () { }

    public static String validateItemId (String raw) {
        String id = clean (raw).toUpperCase ();
        if (!ITEM_ID.matcher (id).matches ()) {
            throw new IllegalArgumentException(
                    "Item ID must be one letter followed by three digits (e.g. R001)."
            		);
        }
        return id;
    }

    public static String validateRequestId (String raw) {
        String id = clean (raw).toUpperCase ();
        if (!REQUEST_ID.matcher (id).matches ()) {
            throw new IllegalArgumentException (
                    "Request ID must be the letter Q followed by three digits (e.g. Q001)."
            		);
        }
        return id;
    }

    public static String validateItemName (String raw) {
        String name = clean (raw);
        if (name.length () < MIN_NAME_LENGTH || name.length () > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException (
                    "Item name must be " + MIN_NAME_LENGTH + "-" + MAX_NAME_LENGTH + " characters after trimming."
                    );
        }
        return name;
    }

    public static int validateStock (String raw) {
        return parseInRange (raw, MIN_STOCK, MAX_STOCK, "Initial stock");
    }

    public static int validateQuantity (String raw) {
        return parseInRange(raw, MIN_QUANTITY, MAX_QUANTITY, "Quantity");
    }

    // Low-stock threshold: any non-negative whole number (0 is allowed)
    public static int validateThreshold (String raw) {
        String text = clean (raw);
        int value = parseWholeNumber (text, "Threshold");
        if (value < 0) {
            throw new IllegalArgumentException("Threshold must not be negative.");
        }
        return value;
    }

    private static int parseInRange(String raw, int min, int max, String field) {
        int value = parseWholeNumber(clean(raw), field);
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    field + " must be between " + min + " and " + max + ".");
        }
        return value;
    }

    // Rejects text, blanks, decimals such as 2.5, and numbers too large for an int
    private static int parseWholeNumber (String text, String field) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException (field + " must be a whole number.");
        }
    }

    private static String clean (String raw) {
        return raw == null ? "" : raw.trim ();
    }
}