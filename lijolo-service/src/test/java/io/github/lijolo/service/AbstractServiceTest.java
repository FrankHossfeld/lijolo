package io.github.lijolo.service;

import io.github.lijolo.model.helper.LiJoLoLiquiBaseTestContainer;
import io.github.lijolo.service.helper.FileUtils;
import io.github.lijolo.service.helper.LiJoLoPostgresqlContainer;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public abstract class AbstractServiceTest {

  private static LiJoLoPostgresqlContainer database;
  protected      int                       maxNumberOfRetires = 3;
  private        LocalDateTime             startTs;
  private String propertyDatabaseUrl;

  public AbstractServiceTest() {
  }

  private static void startDatabase(String databaseName,
                                    Path liquibaseDir,
                                    Path liquibaseDirForLoadingUnitTestData) {
    try {
      AbstractServiceTest.database = new LiJoLoPostgresqlContainer().withUsername("test")
                                                                    .withPassword("test")
                                                                    .withDatabaseName(databaseName);
      database.start();
      database.waitingFor(Wait.forHealthcheck());

      Connection connection01 = DriverManager.getConnection(database.getJdbcUrl(),
                                                            "test",
                                                            "test");
      LiJoLoLiquiBaseTestContainer.initFunction(connection01,
                                                liquibaseDir,
                                                "init/db.changelog-init.yaml");
      connection01.close();

      Connection connection02 = DriverManager.getConnection(database.getJdbcUrl(),
                                                            "test",
                                                            "test");
      LiJoLoLiquiBaseTestContainer.initFunction(connection02,
                                                liquibaseDirForLoadingUnitTestData,
                                                "db.changelog-root-unit-test.yaml");
      connection02.close();
    } catch (SQLException e) {
      System.out.println("SQLException during start of database -> " + e.getMessage());
    }
  }

  private static DSLContext createDSLContextInternal(Connection con) {
    Settings settings = new Settings();
    settings.setExecuteLogging(false);
    settings.setLocale(Locale.GERMANY);
    return DSL.using(con,
                     SQLDialect.POSTGRES,
                     settings);
  }

  @NotNull
  private static List<String> getExcludeTables() {
    return Arrays.asList("iolani_tfa_code",
                         "pool_test",
                         "tfa_code",
                         "databasechangeloglock",
                         "databasechangelog"
                         // liquibase
    );
  }

    private Path copyLiquibaseInitChangelogToTempDir() {
      try {
        Path tempDirectory = Files.createTempDirectory(Path.of(String.valueOf(Paths.get(System.getProperty("java.io.tmpdir")))),
                                                       "test");
        URL test = getClass().getClassLoader()
                             .getResource("db/changelog");
        URLConnection changelog = Objects.requireNonNull(getClass().getClassLoader()
                                                                   .getResource("db/changelog"))
                                         .toURI()
                                         .toURL()
                                         .openConnection();
//        FileUtils.copyJarResourcesRecursively(tempDirectory.toFile(),
//                                              changelog);
        FileUtils.copyResourcesRecursively(changelog.getURL(),tempDirectory.toFile());
        return tempDirectory;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    private Path copyLiquibaseForLoadingUnitTestDataChangelogToTempDir() {
      try {
        Path tempDirectory = Files.createTempDirectory(Path.of(String.valueOf(Paths.get(System.getProperty("java.io.tmpdir")))),
                                                       "test");
        URLConnection changelog = Objects.requireNonNull(getClass().getClassLoader()
                                                                                         .getResource("db/changelog-unit-test"))
                                                               .toURI()
                                                               .toURL()
                                                               .openConnection();
//        FileUtils.copyJarResourcesRecursively(tempDirectory.toFile(),
//                                              changelog);
        FileUtils.copyResourcesRecursively(changelog.getURL(),tempDirectory.toFile());
        return tempDirectory;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    protected void setUpDataBaseConfiguration() {
      String databaseName        = "lijolo-dev";
//      String propertyDatabaseUrl = System.getProperty("postgres.db.url");
  //    if (Objects.isNull(propertyDatabaseUrl) && Objects.isNull(AbstractServiceTest.database)) {
  //      LOGGER.info("Initialize database");
        this.propertyDatabaseUrl = bootstrapPostgresWithTestContainers(databaseName);
  //    } else if (!Objects.isNull(AbstractServiceTest.database)) {
  //      LOGGER.info("Reuse database: " + database.getJdbcUrl());
//        this.propertyDatabaseUrl = database.getJdbcUrl();
  //    }
  //    ConnectionPoolConfiguration configuration = new ConnectionPoolConfiguration();
  //    configuration.setDataBaseName(databaseName);
  //    configuration.setPoolImplementation("HikariCP");
  //    configuration.setJdbcUrl(propertyDatabaseUrl);
  //    configuration.setUserName(System.getProperty("postgres.user.name",
  //                                                 "test"));
  //    configuration.setPassword(System.getProperty("postgres.user.account.password",
  //                                                 "test"));
  //    configuration.setMaxConnections(30);
  //    configuration.setDataBaseDriverClass(System.getProperty("postgres.driver.class",
  //                                                            "org.postgresql.Driver"));
  //    configuration.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
  //    configuration.setTestQuery("select * from \"pool_test\"");
  //    ConnectionPoolConfigurator.get()
  //                              .register(configuration);
    }

  //  protected void setUpServiceContextMakani() {
  //    this.serviceContextMakani = ServiceContextCreatorMakani.create(AbstractServiceTest.MANDANT_NR,
  //                                                                   AbstractServiceTest.USER_ID);
  //
  //  }
  //
  //  protected void setUpServiceContextWai(RightContext rightContext) {
  //    this.serviceContextWai = ServiceContextCreatorWai.create(rightContext,
  //                                                             AbstractServiceTest.USER_ID);
  //
  //  }

    private String bootstrapPostgresWithTestContainers(String databaseName) {
      try {
        Path liquibaseDir                       = copyLiquibaseInitChangelogToTempDir();
        Path liquibaseDirForLoadingUnitTestData = copyLiquibaseForLoadingUnitTestDataChangelogToTempDir();

        if (Objects.isNull(AbstractServiceTest.database)) {
          startDatabase(databaseName,
                        liquibaseDir,
                        liquibaseDirForLoadingUnitTestData);
        }
        String propertyDatabaseUrl = AbstractServiceTest.database.getJdbcUrl();
        org.apache.commons.io.FileUtils.deleteDirectory(liquibaseDir.toFile());
        return propertyDatabaseUrl;
      } catch (IOException e) {
        System.out.println("SQLException during bootstrapPostgresWithTestContainers -> " + e.getMessage());
      }
      return null;
    }

  //  private List<String> getAllTables() {
  //    if (Objects.nonNull(tableNames)) {
  //      return tableNames;
  //    }
  //    Transaction transaction = TransactionFactory.get()
  //                                                .getTransaction();
  //    transaction.begin();
  //
  //    DSLContext create = createDSLContextInternal(transaction);
  //    Result<Record1<Object>> records = create.select(field("table_name"))
  //                                            .from(table("information_schema.tables"))
  //                                            .where(field("table_schema").eq("public")
  //                                                                        .and(field("table_type").eq("BASE TABLE")))
  //                                            .fetch();
  //
  //    tableNames = new ArrayList<>();
  //    for (Record table : records) {
  //      String tableName = table.get("table_name",
  //                                   String.class);
  //      tableNames.add(tableName);
  //    }
  //    transaction.close();
  //
  //    return tableNames;
  //  }
  //
  //  protected Transaction beginTransaction() {
  //    Transaction transaction = TransactionFactory.get()
  //                                                .getTransaction();
  //    transaction.begin();
  //    return transaction;
  //  }
  //
  //  protected void closeTransaction(Transaction transaction) {
  //    transaction.close();
  //  }
  //
  //  @BeforeEach
  //  public void startUp() {
  //    this.startTs = LocalDateTime.now();
  //  }
  //
  //  @AfterEach
  //  public void cleanUp() {
  //    List<String> exclude_tables = getExcludeTables();
  //    List<String> tableNames     = getAllTables();
  //
  //    boolean retry = false;
  //    for (int i = 0; i < this.maxNumberOfRetires; i++) {
  //      Transaction transaction = beginTransaction();
  //      for (String tableName : tableNames) {
  //        if (exclude_tables.contains(tableName)) {
  //          continue; // Skip tables not having insert_log_ts
  //        }
  //        try {
  //          List<Condition> conditionList = new ArrayList<>();
  //          conditionList.add(field("insert_log_ts").greaterOrEqual(this.startTs));
  //          createDSLContextInternal(transaction).delete(table(tableName))
  //                                               .where(conditionList)
  //                                               .execute();
  //          transaction.commit();
  //        } catch (Exception e) {
  //          retry = true;
  //          if (i >= this.maxNumberOfRetires) {
  //            LOGGER.info("");
  //            LOGGER.info("===========================================================================\"");
  //            LOGGER.info("cleanUp Exception: >>" + e.getMessage() + "<<");
  //            LOGGER.info("iteration: >>" + i + "<<");
  //            LOGGER.info("===========================================================================\"");
  //            LOGGER.info("");
  //          }
  //          transaction.rollback();
  //        }
  //      }
  //      transaction.close();
  //      if (!retry) {
  //        break;
  //      }
  //    }
  //  }

}
