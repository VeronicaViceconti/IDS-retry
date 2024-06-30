package it.polimi.sw.TUI;
import com.vdurmont.emoji.EmojiParser;
import it.polimi.sw.*;
import it.polimi.sw.model.*;
import it.polimi.sw.model.Object;
import it.polimi.sw.view.TUI;

import java.util.ArrayList;
import java.util.Arrays;

public class TUITestOneCard {
    /**
     * test of the library.
     * @param
     */
      /*public static void main(String[] args) {
            String str = EmojiParser.parseToUnicode("Hello, World! :white_large_square:");
            System.out.println(str); // Should print: Hello, World! 🌎
        }*/

    static TUI tui=new TUI();
    static Corner corner1=new Corner(null,Resources.FUNGI,1,true,false);
    static Corner corner2=new Corner(null,null,2,false,false);
    static Corner corner3=new Corner(null,null,3,true,false);
    static Corner corner4=new Corner(null,null,4,true,false);

    static ArrayList <Corner> corners=new ArrayList<>(Arrays.asList(corner1,corner2,corner3,corner4));
    static ResourceCard resourceCard=new ResourceCard(corners,1,2,"red",false,false,false,2, Resources.FUNGI);


    static Corner corner2_1=new Corner(Object.INKWELL,null,1,true,false);
    static Corner corner2_2=new Corner(null,null,2,true,false);
    static Corner corner2_3=new Corner(null,null,3,true,false);
    static Corner corner2_4=new Corner(null,null,4,false,false);

    static Resources resources1=Resources.PLANT;
    static Resources resources2=Resources.PLANT;
    static Resources resources3=Resources.INSECT;
    static ArrayList<Resources> necessaryResources=new ArrayList<>(Arrays.asList(resources1,resources2,resources3));
    static ArrayList <Corner> corners_2=new ArrayList<>(Arrays.asList(corner2_1,corner2_2,corner2_3,corner2_4));
    static GoldCard goldCard=new GoldCard(corners_2,2,13,"green",false,false,false,2,Resources.PLANT,"MANUSCRIPT",necessaryResources);

    static Corner corner3_1=new Corner(null,Resources.FUNGI,1,true,false);
    static Corner corner3_2=new Corner(null,Resources.PLANT,2,true,false);
    static Corner corner3_3=new Corner(null,Resources.INSECT,3,true,false);
    static Corner corner3_4=new Corner(null,Resources.ANIMAL,4,true,false);
    static ArrayList<Corner> corners3= new ArrayList<>(Arrays.asList(corner3_1,corner3_2,corner3_3,corner3_4));
    static Resources resources3_1=Resources.ANIMAL;
    static Resources resources3_3=Resources.PLANT;
    static Resources resources3_2=Resources.INSECT;
    static ArrayList<Resources> permanentResources=new ArrayList<>(Arrays.asList(resources3_1,resources3_2,resources3_3));
    static InitialCard initialCard=new InitialCard(corners3,0,1,null,false,false,false,1,permanentResources);



        public static void main(String[] args){ //privato
 /*           tui.topSingleCard(resourceCard,true,true);
            System.out.println();

            tui.middleSingleCard(resourceCard);
            System.out.println();

            tui.bottomSingleCard(resourceCard,true,true);
            System.out.println();
            System.out.println();


            tui.topSingleCard(initialCard,true,true);
            System.out.println();

            tui.middleSingleCard(initialCard);
            System.out.println();

            tui.bottomSingleCard(initialCard,true,true);
            System.out.println();
            System.out.println();


            tui.topSingleCard(goldCard,true,true);
            System.out.println();

            tui.middleSingleCard(goldCard);
            System.out.println();

            tui.bottomSingleCard(goldCard,true,true);
            System.out.println();
            System.out.println();

*/
        }



}
