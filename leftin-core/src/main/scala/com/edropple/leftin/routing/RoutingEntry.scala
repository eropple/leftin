package com.edropple.leftin.routing

import java.util.regex.{PatternSyntaxException, Pattern}
import java.lang.reflect.Method
import com.typesafe.scalalogging.slf4j.Logging

case class RoutingEntry(val method: HttpMethod, val regex: Pattern,
                        val controllerClass: Class[_], val controllerMethod: Method);

object RoutingEntry extends Logging {
    def $(method: HttpMethod, regex: String, controllerClass: Class[_],
          controllerMethod: Symbol): RoutingEntry = {
        logger.info("resolving: {} \"{}\" - {}.{}", method.toString, regex,
                    controllerClass.getSimpleName, controllerMethod.name);

        try {
            val pattern: Pattern = Pattern.compile(regex);
            val realMethod: Method = controllerClass.getMethod(controllerMethod.name);

            return RoutingEntry(method, pattern, controllerClass, realMethod);
        } catch {
            case ex: PatternSyntaxException => {
                logger.error("INVALID REGEX: {}", regex);
                throw new RoutingException(ex);
            }
            case ex: NoSuchMethodException => {
                logger.error("NO METHOD FOUND: {}.{}", controllerClass.getSimpleName, controllerMethod.name);
                throw new RoutingException(ex);
            }
        }
    }
}
