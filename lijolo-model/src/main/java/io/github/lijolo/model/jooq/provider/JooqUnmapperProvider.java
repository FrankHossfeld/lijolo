package io.github.lijolo.model.jooq.provider;

import io.github.lijolo.model.dto.Person;
import io.github.lijolo.model.jooq.unmapper.PersonRecordUnmapper;
import org.jooq.Configuration;
import org.jooq.Record;
import org.jooq.RecordType;
import org.jooq.RecordUnmapper;
import org.jooq.RecordUnmapperProvider;
import org.jooq.impl.DefaultRecordUnmapper;

public class JooqUnmapperProvider
    implements RecordUnmapperProvider {

  private final Configuration configuration;

  public JooqUnmapperProvider(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public <E, R extends Record> RecordUnmapper<E, R> provide(Class<? extends E> type,
                                                            RecordType<R> recordType) {
    DefaultRecordUnmapper<E, R> defaultRecordUnmapper = new DefaultRecordUnmapper<>(type,
                                                                                    recordType,
                                                                                    configuration);
    if (type == Person.class) {
      return (RecordUnmapper<E, R>) new PersonRecordUnmapper();
    }
    return defaultRecordUnmapper;
  }
}
