package ua.danit.flights.data.impl.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ua.danit.flights.data.model.Plane;
import ua.danit.flights.data.model.PlaneSeats;
import ua.danit.flights.data.model.SeatClass;

/**
 * Aggregated entity for plane seats for classes.
 *
 * @author Andrey Minov
 */
@Entity
@Table(name = "PLANE_SEATS")
public class MappedPlaneSeats implements PlaneSeats, Serializable {
  @Id
  @ManyToOne(cascade = CascadeType.REFRESH)
  private MappedPlane plane;
  @Id
  @ManyToOne(cascade = CascadeType.REFRESH)
  private MappedSeatClass seatClass;

  @Column(name = "NUMBER")
  private int numberOfSeats;
  @Column(name = "START_ROW")
  private int startRow;
  @Column(name = "END_ROW")
  private int endRow;
  @Column(name = "ROW_LENGTH")
  private int seatsInRow;

  @Override
  public Plane getPlane() {
    return plane;
  }

  public void setPlane(MappedPlane plane) {
    this.plane = plane;
  }

  @Override
  public SeatClass getSeatClass() {
    return seatClass;
  }

  public void setSeatClass(MappedSeatClass seatClass) {
    this.seatClass = seatClass;
  }

  @Override
  public int getNumberOfSeats() {
    return numberOfSeats;
  }

  public void setNumberOfSeats(int numberOfSeats) {
    this.numberOfSeats = numberOfSeats;
  }

  @Override
  public int getStartRow() {
    return startRow;
  }

  public void setStartRow(int startRow) {
    this.startRow = startRow;
  }

  @Override
  public int getEndRow() {
    return endRow;
  }

  public void setEndRow(int endRow) {
    this.endRow = endRow;
  }

  @Override
  public int getSeatsInRow() {
    return seatsInRow;
  }

  public void setSeatsInRow(int seatsInRow) {
    this.seatsInRow = seatsInRow;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MappedPlaneSeats that = (MappedPlaneSeats) o;

    if (numberOfSeats != that.numberOfSeats) {
      return false;
    }
    if (startRow != that.startRow) {
      return false;
    }
    if (endRow != that.endRow) {
      return false;
    }
    if (seatsInRow != that.seatsInRow) {
      return false;
    }
    if (plane != null ? !plane.equals(that.plane) : that.plane != null) {
      return false;
    }
    return seatClass != null ? seatClass.equals(that.seatClass) : that.seatClass == null;
  }

  @Override
  public int hashCode() {
    int result = plane != null ? plane.hashCode() : 0;
    result = 31 * result + (seatClass != null ? seatClass.hashCode() : 0);
    result = 31 * result + numberOfSeats;
    result = 31 * result + startRow;
    result = 31 * result + endRow;
    result = 31 * result + seatsInRow;
    return result;
  }
}
