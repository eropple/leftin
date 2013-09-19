package com.edropple.leftin.db

import com.typesafe.config.Config
import com.jolbox.bonecp.{BoneCPConfig, BoneCP}

import com.edropple.leftin.util.ConfigUtil._
import com.typesafe.scalalogging.slf4j.Logging
import com.googlecode.flyway.core.Flyway
import java.util.Properties

class Database(val name: String, private val url: String,
               private val username: String, private val password: String,
               private val options: DatabasePoolOptions = DatabasePoolOptions()) extends Logging {

    val pool: BoneCP = {
        logger.info("Creating database '{}'.", name);

        val cfg = new BoneCPConfig();

        cfg.setJdbcUrl(url);
        cfg.setUsername(username);
        cfg.setPassword(password);

        if (options.flywayManaged) {
            logger.info("Running Flyway for database '{}'.")
            val flyway = new Flyway();
            flyway.setDataSource(url, username, password);
            flyway.migrate();
        }
        cfg.setConnectionTimeoutInMs(options.connectionTimeout);

        cfg.setPartitionCount(options.partitionCount);
        cfg.setMinConnectionsPerPartition(options.minConnectionsPerPartition);
        cfg.setMaxConnectionsPerPartition(options.maxConnectionsPerPartition);
        cfg.setAcquireIncrement(options.acquireIncrement);
        cfg.setAcquireRetryDelayInMs(options.acquireRetryDelay);
        cfg.setAcquireRetryAttempts(options.acquireRetryAttempts);

        cfg.setLogStatementsEnabled(options.logSQLStatements);
        cfg.setTransactionRecoveryEnabled(options.enableTransactionRecovery);
        cfg.setDisableConnectionTracking(!options.enableConnectionTracking);

        options.driverProperties.foreach(props => {
            cfg.setDriverProperties(props);
        })

        val retval = new BoneCP(cfg);

        logger.info("'{}' created successfully.", name);

        retval;
    }

}

case class DatabasePoolOptions(val flywayManaged: Boolean = false,
                               val connectionTimeout: Long = 10000,
                               val partitionCount: Int = 1,
                               val minConnectionsPerPartition: Int = 1,
                               val maxConnectionsPerPartition: Int = 5,
                               val acquireIncrement: Int = 1,
                               val acquireRetryDelay: Long = 250,
                               val acquireRetryAttempts: Int = 3,
                               val logSQLStatements: Boolean = false,
                               val enableTransactionRecovery: Boolean = true,
                               val enableConnectionTracking: Boolean = true,
                               val queryTimeLimit: Long = 5000,
                               val driverProperties: Option[Properties] = None) {
    def this(c: Config) = this(
        configBoolean(c, "flyway", false),
        configMilliseconds(c, "connectionTimeout", 10000),
        configInt(c, "partitionCount", 1),
        configInt(c, "minConnectionsPerPartition", 1),
        configInt(c, "maxConnectionsPerPartition", 5),
        configInt(c, "acquireIncrement", 1),
        configMilliseconds(c, "acquireRetryDelay", 250),
        configInt(c, "acquireRetryAttempts", 3),
        configBoolean(c, "logSQLStatements", false),
        configBoolean(c, "enableTransactionRecovery", true),
        configBoolean(c, "enableConnectionTracking", true),
        configMilliseconds(c, "queryTimeLimit", 5000),
        configPropertiesOpt(c, "driverProperties")
    );
}


object Database {
    def create(name: String, config: Config): Database = {
        val dbConfigRoot = config.getConfig("db." + name);

        Class.forName(dbConfigRoot.getString("driver"));

        val url: String = dbConfigRoot.getString("url");
        val username: String = dbConfigRoot.getString("username");
        val password: String = dbConfigRoot.getString("password");

        val options = new DatabasePoolOptions(dbConfigRoot);

        return new Database(name, url, username, password, options);
    }
}
