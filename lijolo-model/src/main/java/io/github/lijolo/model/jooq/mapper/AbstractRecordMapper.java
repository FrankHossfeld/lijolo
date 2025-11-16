package io.github.lijolo.model.jooq.mapper;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DefaultRecordMapper;

public abstract class AbstractRecordMapper<R extends Record, E>
    implements RecordMapper<R, E> {

  protected DefaultRecordMapper<R, E> defaultRecordMapper;

  public AbstractRecordMapper(DefaultRecordMapper<R, E> defaultRecordMapper) {
    this.defaultRecordMapper = defaultRecordMapper;
  }
}
