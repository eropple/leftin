package com.edropple.leftin.routing;

import com.google.common.collect.Iterables;

/**
 * The interface for defining a set of routing entries for the
 * consuming application. Implemented in both JavaRoutingModule
 * and ScalaRoutingModule.
 *
 * @author  eropple
 * @since   17 Sep 2013
 */
public abstract class RoutingModule
{
    public abstract Iterable<RoutingEntry> getRoutingEntries();

    public RoutingModule and(final RoutingModule other)
    {
        final RoutingModule t = this;
        return new RoutingModule()
        {
            @Override
            public Iterable<RoutingEntry> getRoutingEntries()
            {
                return Iterables.concat(t.getRoutingEntries(), other.getRoutingEntries());
            }
        };
    }
}
