package com.edropple.leftin.routing;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Symbol;

import java.util.List;

public class JavaRoutingModule extends RoutingModule
{
    private final List<RoutingEntry> entries = Lists.newArrayList();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Iterable<RoutingEntry> getRoutingEntries()
    {
        return entries;
    }

    protected void $(HttpMethod method, String regex, Class<?> controllerClass, Symbol controllerMethod)
    {
        entries.add(RoutingEntry.$(method, regex, controllerClass, controllerMethod));
    }
}
