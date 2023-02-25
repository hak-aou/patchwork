package fr.uge.main;

import fr.uge.data.Player;
import fr.uge.data.TimeBoard;
import fr.uge.display.Graphic;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;

public class GameManagerGraphic {
  
  /**
   * Collects user keys and allows to move and rotate patches on the quiltBoard
   * 
   * @param player
   * @param context
   * @return
   */
  public static int graphicMoveRotatePatch(Player player, ApplicationContext context) {
    Event event = context.pollOrWaitEvent(10);

    while(event == null) {
      event = context.pollOrWaitEvent(10);
    }
    var patchList = player.quiltBoard().getPatchList();
    var lastPatch = patchList.get(patchList.size() - 1);

    var action = event.getAction();
    
    if (action == Action.KEY_PRESSED) {

      switch(event.getKey()) {
        case Z:
          player.quiltBoard().moveEachCase(lastPatch, 0, -1);
          break;
          
        case S:
          player.quiltBoard().moveEachCase(lastPatch, 0, 1);
          break;
        
        case D:
          player.quiltBoard().moveEachCase(lastPatch, 1, 1);
          break;
        
        case Q:
          player.quiltBoard().moveEachCase(lastPatch, 1, -1);
          break;
          
        case A:
          player.quiltBoard().rotateLeft(lastPatch);
          break;
          
        case E:
          player.quiltBoard().rotateRight(lastPatch);
          break;
          
        case R:
          player.quiltBoard().reverse(lastPatch);
          break;
          
        case V:
          if(player.quiltBoard().checkSuperPosition() == false) {
            return 0;
          }
          return 1;
        
        default:
          break;
      }
    }
    
    // patchList.set(patchList.size() - 1, lastPatch);

    return 0;
    
  }

  
  /**
   * The player chooses the patches he wants or to pass his turn.
   * 
   * @param timeBoard
   * @param player
   * @param context
   * @return
   */
  public static int graphicChoosePatchControlInput(TimeBoard timeBoard, Player player, ApplicationContext context) {
    String text = "Input : A B C to choose red patchs\n" +
                  "X to pass the enemy player\n by one square." +
                  "And get the right \nnumber of buttons";
    Graphic.drawTextOnScreen(context, text, 25, 250);
  
    Event event = context.pollOrWaitEvent(10);

    while(event == null) {
      event = context.pollOrWaitEvent(10);
    }
    int number = -1;

    if (event.getAction() == Action.KEY_PRESSED) {
      switch(event.getKey()) {
        case A:
          number = 1;
          break;
          
        case B:
          number = 2;
          break;
        
        case C:
          number = 3;
          break;
          
        case X:
          number = 0;
          break;
          
        default:
          break;
      }
    }
    
    
    /* Saisie contrôlée */
    var indexPatch = number - 1 + timeBoard.getPositionNeutralToken();
    if(timeBoard.getSizeTotalPatch() > 0) {
      indexPatch %= timeBoard.getSizeTotalPatch();
    }
    if (number == 0) {
      return number;
    }

    int max;
    if(timeBoard.getSizeTotalPatch() < 3) {
      max = timeBoard.getSizeTotalPatch();
    } else {
      max = 3;
    }
    
    while(number < 0 || number > max || player.haveButtonToPay(timeBoard.getPatchFromArray(indexPatch).priceButton()) != true) {
      event = context.pollOrWaitEvent(10);

      while(event == null) {
        event = context.pollOrWaitEvent(10);
      }
      if (event.getAction() == Action.KEY_PRESSED) {
        number = -1;
        switch(event.getKey()) {
          case A:
            number = 1;
            break;
            
          case B:
            number = 2;
            break;
          
          case C:
            number = 3;
            break;
            
          case X:
            number = 0;
            break;
          
          default:
            break;
        }
      }
      
      indexPatch = number - 1 + timeBoard.getPositionNeutralToken();
      if (number == 0) {
        return 0;
      }
    }
    
    return number;
  }
  
}

