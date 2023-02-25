package fr.uge.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

import fr.uge.data.Patch;
import fr.uge.data.Player;
import fr.uge.data.QuiltBoard;
import fr.uge.data.TimeBoard;
import fr.uge.main.Game;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.ScreenInfo;

public class Graphic {
  
  private static ApplicationContext contextFinal;
  private static Font font = new Font("Arial", Font.PLAIN, 20);


  
  /** Initialize the window and then draw the quiltboard, timeboard and patches
   * 
   * @param player
   * @param timeBoard
   * @return
   */
  public static ApplicationContext drawScreen(Player player, TimeBoard timeBoard) {

    Application.run(Color.ORANGE, context -> {
      
      contextFinal = context;
      // get the size of the screen
      ScreenInfo screenInfo = context.getScreenInfo();
      float width = screenInfo.getWidth();
      float height = screenInfo.getHeight();
      
      drawTimeBoard(context, timeBoard);
      drawQuiltBoard(context, player.quiltBoard());
      drawPatchOnCirlce(context, timeBoard, width / 2, height / 2,
                        timeBoard.getSizeTotalPatch(), height / 2 -  50);
    });
    return contextFinal;
  }
  
  /** Draw the timeBoard at the top of the screen
   * 
   * @param context
   * @param timeBoard
   */
  public static void drawTimeBoard(ApplicationContext context, TimeBoard timeBoard) {
    int size_case = 20;
    int space = 3;
    
    ScreenInfo screenInfo = context.getScreenInfo();
    float width = screenInfo.getWidth();
    float height = screenInfo.getHeight();
    
    var superPosition = false;
    if(timeBoard.getPlayer(1).positionChronos() == timeBoard.getPlayer(2).positionChronos()) {
      superPosition = true;
    }
    
    for(var i = 0; i < timeBoard.getTimeBoard().length; i++) {    
      Color color;
      var x = i;
      switch(timeBoard.getTimeBoard()[i]) {
        case EMPTY:
          color = Color.GRAY;     
          break;
        
        case BUTTON:
          color = Color.BLUE;     
          break;
          
        case LEATHER:
          color = Color.RED;     
          break;
         
        default:
          color = Color.BLACK;     
          break;
      }

      int decalage = timeBoard.getTimeBoard().length * (size_case + space);

      var pos_x = width / 2 - decalage / 2 + x * (size_case + space);
      var pos_y = 25;
      
      context.renderFrame(graphics -> {
        graphics.setColor(color);
        graphics.fill(new Rectangle2D.Float(pos_x, pos_y,
                                            size_case, size_case));
      });
      

      if(superPosition == true && timeBoard.getPlayer(1).positionChronos() == i && timeBoard.getSuperPositon() == 1) {
        Graphic.drawTextOnScreen(context, "1", (int) pos_x, pos_y);
      } else if(superPosition == true && timeBoard.getPlayer(2).positionChronos() == i && timeBoard.getSuperPositon() == 2) {
        Graphic.drawTextOnScreen(context, "2", (int) pos_x , pos_y);
      }
      
      /* Display bottom */
      if(timeBoard.getPlayer(1).positionChronos() == i && timeBoard.getSuperPositon() != 1) {
        Graphic.drawTextOnScreen(context, "1", (int) pos_x, pos_y + size_case);
      }
      else if(timeBoard.getPlayer(2).positionChronos() == i && timeBoard.getSuperPositon() != 2){
        Graphic.drawTextOnScreen(context, "2", (int) pos_x, pos_y + size_case);
      }
      else if(i >= 60){
        context.renderFrame(graphics -> {
          graphics.setColor(Color.BLACK);
          graphics.fill(new Rectangle2D.Float(pos_x, pos_y,
                                              size_case, size_case));
        });
      } 
        
      
    }

  }
  
  /**
   * Draw the quilt board in the middle of the screen with the overlapping
   * patches in red, otherwise in blue
   * 
   * @param context
   * @param quiltBoard
   */
  public static void drawQuiltBoard(ApplicationContext context, QuiltBoard quiltBoard) {
    int size_case = 50;
    int space = 3;

    ScreenInfo screenInfo = context.getScreenInfo();
    float width = screenInfo.getWidth();
    float height = screenInfo.getHeight();
    
    for(var i = 0; i < quiltBoard.getSize(); i++) {
      for(var j = 0; j < quiltBoard.getSize(); j++) {
        Color color;
        var x = j;
        var y = i;
        
        switch(quiltBoard.getQuiltBoard()[i][j]) {
          case PATCH:
            color = Color.BLUE;     
            break;
          
          case SUPERPOSITION:
            color = Color.RED;   
            break;
          
          case EMPTY:
            color = Color.GRAY;     
            break;
            
          default:
            color = Color.BLACK;
            break;
        }
        
        int decalage = quiltBoard.getSize() * (size_case + space);

        var pos_x = width / 2 - decalage / 2 + x * (size_case + space);
        var pos_y = height / 2 - decalage / 2 + y * (size_case + space);

        context.renderFrame(graphics -> {
          graphics.setColor(color);
          graphics.fill(new Rectangle2D.Float(pos_x, pos_y,
                                              size_case, size_case));
        });
        
      } // for j
    } // for i
    
  }

    /** Draws the shape of a patch at the given position of the screen in black
     * 
     * @param context
     * @param patch
     * @param pos_x
     * @param pos_y
     * @param color
     */
  public static void displayPatch(ApplicationContext context, Patch patch, double pos_x, double pos_y, Color color) {
    int size_case = 10;
    int space = 2;
    
    var droite = 0;
    var haut = 0;

    // System.out.println(Arrays.deepToString(patch.formPatch()));
    
    for (var i = 0; i < patch.formPatch().length; i++) {
      /* On affiche pas les lignes vides */
      if(patch.patchHaveEmptyLine(patch.formPatch()[i])) {
        continue;
      }

      for (var j = 0; j < patch.formPatch()[i].length; j++) {
        var top = i;
        var right = j;

        if (patch.formPatch()[i][j] == 1) {
          context.renderFrame(graphics -> {
            graphics.setColor(color);
            graphics.fill(new Rectangle2D.Float((int) (pos_x + right * (size_case + space)),
                                                (int) (pos_y + top * (size_case + space)),
                                                size_case, size_case));
          });
        }
      }
      
    }
    
  }

  /**
   * Draw all patches in a circle around a given position.
   * 
   * @param context
   * @param timeBoard
   * @param center_x
   * @param center_y
   * @param number_patch
   * @param radius
   */
  public static void drawPatchOnCirlce(ApplicationContext context, TimeBoard timeBoard, double center_x, double center_y, int number_patch, double radius) {
    double angle, slice;
    double PI = Math.PI;
    
    /* Angle between each points */
    slice  = (PI * 2) / number_patch;
    
    for(var i = 0; i < number_patch; i++) {
        angle = slice * i;
        
        var x = radius * Math.cos(angle) + center_x;
        var y = radius * Math.sin(angle) + center_y;
        
        Color color;
        var positionNeutral = timeBoard.getPositionNeutralToken();

        var cond1 = positionNeutral % timeBoard.getSizeTotalPatch();
        var cond2 = (positionNeutral + 3) % timeBoard.getSizeTotalPatch();

        if( i >= cond1 && (i < cond2 || cond2 == 0 || cond2 == 1 || cond2 == 2) ) {
          color = Color.RED;
        } else {
          color = Color.BLACK;
        }
        
        var patch = timeBoard.getPatchFromArray(i);
        int sizeFont = font.getSize();
        int space = 30;
        
        displayPatch(context, patch, x, y, color);
        Graphic.drawTextOnScreen(context, "P:" + patch.numberProfitButton(), (int) x + space,(int) y);
        Graphic.drawTextOnScreen(context, "T:" + patch.costTime(), (int) x + space, (int) y + sizeFont);
        Graphic.drawTextOnScreen(context, "P:" + patch.priceButton(), (int) x + space, (int) y + (sizeFont * 2));

    }

}
  
  
  /**
   * Erase all items on the screen.
   * 
   * @param context
   */
  public static void cleanWindows(ApplicationContext context) {
    ScreenInfo screenInfo = context.getScreenInfo();
    float width = screenInfo.getWidth();
    float height = screenInfo.getHeight();
    
    context.renderFrame(graphics -> {
      graphics.setColor(Color.ORANGE);
      graphics.fill(new Rectangle2D.Float(0, 0, width, height));
    });
  }
  
  /**
   * Displays text on screen
   * 
   * @param context
   * @param text
   * @param x
   * @param y
   */
  public static void drawTextOnScreen(ApplicationContext context, String text, int x, int y) {
    int sizeFont = font.getSize();
    
    int nbLine = 0;
    
    for(String element : text.split("\n")) {
      var nb = nbLine;
      context.renderFrame(graphics -> {
        graphics.setFont(font);
        graphics.setColor(Color.BLACK);
        graphics.drawString(element, x, y + sizeFont * nb);
      });
      nbLine++;
    }

  }
  
  /**
   * Clear the window then draw one by one all the elements that make up the game on the graphics window.
   * Like timeBoard, the quiltBoard, the patches in circles and the text on the left side.
   * 
   * @param context
   * @param timeBoard
   * @param player
   * @param turnPlayer
   */
  public static void drawAll(ApplicationContext context, TimeBoard timeBoard, Player player, int turnPlayer) {
    int space = 3;
    int y = 75;

    int sizeFont = font.getSize();
    
    // get the size of the screen
    ScreenInfo screenInfo = context.getScreenInfo();
    float width = screenInfo.getWidth();
    float height = screenInfo.getHeight();
    cleanWindows(context);
    drawTimeBoard(context, timeBoard);
    drawQuiltBoard(context, player.quiltBoard());
    drawPatchOnCirlce(context, timeBoard, width / 2, height / 2,
                      timeBoard.getSizeTotalPatch(), height / 2 -  50);
    
    drawTextOnScreen(context, "The player  " + turnPlayer + " play !", 25, y);
    drawTextOnScreen(context, "Total patch : " + timeBoard.getSizeTotalPatch(), 25, y + (sizeFont * 2));
    drawTextOnScreen(context, "Nomber buttons player : " + player.moneyButton(), 25, y + (sizeFont * 3));
    
    Graphic.drawTextOnScreen(context, "Neutral token position : " + timeBoard.getPositionNeutralToken(), 25, y + (sizeFont * 4));
    Graphic.drawTextOnScreen(context, "Chronos position : " + player.positionChronos() + " / 64", 25, y + (sizeFont * 5));
  }


  public static void drawBonus(ApplicationContext context, Player player, int bonus) {
    if(bonus == 1) {
      Graphic.drawTextOnScreen(context, "You found Button Income so gains some buttons : " + player.quiltBoard().getProfitButton(),
                              25, 400);
    }
    if(bonus == 2) {
      Graphic.drawTextOnScreen(context, "You found Leather patch so \n place it on the quiltboad !",
                              25, 400);
    }
    if(bonus == 3) {
      Graphic.drawTextOnScreen(context, "You found Button Income so \n gains some buttons : " + player.quiltBoard().getProfitButton(),
                              25, 400);
      Graphic.drawTextOnScreen(context, "You found Leather patch so \n place it on the quiltboad !",
                              25, 430);
    }    
  }


  /**
   * Draw a message telling the player that he got a bonus tile.
   * 
   * @param context
   * @param value
   */
  public static void drawGainBonusTile(ApplicationContext context, int value) {
    Graphic.drawTextOnScreen(context, "You won a bonus tile worth " + value, 25, 450);
  }


  /**
   * Displays the end message and indicates who won
   * 
   * @param context
   * @param timeBoard
   */
  public static void drawFinishGame(ApplicationContext context, TimeBoard timeBoard) {
    cleanWindows(context);

    int fontSize = font.getSize();
    
    // get the size of the screen
    ScreenInfo screenInfo = context.getScreenInfo();
    float width = screenInfo.getWidth();
    float height = screenInfo.getHeight();
    
    drawTextOnScreen(context, "Game is finish !", (int) width / 2 - 50, (int) height / 2);
    
    int winner = Game.determineWhichPlayerWin(timeBoard, null, null);

    if(winner == 1) {
      drawTextOnScreen(context, "Player 1 won, congratulation !", (int) width / 2 - 100, (int) height / 2 + fontSize);
    } else if(winner == 2) {
      drawTextOnScreen(context, "Player 2 won, congratulation !", (int) width / 2 - 100, (int) height / 2 + fontSize);
    } else if(winner == 3) {
      drawTextOnScreen(context, "None of the players won, it's a draw !", (int) width / 2 - 100, (int) height / 2 + fontSize);
    }
    
  }
  
}
