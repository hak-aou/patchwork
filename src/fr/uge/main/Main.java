package fr.uge.main;

import java.io.IOException;
import java.util.Scanner;

public class Main {
  // PROJET V2
  public static void main(String[] args) throws IOException {
    Scanner input = new Scanner(System.in);
    
    var informations = "1 : The Basic\n" +
                       "2 : Full Game\n" +
                       "3 : Graphic Display\n" +
                       "4 : Automa\n";
    
    System.out.println("Choose between :\n" + informations);

    var value = Game.checkIfNextIsInt(input);
    switch(value) {
      case 1:
        System.out.println("Stage 1: The Basics");
        Game.PatchWorkVersion(VersionGame.BASIC);
        break;
      
      case 2:
        System.out.println("Phase 2: Full Game");
        Game.PatchWorkVersion(VersionGame.FULL);
        break;
      
      case 3:
        System.out.println("Phase 3: Graphic Display");
        Game.PatchWorkVersion(VersionGame.GRAPHIC);
        break;
        
      case 4:
        System.out.println("Phase 4: Automa");
        Game.PatchWorkVersion(VersionGame.AUTOMA);
        break;
      
      default:
        System.out.println("Choose between :\n" + informations);
        value = Game.checkIfNextIsInt(input);
        break;
    }
    
  }

}
