package io.github.lijolo.model.jooq;

import io.github.lijolo.model.jooq.provider.JooqMapperProvider;
import io.github.lijolo.model.jooq.provider.JooqUnmapperProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.util.Locale;

public class DslContextProvider {

  public final static DslContextProvider INSTANCE = new DslContextProvider();

  private DslContextProvider() {
  }

  public DSLContext getDslContext(Connection connection) {
    DefaultConfiguration configuration = new DefaultConfiguration();
    configuration.set(this.createSettings());
    configuration.set(connection);
    configuration.set(SQLDialect.POSTGRES);
    configuration.set(new JooqMapperProvider());
    configuration.set(new JooqUnmapperProvider(configuration));
    return DSL.using(configuration);
  }

  private Settings createSettings() {
    Settings settings = new Settings();
    settings.setExecuteLogging(false);
    settings.setLocale(Locale.GERMANY);
    return settings;
  }
}
