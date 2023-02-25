package fr.uge.data;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeBoard {
  private List<Player> players;
  private int numberPlayer;
  
  private int haveSuperPosition;
  private int positionNeutralToken;
  private StateCaseTimeBoard[] timeBoard;
  private ArrayList<Patch> listTotalPatch;
  
  public TimeBoard() {
    this.haveSuperPosition = 1;
    this.timeBoard = new StateCaseTimeBoard[64];
    this.listTotalPatch = new ArrayList<Patch>();
    
    this.players = Arrays.asList(new Player(5, 0, new QuiltBoard()), new Player(5, 0, new QuiltBoard()));
    this.numberPlayer = 2;
  }
   
  
  /**
   * Initialize the quiltBoard of the 2 Players.
   */
  public void initialiazeQuiltBoardOfPlayers() {
    /*
    playerOne.quiltBoard().initializeQuiltBoard();
    playerTwo.quiltBoard().initializeQuiltBoard();
    */
    int i;
    for(i = 0; i < numberPlayer; i++) {
      players.get(i).quiltBoard().initializeQuiltBoard();
    }
  }
  

  /**
   * Remove button on the player.
   * 
   * @param player which player
   * @param price  the number of buttons
   */
  public void updateRetrieveButtonPlayer(int player, int price) {
    /*
    if(player == 1) {
      playerOne = playerOne.retrieveButton(price);
    } else {
      playerTwo = playerTwo.retrieveButton(price);
    }
    */
    players.set(player - 1, players.get(player - 1).retrieveButton(price));
  }

  
  /**
   * Added button on the player.
   * 
   * @param player which player
   * @param price  the number of buttons
   */
  public void updateAddButtonPlayer(int player, int price) {
    /*
    if(player == 1) {
      playerOne = playerOne.addButton(price);
    } else {
      playerTwo = playerTwo.addButton(price);
    }
    */
    players.set(player - 1, players.get(player - 1).addButton(price));
  }

  
  /**
   * Return a String according to caseBoard.
   * 
   * @param caseBoard type enum
   * @return          String according to caseBoard
   */
  public String getCaracterFromState(StateCaseTimeBoard caseBoard) {
    return switch (caseBoard) {
      case EMPTY -> "-";
      case BUTTON -> "o";
      case LEATHER -> "x";
      };
  }
  
  
  /**
   * Returns the player.
   * 
   * @param player  1 or 2 
   * @return        the Player
   */
  public Player getPlayer(int player) {
    /*
    if(player == 1) {
      return playerOne;
    }
    return playerTwo;*/
    return players.get(player - 1);
  }
  
  
  /**
   * Returns the position of the neutral token.
   * 
   * @return positionNeutralToken
   */
  public int getPositionNeutralToken() {
    return positionNeutralToken;
  }
  
  
  /**
   * Returns the fields haveSuperPosition.
   * 
   * @return haveSuperPosition
   */
  public int getSuperPositon() {
    return haveSuperPosition;
  }
  
  
  /**
   * Returns the fields timeBoard
   * 
   * @return timeBoard
   */
  public StateCaseTimeBoard[] getTimeBoard(){
    int i;
    
    StateCaseTimeBoard[] copyTimeBoard = new StateCaseTimeBoard[64];
    
    for(i = 0; i < 64; i++) {
      copyTimeBoard[i] = timeBoard[i];
    }

    return copyTimeBoard;
  }
  
  
  /**
   * Allows you to move the positionNeutralToken
   * 
   * @param numberCase the number of cases
   */
  public void moveToken(int numberCase) {
    positionNeutralToken += numberCase;
  }
  
  
  /**
   * Allows you to return to the beginning of the timeBoard if you reach the end.
   */
  public void updateNeutralToken() {
    if(listTotalPatch.size() != 0) {
      positionNeutralToken %= listTotalPatch.size();
    }
  }
  
  
  /**
   * Returns the size of the Patch list.
   * 
   * @return the size of listTotalPatch
   */
  public int getSizeTotalPatch() {
    return listTotalPatch.size();
  }
  
  
  /**
   * Given the Patch according to the index.
   * 
   * @param index  index of the Patch
   * @return       return the Patch
   */
  public Patch getPatchFromArray(int index) {
    return listTotalPatch.get(index);
  }
  
  /**
   * Allows you to read the file containing the timeBoard.
   * 
   * @param path          the timeBoard to the file
   * @throws IOException  
   */
  public void readTimeBoardFile(Path path) throws IOException {
    try(var reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
      int c;
      int i = 0;
      while ((c = reader.read()) != -1 && i < 64) {
        switch((char) c) {
          case '_':
            timeBoard[i] = StateCaseTimeBoard.EMPTY;
            break;
          
          case '-':
            timeBoard[i] = StateCaseTimeBoard.EMPTY;
            break;
          
          case 'o':
            timeBoard[i] = StateCaseTimeBoard.BUTTON;
            break;
          
          case 'x':
            timeBoard[i] = StateCaseTimeBoard.LEATHER;
            break;
          
          default:
            break;
         }
        i++;
      }
    } // appelle reader.close()
  }
  
  
  
  /**
   * Allows you to read the file that contains the Patches.
   * 
   * @param path          the patch to the file
   * @throws IOException
   */
  public void readPatchFile(Path path) throws IOException {
    try(var reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
      int c;
      int index = 0;
      var tabInformations = new int[3];
      
      var formPatch = new int[5][5];
      var i = 0;
      var j = 0;
      var precedentChar = ' ';
      boolean canIncrement = false;
      
      while ((c = reader.read()) != -1) {
        if(index == 3) {
          index = 0;
          i = 0;
        }
        if(j == 5) {
          j = 0;
        }
        if((char) c == ' ') {
          if(canIncrement == true || index == 0) {
            i++;
          }
          continue;
        }
        

        if((char) c >= '0' && (char) c <= '9') {
          tabInformations[index] = c - '0'; // Convert char into int
          index++;
        }

        if((char) c == 'x') {
          formPatch[j][i] = 1;
          canIncrement = true;
          i++;
        }
        
        if((char) c == '\n' && precedentChar == 'x') {
          i = 0;
          j++;
        }
        
        if((char) c == '\n' && precedentChar == '\n') {
          listTotalPatch.add(new Patch(tabInformations[0],
                                       tabInformations[1],
                                       tabInformations[2],
                                       formPatch));
          j = 0;
          i = 0;
          index = 0;
          formPatch = new int[5][5];
          canIncrement = false;
          
        }

        precedentChar = (char) c;
      }
    } // appelle reader.close()
  }
  
  
  /**
   * Duplicate the 2 Patches of version 1 in 20 of each.
   */
  public void duplicationVersionOne() {
    for(var i = 0; i < 20; i++) {
      listTotalPatch.add(listTotalPatch.get(0));
      listTotalPatch.add(listTotalPatch.get(1));
    }
  }
  
  
  /**
   * Remove the patch from the patch list (listTotalPatch).
   * 
   * @param  index  index of the Patch to remove
   * @return        the patch we removed
   */
  public Patch deletePatch(int index) {
    if (index < 0 || index > listTotalPatch.size()) {
      throw new IllegalArgumentException("Patch is out of range");
    }
    
    return listTotalPatch.remove(index);
  }
    
  /**
   * Checks if the player has exceeded a leather bonus
   * 
   * @param player
   * @param turnPlayer
   * @return 1 button, 2 leather, 3 button + leather bonus
   */
  public int checkTimeBoardButtonLeather(int turnPlayer) {
    int countBonus = 0;
      Player player = players.get(turnPlayer - 1);

      for(var i = player.positionChronos(); i > 0; i--) {
        if(timeBoard[i] == StateCaseTimeBoard.BUTTON) {
          player = player.addButton(player.quiltBoard().getProfitButton());
          timeBoard[i] =  StateCaseTimeBoard.EMPTY;
          if(countBonus == 2) {
            countBonus = 3;
          } else {
            countBonus = 1;
          }
        }
        
        if(timeBoard[i] == StateCaseTimeBoard.LEATHER) {
          timeBoard[i] =  StateCaseTimeBoard.EMPTY;
          
          if(countBonus == 1) {
            countBonus = 3;
          } else {
            countBonus = 2;
          }
        } // if
      }
      
      players.set(turnPlayer - 1, player);

    return countBonus;
  }
  
  
  /**
   * Change the position of the player's time token to the correct position
   * 
   * @param time
   * @param player
   */
  public void movePlayer(int time, int player) {
    haveSuperPosition = 0;
        
    if(players.get(0).positionChronos() + time == players.get(1).positionChronos()) {
      haveSuperPosition = 1;
    }
    if(players.get(1).positionChronos() + time == players.get(0).positionChronos()) {
      haveSuperPosition = 2;
    }
    
    players.set(player - 1, players.get(player - 1).newChonosPosition(time));
  }
  
  
  /**
   * get which player's turn who must play.
   * 
   * @return 1 = player 1, 2 = player 2
   */
  public int getTurnPlayer() {
    if (players.get(0).positionChronos() == players.get(1).positionChronos()) {
      if (haveSuperPosition == 1) { // Player one is on top of player two
        return 1;
      }
      return 2; // Player two is on top of player one
    }
    if (players.get(0).positionChronos() < players.get(1).positionChronos()) {
      return 1;
    } 
    return 2;
  }
  
  /**
   * Move the player to the next case of the other player
   * 
   * @param player
   */
  public void playerSurpassOtherPlayer(int player) {
    // var diffCase = Math.abs(playerOne.positionChronos() - playerTwo.positionChronos()) + 1;
    var diffCase = Math.abs(players.get(0).positionChronos() - players.get(1).positionChronos()) + 1;
    movePlayer(diffCase, player);
    players.set(player - 1,  players.get(player - 1).addButton(diffCase - 1));
  }

  
}
