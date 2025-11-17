package io.github.lijolo.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person {

  private Integer         personNr;
  private String          name01;
  private String          name02;
  private String          name03;
  private boolean         kunde;
  private boolean         lieferant;
  private String          notes;
  private List<Anschrift> anschriften;

  public Person() {
    this.anschriften = new ArrayList<>();
  }

  public Integer getPersonNr() {
    return personNr;
  }

  public void setPersonNr(Integer personNr) {
    this.personNr = personNr;
  }

  public String getName01() {
    return name01;
  }

  public void setName01(String name01) {
    this.name01 = name01;
  }

  public String getName02() {
    return name02;
  }

  public void setName02(String name02) {
    this.name02 = name02;
  }

  public String getName03() {
    return name03;
  }

  public void setName03(String name03) {
    this.name03 = name03;
  }

  public boolean isKunde() {
    return kunde;
  }

  public void setKunde(boolean kunde) {
    this.kunde = kunde;
  }

  public boolean isLieferant() {
    return lieferant;
  }

  public void setLieferant(boolean lieferant) {
    this.lieferant = lieferant;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public List<Anschrift> getAnschriften() {
    return anschriften;
  }

  public void setAnschriften(List<Anschrift> anschriften) {
    this.anschriften = anschriften;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person person = (Person) o;
    return kunde == person.kunde &&
           lieferant == person.lieferant &&
           Objects.equals(personNr,
                          person.personNr) &&
           Objects.equals(name01,
                          person.name01) &&
           Objects.equals(name02,
                          person.name02) &&
           Objects.equals(name03,
                          person.name03) &&
           Objects.equals(notes,
                          person.notes) &&
           Objects.equals(anschriften,
                          person.anschriften);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personNr,
                        name01,
                        name02,
                        name03,
                        kunde,
                        lieferant,
                        notes,
                        anschriften);
  }

  @Override
  public String toString() {
    return "Person{" +
           "personNr=" +
           personNr +
           ", name01='" +
           name01 +
           '\'' +
           ", name02='" +
           name02 +
           '\'' +
           ", name03='" +
           name03 +
           '\'' +
           ", kunde=" +
           kunde +
           ", lieferant=" +
           lieferant +
           ", notes='" +
           notes +
           '\'' +
           ", anschriften=" +
           anschriften +
           '}';
  }
}
