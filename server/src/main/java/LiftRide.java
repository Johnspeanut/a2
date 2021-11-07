/**
 * Represents an event for a ride, including the time and the lift id.
 */
public class LiftRide {
  private int time;
  private int lift_id;

  /**
   * Construct the lift ride based on the given time and lift_id.
   *
   * @param time the time of the ride
   * @param lift_id the id of the lift
   */
  public LiftRide(int time, int lift_id) {
    if (time <= 0 || lift_id <= 0) {
      throw new IllegalArgumentException("The parameter should be larger than zero");
    }

    this.time = time;
    this.lift_id = lift_id;
  }
}