package io.github.lijolo.model.liquibase;

import liquibase.Scope;
import liquibase.changelog.ChangeLogParameters;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DatabaseChangelogCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateRunner {

  private String password;
  private String user;
  private String jdbcUrl;

  private List<String> changeLogFiles;

  public UpdateRunner() {
    this.setUpChangeLogFiles();
  }

  private void setUpChangeLogFiles() {
    this.changeLogFiles = new ArrayList<>();
    this.changeLogFiles.add("db/changelog/update/db.changelog-4.2.14.yaml");
  }

  public void execute()
      throws SQLException, LiquibaseException {
    System.out.println("Updater: >> execute start");
    System.out.println("Updater: >>");

    this.setUp();
    //    this.initSchema();
    for (String clf : this.changeLogFiles) {
      this.updateSchema(clf);
    }
    System.out.println("Updater: >> execute end");
    System.out.println("Updater: >>");
  }

  private void applyLiquibaseChangelist(Connection connection,
                                        String changelistClasspathResource)
      throws LiquibaseException {
    applyLiquibaseChangelist(connection,
                             changelistClasspathResource,
                             getClass().getClassLoader());
  }

  public void applyLiquibaseChangelist(Connection connection,
                                       String changelistClasspathResource,
                                       ClassLoader classLoader)
      throws LiquibaseException {
    System.out.println("Updater: >> execute applyLiquibaseChangelist for changelist " + changelistClasspathResource);
    System.out.println("Updater: >>");
    try (var database = DatabaseFactory.getInstance()
                                       .findCorrectDatabaseImplementation(new JdbcConnection(connection))) {
      Map<String, Object> scopeObjects = Map.of(Scope.Attr.database.name(),
                                                database,
                                                Scope.Attr.resourceAccessor.name(),
                                                new ClassLoaderResourceAccessor(classLoader));
      Scope.child(scopeObjects,
                  (Scope.ScopedRunner<?>) () -> new CommandScope("update").addArgumentValue(DbUrlConnectionArgumentsCommandStep.DATABASE_ARG,
                                                                                            database)
                                                                          .addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG,
                                                                                            changelistClasspathResource)
                                                                          .addArgumentValue(DatabaseChangelogCommandStep.CHANGELOG_PARAMETERS,
                                                                                            new ChangeLogParameters(database))
                                                                          .execute());
    } catch (LiquibaseException e) {
      throw e;
    } catch (Exception e) {
      //       AutoCloseable.close() may throw Exception
      throw new LiquibaseException(e);
    }
  }

  //  private void initSchema()
  //      throws SQLException, LiquibaseException {
  //    try (Connection connection = DriverManager.getConnection(this.jdbcUrl,
  //                                                             this.user,
  //                                                             this.password)) {
  //      System.out.println("Updater: >> execute createInitialSchema");
  //      System.out.println("Updater: >>");
  //      applyLiquibaseChangelist(connection,
  //                               "db/changelog/init/db.changelog-init.yaml");
  //    }
  //  }

  private void updateSchema(String clf)
      throws SQLException, LiquibaseException {
    try (Connection connection = DriverManager.getConnection(this.jdbcUrl,
                                                             this.user,
                                                             this.password)) {
      System.out.println("Updater: >> execute updateSchema -> " + clf);
      System.out.println("Updater: >>");
      applyLiquibaseChangelist(connection,
                               clf);
    }
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
                                             "makaniProd");
    String contexts = System.getProperty("contexts",
                                         "prod_update");
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
    System.out.println("Updater: >> contexts >>" + contexts + "<<");
    System.out.println("Updater: >>");
  }

}
