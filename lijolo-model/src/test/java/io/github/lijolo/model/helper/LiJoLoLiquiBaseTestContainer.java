package io.github.lijolo.model.helper;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;

import java.nio.file.Path;
import java.sql.Connection;

public class LiJoLoLiquiBaseTestContainer {

  public static void initFunction(Connection connection,
                                  Path directoryPath,
                                  String changeLogFile) {
    try {
      Database  database  = DatabaseFactory.getInstance()
                                           .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase(changeLogFile,
                                          new DirectoryResourceAccessor(directoryPath),
                                          database);
      liquibase.update(new Contexts("unit-test-data"),
                       new LabelExpression());
    } catch (Exception e) {
      System.out.println("Exception during initFunction -> " + e.getMessage());
    }

  }
}
