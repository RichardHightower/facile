package org.facile;


public class JSONException extends RuntimeException {
    
    static void handleException(Exception ex) {
        throw new JSONException(ex);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public JSONException() {
    }

    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable cause) {
        super(cause);
    }

}