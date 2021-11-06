/**
 * Represents the recording for the skier servlet post request.
 */
public class SkierServletPostRequest {
  private int resort_id;
  private int season;
  private int day;
  private int skier_id;



  /**
   * Construct the object instance.
   *
   * @param resort_id the id of the resort
   * @param season the season id
   * @param day the day id
   * @param skier_id the skier id
   */
  public SkierServletPostRequest(int resort_id, int season, int day, int skier_id) {
    if (resort_id <= 0 || season <= 0 || skier_id <= 0) {
      throw new IllegalArgumentException("The parameter should be larger than zero");
    }

    if (day < 1 || day > 366) {
      throw new IllegalArgumentException("The id of day is out of range [1, 366]");
    }

    this.resort_id = resort_id;
    this.season = season;
    this.day = day;
    this.skier_id = skier_id;
  }

  public int getResort_id() {
    return resort_id;
  }

  public int getSeason() {
    return season;
  }

  public int getDay() {
    return day;
  }

  public int getSkier_id() {
    return skier_id;
  }

}
