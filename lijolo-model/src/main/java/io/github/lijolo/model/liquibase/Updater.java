package io.github.lijolo.model.liquibase;

import liquibase.exception.LiquibaseException;

import java.io.IOException;
import java.sql.SQLException;

public class Updater {

  public static void main(String[] args)
      throws ClassNotFoundException, IOException, SQLException, LiquibaseException {
    System.out.println("Updater: >> started");
    System.out.println("Updater: >>");

    UpdateRunner runner = new UpdateRunner();
    runner.execute();

    System.out.println("Updater: >>");
    System.out.println("Updater: >> Ende Updater");
  }
}
