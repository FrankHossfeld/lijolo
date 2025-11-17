package io.github.lijolo.service;

import io.github.lijolo.model.jooq.DslContextProvider;
import org.jooq.DSLContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AbstractService {

  private String password;
  private String user;
  private String jdbcUrl;

  public AbstractService() {
    this.setUp();
  }

  protected Connection getConnection()
      throws SQLException {
    Connection con =  DriverManager.getConnection(this.jdbcUrl,
                                       this.user,
                                       this.password);
    con.setAutoCommit(false);
    con.setReadOnly(false);
    con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
    return con;
  }

  protected DSLContext getDslContext(Connection con) {
    return DslContextProvider.INSTANCE.getDslContext(con);
  }

  private void setUp() {
    String host = System.getProperty("host",
                                     "localhost");
    String port = System.getProperty("port",
                                     "5439");
    this.password = System.getProperty("password",
                                       "postgres");
    this.user     = System.getProperty("user",
                                       "postgres");
    String databaseName = System.getProperty("database",
                                             "lijolo-dev");
    this.jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s",
                                 host,
                                 port,
                                 databaseName);
    System.out.println("Updater: >> jdbcUrl >>" + this.jdbcUrl + "<<");
    System.out.println("Updater: >> host >>" + host + "<<");
    System.out.println("Updater: >> port >>" + port + "<<");
    System.out.println("Updater: >> user >>" + this.user + "<<");
    //    System.out.println("Updater: >> password >>" + password + "<<");
    System.out.println("Updater: >> databaseName >>" + databaseName + "<<");
    System.out.println("Updater: >>");
  }

}
