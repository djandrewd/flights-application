package ua.danit.flights.data.impl.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ua.danit.flights.data.model.SeatClass;

/**
 * Holds information about seat class : type, name and description.
 *
 * @author Andrey Minov
 */
@Entity
@Table(name = "SEAT_CLASS")
public class MappedSeatClass implements SeatClass, Serializable {

  @Id
  private String type;
  private String name;
  private String description;

  @Override
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MappedSeatClass that = (MappedSeatClass) o;

    if (type != null ? !type.equals(that.type) : that.type != null) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    return description != null ? description.equals(that.description) : that.description == null;
  }

  @Override
  public int hashCode() {
    int result = type != null ? type.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
  }
}
