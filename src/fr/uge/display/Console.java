package fr.uge.display;

import fr.uge.data.Automa;
import fr.uge.data.Patch;
import fr.uge.data.Player;
import fr.uge.data.QuiltBoard;
import fr.uge.main.Game;
import fr.uge.main.VersionGame;
import fr.uge.data.TimeBoard;
import fr.uge.data.StateCaseQuiltBoard;
import fr.uge.data.StateCaseTimeBoard;

public class Console {

  /**
   * Display the timeBoard on the console
   * 1 represents the player 1
   * 2 represents the player 2
   * x represents leather patch
   * o represents bonus profit button
   * 
   * @param timeBoard TimeBoard
   */
  public static void displayTimeBoard(TimeBoard timeBoard, VersionGame version, Automa automa) {
    var displayBottom = new StringBuilder();
    var displayTop = new StringBuilder();
    displayBottom.append("\n");
    
    var superPosition = false;
    if(timeBoard.getPlayer(1).positionChronos() == timeBoard.getPlayer(2).positionChronos()) {
      superPosition = true;
    }
    
    int i;
    for(i = 0; i < timeBoard.getTimeBoard().length; i++) {
      if(superPosition == true && timeBoard.getPlayer(1).positionChronos() == i && timeBoard.getSuperPositon() == 1) {
        displayTop.append("1");
      } else if(superPosition == true && timeBoard.getPlayer(2).positionChronos() == i && timeBoard.getSuperPositon() == 2) {
        displayTop.append("2");
      }
      if(superPosition == true) {
        displayTop.append(" ");
      } 
      
      /* Display bottom */
      if(timeBoard.getPlayer(1).positionChronos() == i && timeBoard.getSuperPositon() != 1) {
        displayBottom.append("1");
      }
      else if(timeBoard.getPlayer(2).positionChronos() == i && timeBoard.getSuperPositon() != 2){
        displayBottom.append("2");
      }
      else if(i >= 60){
        displayBottom.append("_");
      } else {

        switch(timeBoard.getTimeBoard()[i]) {
            case EMPTY:
              displayBottom.append(timeBoard.getCaracterFromState(StateCaseTimeBoard.EMPTY));
              break;
            case BUTTON:
              displayBottom.append(timeBoard.getCaracterFromState(StateCaseTimeBoard.BUTTON));
              break;
            case LEATHER:
              displayBottom.append(timeBoard.getCaracterFromState(StateCaseTimeBoard.LEATHER));
              break;
             
            default:
               break;
        }
      }
    }
    
    displayTop.append(displayBottom);
    System.out.println(displayTop.toString());
    
    var displayBonusTile = new StringBuilder();
    for(i = 0; i < timeBoard.getTimeBoard().length; i++) {
      if(version == VersionGame.AUTOMA && (60 - automa.getLevelsOfDifficulty()) == i) {
        displayBonusTile.append("7");
      } else {
        displayBonusTile.append(" ");
      }
    }
    System.out.println(displayBonusTile.toString());
  }
  
  /**
   * Display the three next patch the users can choose
   * 
   * @param timeBoard TimeBoard
   */
  public static void displayNumberOfPatch(TimeBoard timeBoard, int numberPatch) {
    var positionNeutral = timeBoard.getPositionNeutralToken();

    int numberMax = 0;
    for(var i = positionNeutral; i < positionNeutral + numberPatch; i++) {
      if(numberMax >= timeBoard.getSizeTotalPatch()) {
        return;
      }
      
      System.out.println( (i + 1 - positionNeutral)  + " - " + timeBoard.getPatchFromArray(i%timeBoard.getSizeTotalPatch()).toString() );
      Console.displayPatch(timeBoard.getPatchFromArray(i % timeBoard.getSizeTotalPatch()));
      // Graphic.displayPatch(timeBoard.getPatchFromArray(i%timeBoard.getSizeTotalPatch()));
      System.out.println("");
      numberMax++;
    }
  }
  
  
  /**
   * Display the timeBoard on the console
   * o represent part of the patch
   * x represent superposition between two part of patchs
   * . represent an empty case of the quiltboard
   * 
   * @param quiltBoard QuiltBoard
   */
  public static void displayQuiltBoard(QuiltBoard quiltBoard) {
    for(var i = 0; i < quiltBoard.getSize(); i++) {
      for(var j = 0; j < quiltBoard.getSize(); j++) {
        
        switch(quiltBoard.getQuiltBoard()[i][j]) {
          case PATCH:
            System.out.print(quiltBoard.getCaracterFromState(StateCaseQuiltBoard.PATCH));
            break;
          
          case SUPERPOSITION:
            System.out.print(quiltBoard.getCaracterFromState(StateCaseQuiltBoard.SUPERPOSITION)); 
            break;
          
          case EMPTY:
            System.out.print(quiltBoard.getCaracterFromState(StateCaseQuiltBoard.EMPTY)); 
            break;
            
          default:
            break;
        }
        
      }
      System.out.println("");
    }
  }
  
  /**
   * Display the forms of the patch on the console
   * 
   * @param patch Patch
   */
  public static void displayPatch(Patch patch){
    for (var i = 0; i < patch.formPatch().length; i++) {
      
      /* On affiche pas les lignes vides */
      if(patch.patchHaveEmptyLine(patch.formPatch()[i])) {
        continue;
      }
      
      for (var j = 0; j < patch.formPatch()[i].length; j++) {
        if (patch.formPatch()[i][j] == 1) {
          System.out.print("o");
        } else {
          System.out.print(" ");
        }
      }
      
      System.out.println("");
    }
  }
  
  /**
   * Display instructions to the user on how use the keyboard to choose a
   * patch on the console
   * 
   * @param patch Patch
   */
  public static String displayInformationsControlInput() {
    return "- Input must be 1, 2, 3 to choose the Patch\n" +
           "or 0 to pass the rival player and get as many buttons as there are squares." +
           "\n\n- Check if you have enought button to pay the patch you want";
  }
  
  
  /**
   * Shows who won the game on the console
   * 
   * @param winner int
   */
  public static String displayTheWin(int winner) {
    if(winner == 1) {
      return "Player 1 won, congratulation !";
    }
    if(winner == 2) {
      return "Player 2 won, congratulation !";
    }
    if(winner == 4) {
      return "Automa won, congratulation !";
    }
    return "None of the players won, it's a draw !";
  }

  
  /**
   * Display the score of the players on the console
   * 
   * @param timeBoard TimeBoard
   */
  public static String displayScore(TimeBoard timeBoard, VersionGame version, Automa automa) {
    int gainTotalPlayerOne = timeBoard.getPlayer(1).calculFinalButton();
    int gainTotalPlayerTwo = timeBoard.getPlayer(2).calculFinalButton();
    int gainTotalAutoma = 0;
    
    if(version == VersionGame.AUTOMA) {
      gainTotalAutoma = automa.calculFinalButton();
      return "Player 1 score : " +
             Integer.toString(gainTotalPlayerOne) +
             "\nAutoma score : " +
             Integer.toString(gainTotalAutoma);
    }
    
    return "Player 1 score : " +
            Integer.toString(gainTotalPlayerOne) +
            "\nPlayer 2 score : " +
            Integer.toString(gainTotalPlayerTwo);
  }
  
  
  /**
   * Show that the game is over and it's time to know who win on the console
   * 
   * @param timeBoard TimeBoard
   */
  public static void displayFinishGame(TimeBoard timeBoard, VersionGame version, Automa automa) {
    System.out.println("Game is finish !");
    Console.displayTimeBoard(timeBoard, version, automa);
    System.out.println("Time to know who win !");

    
    var winner = Game.determineWhichPlayerWin(timeBoard, version, automa);
    System.out.println(Console.displayScore(timeBoard, version, automa));
    System.out.println(Console.displayTheWin(winner));
  }
  
  
  /**
   * Show the quiltboard and informations of player on the console
   * 
   * @param player Player to know the information of which player to display
   */
  public static void displayTimeBoardAndPlayer(TimeBoard timeBoard, Player player) {
    System.out.println("Neutral token position : " + (timeBoard.getPositionNeutralToken() + 1));
    System.out.println("Remainning patch on the Quiltboard : " + timeBoard.getSizeTotalPatch());
    System.out.println(player);
    System.out.println("");
  }
  
  
  /**
   * Display the three next patchs and the quilboard of the current player on the console
   * 
   * @param timeBoard TimeBoard
   * @param player Player to know which quiltboard to display
   */
  public static void displayPatchChoiceWithQuiltBoard(TimeBoard timeBoard, Player player, VersionGame version) {
    Console.displayNumberOfPatch(timeBoard, 10);
    if(version != VersionGame.AUTOMA || (version == VersionGame.AUTOMA && Game.determinePlayer(timeBoard) != 2) ) {
      System.out.println("Your QuiltBoard :");
      Console.displayQuiltBoard(player.quiltBoard());
    }
  }
  
  
  /**
   * Display informations about the game like timboard infos, player infos
   * and next patchs
   * 
   * @param timeBoard TimeBoard
   * @param player Player
   */
  public static void displayInformationsGame(TimeBoard timeBoard, Player player, VersionGame version) {
    displayTimeBoardAndPlayer(timeBoard, player);
    displayPatchChoiceWithQuiltBoard(timeBoard, player, version);
  }
  
  
  /**
   * Shows which bonus the player has earned
   * 
   * @param player Player
   * @param bonus int
   */
  public static void displayBonus(Player player, int bonus) {
    if(bonus == 1) {
      System.out.println("/***********************************************/");
      System.out.println("You found Button Income so gains some buttons : " + player.quiltBoard().getProfitButton());
      System.out.println("/***********************************************/");
    }
    if(bonus == 2) {
      System.out.println("/***********************************************/");
      System.out.println("You found Leather patch so place it on the quiltboad !");
      System.out.println("/***********************************************/");
    }
    if(bonus == 3) {
      System.out.println("/***********************************************/");
      System.out.println("You found Button Income so gains some buttons : " + player.quiltBoard().getProfitButton());
      System.out.println("You found Leather patch so place it on the quiltboad !");
      System.out.println("/***********************************************/");
    }
  }
  
  
  /**
   * Shows that the player win a bonus tile with his value
   * 
   * @param value int
   */
  public static void displayGainBonusTile(int value) {
    System.out.println("/***********************************************/");
    System.out.println("You won a bonus tile worth " + value);
    System.out.println("/***********************************************/");
  }

  public static void displayTurnPlayer(int turnPlayer, VersionGame version) {
    if(turnPlayer == 2 && version == VersionGame.AUTOMA) {
      System.out.println("\nIt's time to Automa to play !\n");
    } else {
      System.out.println("\nIt's time to the player " + turnPlayer + " to play !\n");      
    }
  }
  
}

