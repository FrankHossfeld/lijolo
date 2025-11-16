package io.github.lijolo.model.jooq.mapper;

import io.github.lijolo.model.dto.Person;
import io.github.lijolo.model.sql.tables.PersonJooq;
import io.github.lijolo.model.sql.tables.records.PersonRecord;
import org.jooq.impl.DefaultRecordMapper;

import java.util.Objects;

public class PersonRecordMapper
    extends AbstractRecordMapper<PersonRecord, Person> {

  public PersonRecordMapper(DefaultRecordMapper<PersonRecord, Person> defaultRecordMapper) {
    super(defaultRecordMapper);
  }

  @Override
  public Person map(PersonRecord record) {
    Person pojo = defaultRecordMapper.map(record);
    if (Objects.nonNull(pojo)) {
      pojo.setName01(record.get(PersonJooq.PERSON.NAME_01));
      pojo.setName02(record.get(PersonJooq.PERSON.NAME_02));
      pojo.setName02(record.get(PersonJooq.PERSON.NAME_03));
    }
    return pojo;
  }
}
