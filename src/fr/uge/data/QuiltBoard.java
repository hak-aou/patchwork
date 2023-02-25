package fr.uge.data;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class QuiltBoard {
  
  public static int BONUS_TILE = 7;

  private final ArrayList<int[][]> patchForm2DList;
  private final StateCaseQuiltBoard[][] quiltBoard;
  private int profitButton;
  
  public QuiltBoard() {
    this.quiltBoard = new StateCaseQuiltBoard[9][9];
    this.patchForm2DList = new ArrayList<int[][]>();
  }

  public ArrayList<int[][]> getPatchList() {
    return patchForm2DList;
  }
  
  /**
   * Allows you to know the size of the quiltBoard.
   * 
   * @return  fields sizeQuiltBoard
   */
  public int getSize() {
    return quiltBoard.length;
  }
  
  /**
   * Allows you to find out the 2-dimensional list of the quiltBoard.
   * 
   * @return  fields quiltBoard
   */
  public StateCaseQuiltBoard[][] getQuiltBoard() {
    int i, j;
    int size = quiltBoard.length;

    StateCaseQuiltBoard[][] copyQuiltBoard = new StateCaseQuiltBoard[size][size];

    for(i = 0; i < size; i++) {
      for(j = 0; j < size; j++) {
        copyQuiltBoard[i][j] = quiltBoard[i][j];
      }
    }
    
    return copyQuiltBoard;
  }
  
  /**
   * Allows to know the number of button it on each Patch of the quiltBoard.
   * 
   * @return  fields profitButton
   */
  public int getProfitButton() {
    return profitButton;
  }
  
  /**
   * Allows to return a String according to state.
   * 
   * @param  state  type enum State 
   * @return        A String
   */
  public String getCaracterFromState(StateCaseQuiltBoard state) {
    return switch (state) {
      case EMPTY -> ". ";
      case PATCH -> "o ";
      case SUPERPOSITION -> "x ";
    };
  }
  
  /**
   * Initialize list to 2 dimension of quiltBoard to 0.
   */
  public void initializeQuiltBoard() {
    for(var i = 0; i < quiltBoard.length; i++) {
      for(var j = 0; j < quiltBoard.length; j++) {
        quiltBoard[i][j] = StateCaseQuiltBoard.EMPTY;
      }
    }
  }
  
  
  /**
   * Adds a piece to the list of pieces on the player board.
   * 
   * @param patch The patch to add
   */
  public void addPatch(Patch patch) {
    Objects.requireNonNull(patch, "pieces is null");
    
    profitButton += patch.numberProfitButton();
    patchForm2DList.add(patch.convertFormPatchTo2DList());
  }
   
  
  /**
   * Displays the player’s board on the terminal.
   */
  public void updateQuiltBoard() {
    for(var elem: this.patchForm2DList) {
      for(var i = 0; i < elem.length; i++) {
        var k = elem[i][0];
        var j = elem[i][1];
        
        if(quiltBoard[k][j] == StateCaseQuiltBoard.PATCH) {
          quiltBoard[k][j] = StateCaseQuiltBoard.SUPERPOSITION;
        } 
        if(quiltBoard[k][j] == StateCaseQuiltBoard.EMPTY){
          quiltBoard[k][j] = StateCaseQuiltBoard.PATCH;
        }
      }
    }
    
  }
  
  /**
   * Allows you to know how many case are empty in the quiltBoard
   * 
   * @return the number of the empty case
   */
  public int numberEmptyCase() {
    var resultats = 0;
    
    for(var i = 0; i < quiltBoard.length; i++) {
      for(var j = 0; j < quiltBoard.length; j++) {
        if(quiltBoard[i][j] == StateCaseQuiltBoard.EMPTY) {
          resultats++;
        }
      }
    }
    
    return resultats;
  }

  /**
   * Check if the position is in the quiltBoard.
   * 
   * @param  value  Value to check
   * @return        true if is in the quiltBoard, false otherwise
   */
  public boolean checkPosition(int valeur) {
    return !(valeur < 0 || valeur > quiltBoard.length - 1);
  }
  
  /**
   * Check if there is an overlap between the Patches in the quiltBoard
   * 
   * @return  true if is an overlap false, otherwise
   */
  public boolean checkSuperPosition() {
    for(var i = 0; i < quiltBoard.length; i++) {
      for(var j = 0; j < quiltBoard.length; j++) {
        if (quiltBoard[i][j] == StateCaseQuiltBoard.SUPERPOSITION) {
          return false;
        }
      }
    }
    return true;
  }

  
  /**
   * Check if in the given square is completely filled.
   * 
   * @param  size  square size
   * @param  lig   long start
   * @param  col   start of column
   * @return       true if is completely filled, false otherwise
   */
  public boolean isSquareFull(int size, int lig, int col) {
    for(var i = lig; i < size + lig; i++) {
      for(var j = col; j < size + col; j++) {
        if(quiltBoard[i][j] == StateCaseQuiltBoard.EMPTY) {
          return false;
        }
      }
    }
    return true;
  }
  
  /**
   * Check if there is a size 7 square that is completely filled.
   * 
   * @return  true if is completely filled, false otherwise
   */
  public boolean checkBonusTile() {
    for(var i = 0; i < 2; i++) {
      for(var j = 0; j < 2; j++) {
        if(isSquareFull(BONUS_TILE, i, j)) {
          return true;
        }
      }
    }
    return false;
  }  
  
  /**
   * Generic method which allows to move a Patch in the quiltBoard
   * according to the direction given by index and shift.
   * 
   * @param lastCoordinates  Patch coordinates
   * @param index            index between 0 or 1
   * @param shift            shift between 1 or -1
   */
  public void moveEachCase(int[][] lastCoordinates, int index, int shift) {
    var getOutTimeBoard = 0;
    
    for(var i = 0; i < lastCoordinates.length; i++) {
      var tmp = lastCoordinates[i][index];

      tmp += shift;
      if(checkPosition(tmp) == false) {
        getOutTimeBoard += 1;
      }
    }
    if(getOutTimeBoard == 0) {
      for(var i = 0; i < lastCoordinates.length; i++) {
        lastCoordinates[i][index] += shift;
      }
    }
  }
  
  
  public int maxVert(int[][] patchCoordinate) {
    int max = patchCoordinate[0][0];
    
    for(var i = 0; i < patchCoordinate.length; i++) {
      if(patchCoordinate[i][0] > max) {
        max = patchCoordinate[i][0];
      }
    }
    
    return max;
  }
  public int minHor(int[][] patchCoordinate) {
    int min = patchCoordinate[0][1];
    
    for(var i = 0; i < patchCoordinate.length; i++) {
      if(patchCoordinate[i][1] < min) {
        min = patchCoordinate[i][1];
      }
    }
    
    return min;
  }
  
  
  
  public void rotateRight(int[][] patchCoordinate) {
    int size = patchCoordinate.length;
    
    int pivotX = minHor(patchCoordinate);
    int pivotY = maxVert(patchCoordinate);
    
    for(var i = 0; i < size; i++) {
      
      int x1 = pivotY - patchCoordinate[i][0];
      int y1 = patchCoordinate[i][1] - pivotX;
      
      patchCoordinate[i][1] = x1; // colonne
      patchCoordinate[i][0] = y1; // ligne
    }
  }
  

  
  /**
   * Give the mirror position of the patch
   * 
   * @return
   */
  public void reverse(int[][] patchCoordinate) {
    int size = patchCoordinate.length;
    int[][] copyPatchCoordinate = new int[size][2];
    
    for(var i = 0; i < size; i++) {
      copyPatchCoordinate[i][0] = patchCoordinate[i][0];
      copyPatchCoordinate[i][1] = patchCoordinate[i][1];
    }
    
    for(var i = 0; i < size; i++) {
      patchCoordinate[i][0] = copyPatchCoordinate[size - i - 1][1];
      patchCoordinate[i][1] = copyPatchCoordinate[size - i - 1][0];
    }
    
    rotateRight(patchCoordinate);
  }
  
  public void rotateLeft(int[][] lastPatch) {
    reverse(lastPatch);
    rotateRight(lastPatch);
    reverse(lastPatch);
  }

  
  /**
   * Allows to move the Patch according to the user's input and to check the superposition when validating.
   * 
   * @return  1 if the Patch is at a good position, 0 otherwise
   */
  public int moveRotatePatch() {
    Scanner input = new Scanner(System.in);
    var lastCoordinates = patchForm2DList.get(patchForm2DList.size() - 1);
    
    switch( input.nextLine() ) { 
        case "z":
            moveEachCase(lastCoordinates, 0, -1);
            System.out.println("Up");
            break;
            
        case "q":
            moveEachCase(lastCoordinates, 1, -1);
            System.out.println("Left");
            break;
            
        case "s":
            moveEachCase(lastCoordinates, 0, 1);
            System.out.println("Down");
            break;
            
        case "d":
            moveEachCase(lastCoordinates, 1, 1);
            System.out.println("Right");
            break;
        
        case "a":
          System.out.println("Rotaion Left");
          rotateLeft(lastCoordinates);
          break;      
          
        case "e":
          System.out.println("Rotation Right");
          rotateRight(lastCoordinates);
          break;
          
        case "r":
          reverse(lastCoordinates);
          System.out.println("Reverse");
          break; 
            
        case "v":
          if (checkSuperPosition() == false) {
            System.out.println("Superposition");
            return 0;
          }
          System.out.println("Validation");
          return 1;
          
         default:
           break;
     }

    return 0;
  }



}
