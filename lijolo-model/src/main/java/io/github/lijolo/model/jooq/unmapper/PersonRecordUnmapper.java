package io.github.lijolo.model.jooq.unmapper;

import io.github.lijolo.model.dto.Person;
import io.github.lijolo.model.sql.tables.records.PersonRecord;
import org.jooq.exception.MappingException;

public class PersonRecordUnmapper
    extends AbstractRecordUnmapper<Person, PersonRecord> {

  @Override
  public PersonRecord unmap(Person source)
      throws MappingException {
    PersonRecord record = new PersonRecord();
    record.from(source);
    record.setName_01(source.getName01());
    record.setName_02(source.getName02());
    record.setName_03(source.getName03());
    return record;
  }
}
