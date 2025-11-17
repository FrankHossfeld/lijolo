package io.github.lijolo.service;

import io.github.lijolo.model.dto.Anschrift;
import io.github.lijolo.model.dto.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonServiceTest
  extends AbstractServiceTest{

  private PersonService service;

  @BeforeAll
  public void beforeAll() {
  }

  @BeforeEach
  public void beforeEach() {
    this.service = new PersonService();
    this.setUpDataBaseConfiguration();
  }

  @Test
  void insert01()
      throws SQLException {
    Person model = new Person();
    model.setName01("test");
    model.setKunde(true);
    model.getAnschriften().add(new Anschrift("Test Strasse", "21a", "47110",  "Kölsches Wasser"));
    Person newModel = this.service.insert(model);
    assertNotNull(newModel);
    Person readModel = this.service.get(newModel.getPersonNr());
    assertNotNull(readModel);
    assertEquals(newModel.getName01(), readModel.getName01());
    readModel.getAnschriften().forEach(a -> assertNotNull(a.getPartnerNr()));
  }

}