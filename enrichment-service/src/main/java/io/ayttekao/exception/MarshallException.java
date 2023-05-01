package io.ayttekao.exception;

public class MarshallException extends RuntimeException {
    public MarshallException(String msg)
    {
        super(msg);
    }

    public MarshallException(String msg, Throwable cause)
    {
        super(msg,cause);
    }
}
