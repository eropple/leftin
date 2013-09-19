package com.edropple.leftin.util

import scala.collection.JavaConverters._

import com.typesafe.config.Config
import java.util.Properties

object ConfigUtil {
    def configBoolean(config: Config, path: String, default: Boolean): Boolean =
        if (config.hasPath(path)) config.getBoolean(path) else default;

    def configString(config: Config, path: String, default: String): String =
        if (config.hasPath(path)) config.getString(path) else default;

    def configInt(config: Config, path: String, default: Int): Int =
        if (config.hasPath(path)) config.getInt(path) else default;

    def configMilliseconds(config: Config, path: String, default: Long): Long =
        if (config.hasPath(path)) config.getMilliseconds(path) else default;

    def configPropertiesOpt(config: Config, path: String): Option[Properties] = {
        if (!config.hasPath(path)) return None;

        val set = config.atPath(path).root().entrySet();

        val props = new Properties();

        set.asScala.foreach(pair => {
            props.put(pair.getKey, pair.getValue.unwrapped());
        });

        return Some(props);
    }
}
