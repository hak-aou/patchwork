package fr.uge.data;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Automa {
  private int whichDeck;
  private int levelsOfDifficulty;
  private int BonusTile;
  private int moneyButton;
  private int positionChronos;
  private final ArrayList<Patch> patchWithButton;
  private final ArrayList<Patch> patchWithoutButton;
  private final ArrayList<AutomaCards> normalDeck;
  private final ArrayList<AutomaCards> tacticalDeck;
  
  
  public Automa() {
    this.BonusTile = 0;
    this.moneyButton = 0;
    this.positionChronos = 0;
    this.patchWithButton = new ArrayList<Patch>();
    this.patchWithoutButton = new ArrayList<Patch>();
    this.normalDeck = new ArrayList<AutomaCards>();
    this.tacticalDeck = new ArrayList<AutomaCards>();
  }
  
  
  @Override
  public String toString() {
    return "Number Buttons : " + moneyButton +
           "\nChronos position : " + (positionChronos + 1) +
           "/64";
  }
  
  
  /**
   * Returns the value of levelsOfDifficulty.
   * 
   * @return levelsOfDifficulty
   */
  public int getLevelsOfDifficulty() {
    return levelsOfDifficulty;
  }
  
  
  /**
   * Returns the value of BonusTile.
   * 
   * @return BonusTile
   */
  public int getBonusTile() {
    return BonusTile;
  }
  
  
  /**
   * Set the value of BonusTile.
   * 
   * @param value
   */
  public void setBonusTile(int value) {
    BonusTile = value;
  }
  
  
  /**
   * Returns the value of whichDeck.
   * 
   * @return whichDeck
   */
  public int getWhichDeck() {
    return whichDeck;
  }
  
  
  /**
   * Set the value of whichDeck.
   * 
   * @param deck
   */
  public void setwhichDeck(int deck) {
    whichDeck = deck;
  }
  
  
  /**
   * Set the difficulty value.
   * 
   * @param difficulty
   */
  public void setlevelsOfDifficulty(int difficulty) {
    switch (difficulty) {
      case 1:
        levelsOfDifficulty = 1;
        break;
      
      case 2:
        levelsOfDifficulty = 9;
        break;
      
      case 3:
        levelsOfDifficulty = 12;
        break;
      
      case 4:
        levelsOfDifficulty = 15;
        break;
      
      case 5:
        levelsOfDifficulty = 18;
        break;
      
      default:
        break;
      }
  }
  
  
  /**
   * Lets check if Automa can recover the 7x7-Special Tile.
   */
  public void obtainBonusTile() {
    if(positionChronos >= (60-levelsOfDifficulty) && BonusTile == 0) {
      BonusTile = 1;
    }
  }
  
  
  /**
   * Add buttons to Automa.
   * 
   * @param buttons
   */
  public void addButton(int buttons) {
    moneyButton += buttons;
  }
  
  
  /**
   * Allows you to modify the position of the positionChronos.
   * 
   * @param numberMovement
   * @return               new position Chronos of Automa
   */
  public int moveChonosPosition(int numberMovement) {
    return positionChronos += numberMovement;
  }
  
  
  /**
   * Add the patch chosen by Automa to the correct list.
   * 
   * @param newPatch
   */
  public void addPatch(Patch newPatch) {
    Objects.requireNonNull(newPatch, "newPatch is null");
    
    if(newPatch.numberProfitButton() > 0) {
      patchWithButton.add(newPatch);
    } else {
      patchWithoutButton.add(newPatch);
    }
  }
  
  
  /**
   * Read the file containing the list of cards and
   * add it to the correct list according to the chosen deck.
   * 
   * @param path
   * @throws IOException
   */
  public void readAutomaCardsFile(Path path) throws IOException {
    Objects.requireNonNull(path, "path is null");
    
    int c, i = 0, tmp = 0;
    var tabInformations = new int[5];
    
    try(var reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))){
      while((c = reader.read()) != -1){
        if((char) c >= '0' && (char) c <= '9') {
          if(i == 0 && tmp == 1) {
            tabInformations[i] = 10;
          } else {
            tabInformations[i] = c - '0'; // Convert char into int
            tmp++;
          }
        }
        
        if((char) c == '\n') {
          i = 0;
          tmp = 0;
          if(whichDeck == 0) {
            normalDeck.add(new AutomaCards(tabInformations[0], tabInformations[1],
                                           tabInformations[2], tabInformations[3], tabInformations[4]));
          } else {
            tacticalDeck.add(new AutomaCards(tabInformations[0], tabInformations[1],
                                             tabInformations[2], tabInformations[3], tabInformations[4]));
          }
        }
        
        if((char) c == ' ') {
          i++;
        }        
      } // while
    } // try
  }
  
  
  /**
   * Displays the contents of the card list. Use for debugging.
   */
  public void PrintAutomaDeck() {
    for(var i = 0; i < 12; i++) {
      if (whichDeck == 0) {
        System.out.println(normalDeck.get(i));
      } else {
        System.out.println(tacticalDeck.get(i));
      }
    }
  }
  
  
  /**
   * Allows you to obtain a random card in one of the two card decks.
   * 
   * @return The random card
   */
  public AutomaCards getRandomAutomaCards() {
    Random rand = new Random();
    int numberRandom = rand.nextInt(3);
    
    if(whichDeck == 0) {
      return normalDeck.get(numberRandom); 
    } else {
      return tacticalDeck.get(numberRandom);
    }
  }
  
  
  /**
   * Allows you to choose which Patch so as not to overtake the other player.
   * Otherwise return 0.
   * 
   * @param one             costTime of the 1 Patch
   * @param two             costTime of the 2 Patch
   * @param three           costTime of the 2 Patch
   * @param positionPlayer  Chronos position from the other player
   * @return 1 if one is smaller than positionPlayer 
   *         2 if Two is smaller than positionPlayer
   *         3 if Three is smaller than positionPlayer
   *         otherwise return 0
   */
  public int filtercost(int one, int two, int three, int positionPlayer) {
    int tmp = positionChronos;
    int testOne = tmp + one;
    int testTwo = tmp + two;
    int testThree = tmp + three;
    
    if(testOne < positionPlayer) {
      return 1;
    }
    if(testTwo < positionPlayer) {
      return 2;
    }
    if(testThree < positionPlayer) {
      return 3;
    }
    return 0;
  }
  
  /**
   * Make a comparison between 3 int to know which is the biggest otherwise return 0.
   * 
   * @param one   the length of the 1 Patch or numberProfitButton
   * @param two   the length of the 2 Patch or numberProfitButton
   * @param three the length of the 3 Patch or numberProfitButton
   * @return 1 if One is bigger that 2 and 3
   *         2 if Two is bigger that 1 and 3
   *         3 if Three is bigger that 1 and 2
   *         otherwise return 0
   */
  public int filterLargestPatchOrMostButtons(int one, int two, int three) {
    if(one > two && one > three) {
      return 1;
    }
    if(two > one && two > three) {
      return 2;
    } 
    if(three > one && three > two) {
      return 3;
    }
    return 0;
  }
  
  
  /**
   * Check in the list of Patch which can be buy.
   * 
   * @param tabPatch   list of Patch
   * @param buttonCard button that Automa have
   * @return new list of Patch that Automa can buy
   */
  public ArrayList<Patch> checkIfHaveButton(ArrayList<Patch> tabPatch, int buttonCard) {
    Objects.requireNonNull(tabPatch, "tabPatch is null");
    
    var tabNewPatch = new ArrayList<Patch>();
    
    for(int i = 0; i < tabPatch.size(); i++) {
      if(tabPatch.get(i).priceButton() <= buttonCard) {
        tabNewPatch.add(tabPatch.get(i));
      }
    }
    
    return tabNewPatch;
  }
  
  
  /**
   * Apply the corresponding filter according to the list of Patches.
   * 
   * @param filter         
   * @param positionPlayer opposing player's position
   * @param tabPatch       list of Patch
   * @return               the position of the patch to take otherwise 0
   */
  public int applyfFilter(int filter, int positionPlayer, ArrayList<Patch> tabPatch) {
    Objects.requireNonNull(tabPatch, "tabPatch is null");
    
    if(tabPatch.size() == 2) {
      switch (filter) {
        case 1:
          return filtercost(tabPatch.get(0).costTime(), tabPatch.get(1).costTime(), -1, positionPlayer);
        
        case 2:
          return filterLargestPatchOrMostButtons(tabPatch.get(0).getSizePatchForm(), tabPatch.get(1).getSizePatchForm(), -1);
        
        case 3:
          return filterLargestPatchOrMostButtons(tabPatch.get(0).numberProfitButton(), tabPatch.get(1).numberProfitButton(), -1);
      }
    } else {
      switch (filter) {
        case 1:
          return filtercost(tabPatch.get(0).costTime(), tabPatch.get(1).costTime(), tabPatch.get(2).costTime(), positionPlayer);
        
        case 2:
          return filterLargestPatchOrMostButtons(tabPatch.get(0).getSizePatchForm(), tabPatch.get(1).getSizePatchForm(), tabPatch.get(2).getSizePatchForm());
        
        case 3:
          return filterLargestPatchOrMostButtons(tabPatch.get(0).numberProfitButton(), tabPatch.get(1).numberProfitButton(), tabPatch.get(2).numberProfitButton());
      }
    }
    return -1;
  }
  
  
  /**
   * Allows you to obtain the list of Patches to choose from.
   * 
   * @param timeBoard
   * @return list of Patch
   */
  public ArrayList<Patch> getPatchFromTimeBoard(TimeBoard timeBoard){
    Objects.requireNonNull(timeBoard, "timeBoard is null");
    
    var tabPatch = new ArrayList<Patch>();
    var positionNeutral = timeBoard.getPositionNeutralToken();
    int numberMax = 0;
    
    for(var i = positionNeutral; i < positionNeutral + 3; i++) {
      if(numberMax >= timeBoard.getSizeTotalPatch()) {
        return tabPatch;
      }
      tabPatch.add(timeBoard.getPatchFromArray(i%timeBoard.getSizeTotalPatch()));
      numberMax++;
    }
    
    return tabPatch;
  }
  
  
  /**
   * Apply the cards from one of the decks on the patch list.
   * 
   * @param tabPatch       list of Patch
   * @param positionPlayer opposing player's position
   * @return an array of int with the Patch chosen and the number of buttons on the cards added
   */
  public int[] AutomaChoosePatch(ArrayList<Patch> tabPatch, int positionPlayer) {
    Objects.requireNonNull(tabPatch, "tabPatch is null");
    
    AutomaCards card = getRandomAutomaCards();
    
    System.out.println("The chosen card :\nVirtual button :" + card.virtualButton() +
                       " filterOne : " + card.filterOne() +
                       " filterTwo : " + card.filterTwo() + " filterThree : " +
                       card.filterThree() + " addButton : " + card.addButton());
              
    var tabNewPatch = new ArrayList<Patch>();
    int testfilter;
    var res = new int[2];
    
    res[1] = card.addButton();
    tabNewPatch = checkIfHaveButton(tabPatch, card.virtualButton());

    if(tabNewPatch.size() == 0) {
      res[0] = -1;
      return res;
    }
    
    if(tabNewPatch.size() == 1) {
      res[0] = 1;
      return res;
    }
    
    testfilter = applyfFilter(card.filterOne(), positionPlayer, tabNewPatch);
    if(testfilter != 0) {
      res[0] = testfilter;
      return res;
    }
    
    testfilter = applyfFilter(card.filterTwo(), positionPlayer, tabNewPatch);
    if(testfilter != 0) {
      res[0] = testfilter;
      return res;
    }
    
    res[0] = 3;
    return res;
  }
  
  
  /**
   * Allows to obtain the number of button on each Patch of a list of Patch.
   * 
   * @param listPatch 
   * @return total number of buttons
   */
  public int getButtonFromPatchList(ArrayList<Patch> listPatch) {
    Objects.requireNonNull(listPatch, "listPatch is null");
    
    int result = 0;
    
    for(int i = 0; i < listPatch.size(); i++) {
      result += listPatch.get(i).numberProfitButton();
    }
    
    return result;
  }
  
  
  /**
   * Calculates the final score of Automa.
   * 
   * @return his score
   */
  public int calculFinalButton() {
    switch (levelsOfDifficulty) {
      case 1:
        if (BonusTile == 1) {
          return 7;
        } 
        return 0;
        
      case 9:
        if(BonusTile == 1) {
          return 7 + moneyButton;
        }
        return moneyButton;
        
      case 12:
        if(BonusTile == 1) {
          return 7 + moneyButton + patchWithButton.size();
        }
        return moneyButton + patchWithButton.size();
        
      case 15:
        if(BonusTile == 1) {
          return 7 + moneyButton + getButtonFromPatchList(patchWithButton);
        }
        return moneyButton + getButtonFromPatchList(patchWithButton);
        
      case 18:
        if(BonusTile == 1) {
          return 7 + moneyButton + patchWithButton.size() + getButtonFromPatchList(patchWithButton);
        }
        return moneyButton + patchWithButton.size() + getButtonFromPatchList(patchWithButton);
        
      default:
        return 0;
    }
  }
  
}