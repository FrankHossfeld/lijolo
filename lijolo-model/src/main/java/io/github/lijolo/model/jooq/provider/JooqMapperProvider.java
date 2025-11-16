package io.github.lijolo.model.jooq.provider;

import io.github.lijolo.model.dto.Person;
import io.github.lijolo.model.jooq.mapper.PersonRecordMapper;
import io.github.lijolo.model.sql.tables.records.PersonRecord;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.RecordMapperProvider;
import org.jooq.RecordType;
import org.jooq.impl.DefaultRecordMapper;

public class JooqMapperProvider
    implements RecordMapperProvider {

  @Override
  @SuppressWarnings("unchecked")
  public <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> recordType,
                                                          Class<? extends E> type) {
    DefaultRecordMapper<R, E> defaultRecordMapper = new DefaultRecordMapper<>(recordType,
                                                                              type);

    // ==> data > accounting
    if (type == Person.class) {
      return (RecordMapper<R, E>) new PersonRecordMapper((DefaultRecordMapper<PersonRecord, Person>) defaultRecordMapper);
    }

    // Fall back to jOOQ's DefaultRecordMapper, which maps records onto
    // POJOs using reflection.
    return defaultRecordMapper;
  }
}
