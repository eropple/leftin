package com.edropple.leftin.routing;

public class RoutingException extends RuntimeException
{
    public RoutingException()
    {
    }

    public RoutingException(String message)
    {
        super(message);
    }

    public RoutingException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RoutingException(Throwable cause)
    {
        super(cause);
    }

    public RoutingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
