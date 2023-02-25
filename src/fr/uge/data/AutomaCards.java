package fr.uge.data;

public record AutomaCards(int virtualButton, int filterOne, int filterTwo, int filterThree, int addButton) {
  public AutomaCards {  
    if (virtualButton < 0) {
      throw new IllegalArgumentException("virtualButton < 0");
    }
    if (0 > filterOne && filterOne > 5) {
      throw new IllegalArgumentException("0 < filterOne < 5");
    }
    if (0 > filterTwo && filterTwo > 5) {
      throw new IllegalArgumentException("0 < filtertTwo < 5");
    }
    if (0 > filterThree && filterThree > 5) {
      throw new IllegalArgumentException("0 < filterThree < 5");
    }
    if (0 > addButton && addButton > 5) {
      throw new IllegalArgumentException("0 < addButton < 5");
    }
  }
}