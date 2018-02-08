package ua.danit.flights.data.impl.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ua.danit.flights.data.model.Plane;
import ua.danit.flights.data.model.PlaneSeats;

/**
 * Describe information about plane: number, classes, number of the seats, manifacturer.
 *
 * @author Andrey Minov
 */
@Entity
@Table(name = "PLANE")
public class MappedPlane implements Plane, Serializable {
  @Id
  private long id;
  private String type;
  @Column(name = "MANIFACTURER")
  private String manufacturer;
  @Column(name = "TOTAL_SEATS")
  private int totalSeats;
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  private List<MappedPlaneSeats> planeSeats;

  @Override
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  @Override
  public int getTotalSeats() {
    return totalSeats;
  }

  public void setTotalSeats(int totalSeats) {
    this.totalSeats = totalSeats;
  }

  @Override
  public List<PlaneSeats> getPlaneSeats() {
    Object v = planeSeats;
    return (List<PlaneSeats>) v;
  }

  public void setPlaneSeats(List<MappedPlaneSeats> planeSeats) {
    this.planeSeats = planeSeats;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MappedPlane that = (MappedPlane) o;

    if (id != that.id) {
      return false;
    }
    if (totalSeats != that.totalSeats) {
      return false;
    }
    if (type != null ? !type.equals(that.type) : that.type != null) {
      return false;
    }
    if (manufacturer != null ? !manufacturer.equals(that.manufacturer)
        : that.manufacturer != null) {
      return false;
    }
    return planeSeats != null ? planeSeats.equals(that.planeSeats) : that.planeSeats == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (manufacturer != null ? manufacturer.hashCode() : 0);
    result = 31 * result + totalSeats;
    result = 31 * result + (planeSeats != null ? planeSeats.hashCode() : 0);
    return result;
  }
}
