package fr.uge.main;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

import fr.uge.data.Automa;
import fr.uge.data.Patch;
import fr.uge.data.Player;
import fr.uge.data.QuiltBoard;
import fr.uge.data.TimeBoard;
import fr.uge.display.Console;
import fr.uge.display.Graphic;
import fr.umlv.zen5.ApplicationContext;

public class Game {
  
  
  /** Allow us to know if the next token is an Integer
   * 
   * @param scanner
   * @return
   */
  public static int checkIfNextIsInt(Scanner scanner) {
    Objects.requireNonNull(scanner);

    while(!scanner.hasNextInt()) {
      scanner = new Scanner(System.in);
      System.out.println("You must write a number !");
    }

    return scanner.nextInt();
  }
  
  /**
   * Allows the user to choose between the next 3 patch with the keyboard.
   * 
   * @param timeBoard  TimeBoard to get the neutral token position
   *                   and so the next three patch
   * @param player     To know if the current player has enought to pay
   * @return int       represent which patch user choose
   */
  public static int userChoosePatchControlInput(TimeBoard timeBoard, Player player, VersionGame version, ApplicationContext context) {
    Objects.requireNonNull(timeBoard);
    Objects.requireNonNull(player);
    Objects.requireNonNull(version);

    var scanner = new Scanner(System.in);
    if(version == VersionGame.BASIC || version == VersionGame.FULL) {
      System.out.println(Console.displayInformationsControlInput());
    }
    
    int value = checkIfNextIsInt(scanner);

    /* Saisie contrôlée */
    var indexPatch = value - 1 + timeBoard.getPositionNeutralToken();
    indexPatch %= timeBoard.getSizeTotalPatch();
    if (value == 0) {
      return value;
    }

    int max;
    if(timeBoard.getSizeTotalPatch() < 3) {
      max = timeBoard.getSizeTotalPatch();
    } else {
      max = 3;
    }

    while(value < 0 || value > max || player.haveButtonToPay(timeBoard.getPatchFromArray(indexPatch).priceButton()) != true) {
      if(version == VersionGame.BASIC || version == VersionGame.FULL) {
        System.out.println(Console.displayInformationsControlInput());
      }
      
      value = checkIfNextIsInt(scanner);
      indexPatch = value - 1 + timeBoard.getPositionNeutralToken();
      if (value == 0) {
        return 0;
      }
    }
    return value;
  }
  
  
  /**
   * Checks if the 2 players have arrived at the end and so if the game is over
   * 
   * @param timeBoard  TimeBoard to get the neutral token position
   * @return boolean   false if game is over, true if not
   */
  public static boolean gameNotFinish(TimeBoard timeBoard) {
    Objects.requireNonNull(timeBoard);

    return !(timeBoard.getPlayer(1).positionChronos() >= 60 &&
           timeBoard.getPlayer(2).positionChronos() >= 60);
  
  }
  
  
  /**
   * Allows you to know which player has the best score and therefore who has won
   * 
   * @param timeBoard  TimeBoard to get players
   * @return int        1 = player 1 win, 2 = player 2 win, 3 = draw
   */
  public static int determineWhichPlayerWin(TimeBoard timeBoard, VersionGame version, Automa automa) {
    Objects.requireNonNull(timeBoard);

    var gainTotalPlayerOne = timeBoard.getPlayer(1).calculFinalButton();
    var gainTotalPlayerTwo = timeBoard.getPlayer(2).calculFinalButton();
    int gainTotalAutoma = 0;
    
    if(version == VersionGame.AUTOMA) {
      gainTotalAutoma = automa.calculFinalButton();
      if(gainTotalPlayerOne - gainTotalAutoma > 0) { // Player 1 win
        return 1;
      }
      if(gainTotalPlayerOne - gainTotalAutoma < 0) { // Automa 4 win
        return 4;
      }
      return 3;
    }
    // Player 1 win
    if(gainTotalPlayerOne - gainTotalPlayerTwo > 0) {
      return 1;
    }
    // Player 2 win
    if(gainTotalPlayerOne - gainTotalPlayerTwo < 0) {
      return 2;
    }
    // Draw
    return 3;
  }
  
  
  /**
   * Allows the player to place the patch on the quilt board
   * 
   * @param player  Player to get the current quiltboard
   */
  public static void placePatchInTheQuiltBoard(Player player, VersionGame version, ApplicationContext context) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(version);

    player.quiltBoard().initializeQuiltBoard();
    player.quiltBoard().updateQuiltBoard();
    
    if(version == VersionGame.BASIC || version == VersionGame.FULL || version == VersionGame.AUTOMA) {
      Console.displayQuiltBoard(player.quiltBoard());
      while(player.quiltBoard().moveRotatePatch() == 0) {
        player.quiltBoard().initializeQuiltBoard();
        player.quiltBoard().updateQuiltBoard();
        Console.displayQuiltBoard(player.quiltBoard());
      }
    } 
    
    if(version == VersionGame.GRAPHIC) {
      Graphic.drawQuiltBoard(context, player.quiltBoard());
      while(GameManagerGraphic.graphicMoveRotatePatch(player, context) == 0) {
        player.quiltBoard().initializeQuiltBoard();
        player.quiltBoard().updateQuiltBoard();
        Graphic.drawQuiltBoard(context, player.quiltBoard());
      }
    }
}

  
  /**
   * Read parameter data files to initialize timeBoard
   * 
   * @param timeBoard
   * @param listPatchFile  String path to the file
   * @param timeBoardFile  String path to the file
   */
  public static void readFiles(TimeBoard timeBoard, String listPatchFile, String timeBoardFile) throws IOException {
    Objects.requireNonNull(timeBoard);
    Objects.requireNonNull(listPatchFile);
    Objects.requireNonNull(timeBoardFile);

    timeBoard.readPatchFile(Path.of(listPatchFile));
    timeBoard.readTimeBoardFile(Path.of(timeBoardFile));
  }
  
  
  /**
   * Allows to create a patch and the player to place a patch of leather on the quiltboard
   * 
   * @param player Which player will place the leather patch
   * @param bonus  To know if he can place it
   */
  public static void bonusCreateMoveLeatherPatch(Player player, int bonus, VersionGame version, ApplicationContext context) {
    Objects.requireNonNull(player);
    Objects.requireNonNull(version);

    if(bonus == 2 || bonus == 3) {
      var formLeather = new int[1][1];
      formLeather[0][0] = 1;
      player.quiltBoard().addPatch(new Patch(0, 0, 0, formLeather) );
      Game.placePatchInTheQuiltBoard(player, version, context);
    }
  }
  
  
  /**
   * This methode call other methods who update value of the timeBoard and allows user to place
   * patch on quiltBoard, retrive button, add button and check if he has right for a bonus.
   * 
   * @param timeBoard     TimeBoard
   * @param player        Player To get informations about the player like quiltboard.
   * @param selectedPatch int To know what patch the user choose.
   * @param turnPlayer    int To know which player is currently playing
   */
  public static void updateTimeBoard(TimeBoard timeBoard, Player player, int selectedPatch, int turnPlayer, VersionGame version, ApplicationContext context, Automa automa) {
    Objects.requireNonNull(timeBoard);
    Objects.requireNonNull(player);
    Objects.requireNonNull(version);

    // 2. Move the Neutral Token
    timeBoard.moveToken(selectedPatch - 1);
    timeBoard.updateNeutralToken();

    // 3. Pay for the Patch
    var patchChoose = timeBoard.deletePatch(timeBoard.getPositionNeutralToken());
    timeBoard.movePlayer(patchChoose.costTime(), turnPlayer);
    timeBoard.updateRetrieveButtonPlayer(turnPlayer, patchChoose.priceButton());
    player.quiltBoard().addPatch(patchChoose);
    
    // 4. Place the Patch on Your Quilt Board
    Game.placePatchInTheQuiltBoard(player, version, context);
    
    if(player.quiltBoard().checkBonusTile()) {
      timeBoard.updateAddButtonPlayer(turnPlayer, QuiltBoard.BONUS_TILE);
      
      if(version == VersionGame.AUTOMA && automa.getBonusTile() == 0) {
        System.out.println("AOUHFEIYHFUOJFEOIJFMOIE");
        timeBoard.updateAddButtonPlayer(turnPlayer, QuiltBoard.BONUS_TILE);
        Console.displayGainBonusTile(QuiltBoard.BONUS_TILE);
        automa.setBonusTile(-1);
      }
      
      if(version == VersionGame.BASIC || version == VersionGame.FULL || version == VersionGame.AUTOMA) {
        Console.displayGainBonusTile(QuiltBoard.BONUS_TILE);
      }
      if(version == VersionGame.GRAPHIC) {
        Graphic.drawGainBonusTile(context, QuiltBoard.BONUS_TILE);
      }
        
    }
    
    int bonusResultat = timeBoard.checkTimeBoardButtonLeather(turnPlayer);
    if(version == VersionGame.BASIC || version == VersionGame.FULL || version == VersionGame.AUTOMA) {
      Console.displayBonus(player, bonusResultat);
    }
    if(version == VersionGame.GRAPHIC) {
      Graphic.drawBonus(context, player, bonusResultat);
    }
    
    bonusCreateMoveLeatherPatch(player, bonusResultat, version, context);
    timeBoard.updateNeutralToken();
  }
  
  
  /**
   * Allow us to know what's next to play
   * 
   * @param timeBoard   TimeBoard
   * @return turnPlayer 1 if player 1 will play, 2 for player 2
   */
  public static int determinePlayer(TimeBoard timeBoard) {
    Objects.requireNonNull(timeBoard);
    
    int turnPlayer = timeBoard.getTurnPlayer();

    return turnPlayer;
  }
  
  
  /**
   * Allow the user to choose what patch he wants and to check if
   * he advance and so receive button.
   * 
   * @param timeBoard     TimeBoard
   * @param player        Player To get informations about the player like quiltboard.
   * @param turnPlayer    int To know which player is currently playing
   * @return turnPlayer 1 if player 1 wille player, 2 for player 2
   */
  public static void choosingPatch(TimeBoard timeBoard, Player player, int turnPlayer, VersionGame version, ApplicationContext context) {
    Objects.requireNonNull(timeBoard);
    Objects.requireNonNull(player);
    Objects.requireNonNull(version);

    var selectedPatch = 0;
    
    if(version == VersionGame.BASIC || version == VersionGame.FULL || version == VersionGame.AUTOMA) {
      selectedPatch =  Game.userChoosePatchControlInput(timeBoard, player, version, context);
    }
    if(version == VersionGame.GRAPHIC) {
      selectedPatch = GameManagerGraphic.graphicChoosePatchControlInput(timeBoard, player, context);
    }
    
    if (selectedPatch == 0){
      timeBoard.playerSurpassOtherPlayer(turnPlayer);
      int bonusResultat = timeBoard.checkTimeBoardButtonLeather(turnPlayer);
      
      if(version == VersionGame.BASIC || version == VersionGame.FULL || version == VersionGame.AUTOMA) {
        Console.displayBonus(player, bonusResultat);
      }
      if(version == VersionGame.GRAPHIC) {
        Graphic.drawBonus(context, player, bonusResultat);
      }
      
      
      bonusCreateMoveLeatherPatch(player, bonusResultat, version, context);
    } else {
      if(version == VersionGame.FULL) {
        System.out.println("Player " + 
            turnPlayer + " choose the " +
            selectedPatch  + " patch after neutral Token");
      }
      
      Game.updateTimeBoard(timeBoard, player, selectedPatch, turnPlayer, version, context, null);
    }
  }
  
  
  public static void choosePatchAutomata(TimeBoard timeBoard, Player player, int turnPlayer, Automa automa) {
    Objects.requireNonNull(timeBoard);
    Objects.requireNonNull(player);

    
    if(turnPlayer == 2) {
      var tabPatch = automa.getPatchFromTimeBoard(timeBoard);
      
      var patchAndButton = automa.AutomaChoosePatch(tabPatch, timeBoard.getPlayer(1).positionChronos());
      
      if(patchAndButton[0] == -1) {
        timeBoard.playerSurpassOtherPlayer(turnPlayer);
      } else {
        timeBoard.moveToken(patchAndButton[0] - 1);
        timeBoard.updateNeutralToken();
        
        var patchChoose = timeBoard.deletePatch(timeBoard.getPositionNeutralToken());
        timeBoard.movePlayer(patchChoose.costTime(), turnPlayer);
        automa.moveChonosPosition(patchChoose.costTime());
        automa.addPatch(patchChoose);
        
        int bonusResultat = timeBoard.checkTimeBoardButtonLeather(turnPlayer);
        if(bonusResultat == 1 || bonusResultat == 3) {
          automa.addButton(patchAndButton[1]);
        }
        
        automa.obtainBonusTile();
        timeBoard.updateNeutralToken();
        System.out.println("Automa choose the " +
                           patchAndButton[0] +
                           " patch after neutral Token");
        Console.displayPatch(patchChoose);
        
        // Game.updateTimeBoard(timeBoard, player, patchAndButton[0], turnPlayer, VersionGame.AUTOMATA, null, automa);
      }
      
    }

  }
  

  /**
   * Allows you to choose the deck and the difficulty of Automa.
   * 
   * @param automa Automa
   */
  public static void PatchWorkVersionAutoma(Automa automa) {
    Objects.requireNonNull(automa);

    Scanner input = new Scanner(System.in);
    
    System.out.println("Choose between :\n" + "1 : normal deck\n" + "2 : tacticaldeck");
    
    var value = Game.checkIfNextIsInt(input);
    while(value < 1 || value > 2) {
      value = Game.checkIfNextIsInt(input);
    }
    automa.setwhichDeck(value-1);
    
    System.out.println("Choose between :\n" + "1 : Intern: 1 space away\n"
                      + "2 : Apprentice: 9 space away\n"
                      + "3 : Fellow: 12 space away\n"
                      + "4 : Master: 15 space away\n"
                      + "5 : Legend: 18 space away");
    
    value = Game.checkIfNextIsInt(input);
    while(value < 1 || value > 5) {
      value = Game.checkIfNextIsInt(input);
    }
    
    automa.setlevelsOfDifficulty(value);
  }
  
  /**
   * The main loop of the game which calls all the essential methods for the smooth running of the game..
   * 
   * @param version Which version the users wants to play
   */
  public static void PatchWorkVersion(VersionGame version) throws IOException {
    Objects.requireNonNull(version);

    /* Initialisation */
    var timeBoard = new TimeBoard();
    var automa = new Automa();

    timeBoard.initialiazeQuiltBoardOfPlayers();

    if(version == VersionGame.BASIC) {
      Game.readFiles(timeBoard, "../Aoudia_Bakhti_Patchwork/src/fr/uge/files/PatchsOne.txt",
                                "../Aoudia_Bakhti_Patchwork/src/fr/uge/files/timeBoardOne.txt");
      timeBoard.duplicationVersionOne();
    } 
    if(version == VersionGame.FULL || version == VersionGame.GRAPHIC || version == VersionGame.AUTOMA) {
      Game.readFiles(timeBoard, "../Aoudia_Bakhti_Patchwork/src/fr/uge/files/PatchsTwo.txt",
                                "../Aoudia_Bakhti_Patchwork/src/fr/uge/files/timeBoardTwo.txt");
    }

    
    if(version == VersionGame.AUTOMA) {
      PatchWorkVersionAutoma(automa);
      if(automa.getWhichDeck() == 0) {
        automa.readAutomaCardsFile(Path.of("../Aoudia_Bakhti_Patchwork/src/fr/uge/files/AutomaCardsOne.txt"));        
      } else {
        automa.readAutomaCardsFile(Path.of("../Aoudia_Bakhti_Patchwork/src/fr/uge/files/AutomaCardsTwo.txt"));
      }
    }

    
    ApplicationContext context = null;
    if(version == VersionGame.GRAPHIC) {
      int turnPlayer = Game.determinePlayer(timeBoard);
      var player = timeBoard.getPlayer(turnPlayer);
      context = Graphic.drawScreen(player, timeBoard);
    }
    
    while(Game.gameNotFinish(timeBoard)) {
     
      // Determines which player will play 
      int turnPlayer = Game.determinePlayer(timeBoard);
      var player = timeBoard.getPlayer(turnPlayer);
      
      if(version == VersionGame.BASIC || version == VersionGame.FULL || version == VersionGame.AUTOMA) {
        Console.displayTimeBoard(timeBoard, version, automa);
        Console.displayTurnPlayer(turnPlayer, version);
        Console.displayInformationsGame(timeBoard, player, version);
      }
      
      if(version == VersionGame.GRAPHIC) {
        Graphic.drawAll(context, timeBoard, player, turnPlayer);
      }
       
      if(version == VersionGame.AUTOMA && turnPlayer == 2) { // bot
        Game.choosePatchAutomata(timeBoard, player, turnPlayer, automa);
      } else {
        Game.choosingPatch(timeBoard, player, turnPlayer, version, context); // joueur
      }
    } // while
    
    // End of the game we display the scores
    if(version == VersionGame.BASIC || version == VersionGame.FULL || version == VersionGame.AUTOMA) {
      Console.displayFinishGame(timeBoard, version, automa);
    }
    if(version == VersionGame.GRAPHIC) {
      Graphic.drawFinishGame(context, timeBoard);
    }
    
    
  }
  
}
