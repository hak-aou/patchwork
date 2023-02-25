package fr.uge.data;

import java.util.Objects;

public record Player(int moneyButton, int positionChronos, QuiltBoard quiltBoard) {
  
  public Player {
    if (moneyButton < 0) {
      throw new IllegalArgumentException("argent_button < 0");
    }
    if (positionChronos < 0) {
      throw new IllegalArgumentException("position_chronos < 0");
    }
    
    Objects.requireNonNull(quiltBoard, "plateau is null");
  }
  
  @Override
  public String toString() {
    return "Number Buttons : " + moneyButton +
           "\nChronos position : " + (positionChronos + 1) +
           "/64";
  }

  /**
   * Allows you to add buttons to the player.
   * 
   * @param  buttons the number of button added
   * @return         A player with new moneyButton
   */
  public Player addButton(int buttons) {
    return new Player(moneyButton + buttons, positionChronos, quiltBoard);
  }
  
  /**
   * Allows you to remove buttons on the player.
   * 
   * @param  cost the number of button to remove
   * @return      A player with new moneyButton
   */
  public Player retrieveButton(int cost) {
    return new Player(moneyButton - cost, positionChronos, quiltBoard);
  }
  /**
   * Allows know if the player can buy a Patch
   * 
   * @param  cost how much does the patch cost
   * @return      true if he can buy it otherwise false
   */
  public boolean haveButtonToPay(int cost) {
    return moneyButton >= cost;
  }
  
  /**
   * Allows you to modify the position of the positionChronos 
   * 
   * @param  numberMovement how much we have to move it
   * @return                A player with new positionChronos
   */
  public Player newChonosPosition(int numberMovement) {
    return new Player(moneyButton, positionChronos + numberMovement, quiltBoard);
  }
  
  /**
   * Allows you to calculate the end to know who has won
   * 
   * @return  moneyButton - (the empty case of the quiltBoard * 2)
   */
  public int calculFinalButton() {
    return moneyButton - (quiltBoard.numberEmptyCase() * 2);
  }
  
}
