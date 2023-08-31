package cc.robotdreams.automation.utils;

import cc.robotdreams.automation.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MySQLDriver {
    final private Logger logger = LogManager.getLogger( this.getClass( ));

    final private Connection _mysqlConnection;
    public MySQLDriver(){
        String url = String.format(
                "jdbc:mysql://%s:%s/%s",
                Config.MYSQL_HOST.value,
                Config.MYSQL_PORT.value,
                Config.MYSQL_DATABASE.value
        );
        String username = "opencart";
        String password = "opencart";
        try{
            logger.debug("Create mysql connection");
            this._mysqlConnection = DriverManager.getConnection(url, Config.MYSQL_USERNAME.value,Config.MYSQL_PASSWORD.value);
            Runtime.getRuntime().addShutdownHook(new Thread(MySQLDriver.this::close));
        } catch (SQLException e){
            String msg = "Could not connect to database";
            logger.error(msg + ": REASON"+ e.getMessage());
            throw new RuntimeException( "Could not connect to database",e );
        }

    }
    public ResultSet executeQuery(String query) {
        try {
            return this._mysqlConnection.createStatement().executeQuery(query);
        } catch (SQLException e) {
            String msg = "Could not execute query: \"" + query + "\"";
            logger.error(msg + ": REASON: " + e.getMessage());
            throw new RuntimeException(msg, e);
        }
    }

    public boolean execute(String query) {
        try {
            return this._mysqlConnection.createStatement().execute(query);
        } catch (SQLException e) {
            String msg = "Could not execute query: \"" + query + "\"";
            logger.error(msg + ": REASON: " + e.getMessage());
            throw new RuntimeException(msg, e);
        }
    }

    public PreparedStatement preparedStatement(String query) {
        try {
            return this._mysqlConnection.prepareStatement(query);
        } catch (SQLException e) {
            String msg = "Could not create prepared statement with query: \"" + query + "\"";
            logger.error(msg + ": REASON: " + e.getMessage());
            throw new RuntimeException(msg, e);
        }
    }
    final public void close() {
        try {
            this._mysqlConnection.close();
            logger.debug("Close mysql connection");
        } catch (SQLException e) { /* Ignore */ }
    }
}
