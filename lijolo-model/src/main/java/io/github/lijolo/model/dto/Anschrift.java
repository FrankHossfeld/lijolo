package io.github.lijolo.model.dto;

import java.util.Objects;

public class Anschrift {

  private Integer anschriftNr;
  private Integer partnerNr;
  private String  strasse;
  private String  hausnummer;
  private String  plz;
  private String  ort;

  public Anschrift() {
  }

  public Anschrift(String strasse,
                   String hausnummer,
                   String plz,
                   String ort) {
    this.strasse    = strasse;
    this.hausnummer = hausnummer;
    this.plz        = plz;
    this.ort        = ort;
  }

  public Integer getAnschriftNr() {
    return anschriftNr;
  }

  public void setAnschriftNr(Integer anschriftNr) {
    this.anschriftNr = anschriftNr;
  }

  public Integer getPartnerNr() {
    return partnerNr;
  }

  public void setPartnerNr(Integer partnerNr) {
    this.partnerNr = partnerNr;
  }

  public String getStrasse() {
    return strasse;
  }

  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }

  public String getHausnummer() {
    return hausnummer;
  }

  public void setHausnummer(String hausnummer) {
    this.hausnummer = hausnummer;
  }

  public String getPlz() {
    return plz;
  }

  public void setPlz(String plz) {
    this.plz = plz;
  }

  public String getOrt() {
    return ort;
  }

  public void setOrt(String ort) {
    this.ort = ort;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Anschrift anschrift = (Anschrift) o;
    return Objects.equals(anschriftNr,
                          anschrift.anschriftNr) && Objects.equals(partnerNr,
                                                                   anschrift.partnerNr) && Objects.equals(strasse,
                                                                                                          anschrift.strasse) && Objects.equals(hausnummer,
                                                                                                                                               anschrift.hausnummer) && Objects.equals(plz,
                                                                                                                                                                                       anschrift.plz) && Objects.equals(ort,
                                                                                                                                                                                                                        anschrift.ort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anschriftNr,
                        partnerNr,
                        strasse,
                        hausnummer,
                        plz,
                        ort);
  }

  @Override
  public String toString() {
    return "Anschrift{" + "anschriftNr=" + anschriftNr + ", partnerNr=" + partnerNr + ", strasse='" + strasse + '\'' + ", hausnummer='" + hausnummer + '\'' + ", plz='" + plz + '\'' + ", ort='" + ort + '\'' + '}';
  }
}
