package fr.uge.data;

import java.util.Objects;

public record Patch(int numberProfitButton, int costTime, int priceButton, int[][] formPatch) {
  
  public Patch {  
    if (numberProfitButton < 0) {
      throw new IllegalArgumentException("numberButtons < 0");
    }
    if (costTime < 0) {
      throw new IllegalArgumentException("costTime < 0");
    }
    if (priceButton < 0) {
      throw new IllegalArgumentException("priceButton < 0");
    }
    
    Objects.requireNonNull(formPatch, "formPatch is null");
  }
  
  @Override
  public String toString() {
    return "Profit : " + numberProfitButton +
         ", Time cost : " + costTime +
         ", Price : " + priceButton;
  }
  
  
  /**
   * Allows you to know if there is an empty line for the display.
   *  
   * @param tab 
   * @return    true if is empty line, otherwise false 
   */
  public boolean patchHaveEmptyLine(int[] tab) {
    for(var i = 0; i < tab.length; i++) {
      if(tab[i] == 1) {
        return false;
      }
    }
    
    return true;
  }
  
  
  /**
   * Allows to know the size of the list for the Patch.
   * 
   * @return list size
   */
  public int getSizePatchForm() {
    int nb = 0;
    
    for(var i = 0; i < formPatch.length; i++) {
      for(var j = 0; j < formPatch[i].length; j++) {
        if (formPatch[i][j] == 1) {
          nb++;
        }
      }
    }
    
    return nb;
  }
  
  /**
   * Convert the Patch format to a 2 dimensional list.
   * 
   * @return list that represents a Patch
   */
  public int[][] convertFormPatchTo2DList() {
    int tab[][] = new int[getSizePatchForm()][2];
    int taille = 0;
    int i,j;
    
    for(i = 0; i < formPatch.length; i++) {
      for(j = 0; j < formPatch[i].length; j++) {
        if (formPatch[i][j] == 1) {
          tab[taille][0] = i;
          tab[taille][1] = j;
          taille++;
        }
      }
    }
    
    return tab;
  }

  
}