package io.github.lijolo.service;

import io.github.lijolo.model.dto.Anschrift;
import io.github.lijolo.model.dto.Person;
import io.github.lijolo.model.sql.tables.AnschriftJooq;
import io.github.lijolo.model.sql.tables.PersonJooq;
import io.github.lijolo.model.sql.tables.records.PersonRecord;
import io.github.lijolo.service.exception.DataNotFoundException;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectWhereStep;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonService
    extends AbstractService {

  public PersonService() {
    super();
  }

  public Person get(Integer key)
      throws SQLException {
    Connection con = super.getConnection();
    Optional<PersonRecord> optional = super.getDslContext(con)
                                           .selectFrom(PersonJooq.PERSON)
                                           .where(PersonJooq.PERSON.PERSON_NR.eq(key))
                                           .fetchOptional();
    if (optional.isPresent()) {
      Person model = optional.get()
                             .into(Person.class);
      model.setAnschriften(this.getAnschriftenFor(con, key));
//      this.getDslContext(con).
      con.close();
      return model;
    }
    throw new DataNotFoundException("Person with personNr >>" + key + "<< not found");
  }

  public Person insert(Person model)
      throws SQLException {
    Connection con = super.getConnection();
    PersonRecord newRecord = super.getDslContext(con).newRecord(PersonJooq.PERSON,
                                                        model);
    newRecord.insert();
    newRecord.refresh();
    Person newModel = newRecord.into(Person.class);
//    model.getAnschriften().forEach(a -> newModel.getAnschriften().add(this.insertAnschrift()));

    con.commit();
    con.close();
    return newModel;
  }




  private List<Anschrift> getAnschriftenFor(Connection con,
                                            Integer key) {
    List<Condition> conditionList = new ArrayList<>();
    conditionList.add(AnschriftJooq.ANSCHRIFT.PERSON_NR.eq(key));
    return super.getDslContext(con)
                .selectFrom(AnschriftJooq.ANSCHRIFT)
                .where(conditionList)
                .fetch()
                .into(Anschrift.class);
  }

  private SelectWhereStep<PersonRecord> createSqlForGet(DSLContext create) {
    return create.selectFrom(PersonJooq.PERSON);
  }

}
