package it.polimi.sw.TUI;

import it.polimi.sw.model.*;
import it.polimi.sw.model.Object;
import it.polimi.sw.view.TUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TUITestManuscriptLongX {
        static TUI tui=new TUI();

        static Corner cornerg_1=new Corner(Object.INKWELL,null,1,true,false);
        static Corner cornerg_2=new Corner(null,null,2,true,false);
        static Corner cornerg_3=new Corner(null,null,3,true,false);
        static Corner cornerg_4=new Corner(null,null,4,false, false);

        static Resources resources1=Resources.PLANT;
        static ArrayList<Resources> necessaryResources=new ArrayList<>(Arrays.asList(resources1,resources1,resources1));
        static ArrayList <Corner> cornersg1=new ArrayList<>(Arrays.asList(cornerg_1,cornerg_2,cornerg_3,cornerg_4));
        static GoldCard goldCard=new GoldCard(cornersg1,2,13,"green",false,false,false,2,Resources.PLANT,"MANUSCRIPT",necessaryResources);
        static GoldCard goldCard1=new GoldCard(cornersg1,2,13,"green",false,false,false,2,Resources.PLANT,"MANUSCRIPT",necessaryResources);

        static GoldCard goldCard2=new GoldCard(cornersg1,2,13,"green",false,false,false,2,Resources.PLANT,"MANUSCRIPT",necessaryResources);
        static GoldCard goldCard3=new GoldCard(cornersg1,2,13,"green",false,false,false,2,Resources.PLANT,"MANUSCRIPT",necessaryResources);
        static GoldCard goldCard4=new GoldCard(cornersg1,2,13,"green",false,false,false,2,Resources.PLANT,"MANUSCRIPT",necessaryResources);
        static GoldCard goldCard5=new GoldCard(cornersg1,2,13,"green",false,false,false,2,Resources.PLANT,"MANUSCRIPT",necessaryResources);
        static GoldCard goldCard6=new GoldCard(cornersg1,2,13,"green",false,false,false,2,Resources.PLANT,"MANUSCRIPT",necessaryResources);
        static GoldCard goldCard7=new GoldCard(cornersg1,2,13,"green",false,false,false,2,Resources.PLANT,"MANUSCRIPT",necessaryResources);


    static Corner corner1=new Corner(null, Resources.FUNGI,1,false,false);
    static Corner corner2=new Corner(null,null,2,true,false);
    static Corner corner3=new Corner(null,null,3,true,false);
    static Corner corner4=new Corner(null,null,4,true,true);
    static ArrayList<Corner> corners1=new ArrayList<>(Arrays.asList(corner1,corner2,corner3,corner4));
    static ResourceCard resourceCard=new ResourceCard(corners1,1,2,"red",false,false,false,2, Resources.FUNGI);




    static Corner corner4_1=new Corner(null, Resources.FUNGI,1,false,false);
        static Corner corner4_2=new Corner(null,null,2,true,false);
        static Corner corner4_3=new Corner(null,null,3,true,true);
        static Corner corner4_4=new Corner(null,null,4,true,true);

        static ArrayList<Corner> cornersr4=new ArrayList<>(Arrays.asList(corner4_1,corner4_2,corner4_3,corner4_4));
        static ResourceCard resourceCard4=new ResourceCard(cornersr4,1,2,"purple",false,false,false,2, Resources.FUNGI);

        static ResourceCard resourceCard5=new ResourceCard(cornersr4,1,2,"blue",false,false,false,2, Resources.FUNGI);

        static ResourceCard resourceCard6=new ResourceCard(cornersr4,1,2,"blue",false,false,false,2, Resources.FUNGI);

    static ResourceCard resourceCard7=new ResourceCard(cornersr4,1,2,"blue",false,false,false,2, Resources.FUNGI);

    static ResourceCard resourceCard8=new ResourceCard(cornersr4,1,2,"blue",false,false,false,2, Resources.FUNGI);
    static ResourceCard resourceCard9=new ResourceCard(cornersr4,1,2,"blue",false,false,false,2, Resources.FUNGI);

        static Corner corneri_1=new Corner(null, Resources.FUNGI,1,true,false);
        static Corner corneri_2=new Corner(null,Resources.PLANT,2,true,false);
        static Corner corneri_3=new Corner(null,Resources.INSECT,3,true,true);
        static Corner corneri_4=new Corner(null,Resources.ANIMAL,4,true,true);
        static ArrayList<Corner> cornersi= new ArrayList<>(Arrays.asList(corneri_1,corneri_2,corneri_3,corneri_4));
        static Resources resources3_1=Resources.ANIMAL;
        static Resources resources3_3=Resources.PLANT;
        static Resources resources3_2=Resources.INSECT;
        static ArrayList<Resources> permanentResources=new ArrayList<>(Arrays.asList(resources3_1,resources3_2,resources3_3));
        static InitialCard initialCard=new InitialCard(cornersi,0,1,null,false,false,false,1,permanentResources);


        static HashMap<Card, Integer[]> manuscript = new HashMap<Card, Integer[]>();

        public static void main(String[] args){ //zigzag horisontal
            manuscript.put(goldCard1, new Integer[]{-1, -1});
            manuscript.put(goldCard2, new Integer[]{1, -1});
            manuscript.put(goldCard3, new Integer[]{3, -1});
            manuscript.put(goldCard4, new Integer[]{5, -1});
            manuscript.put(goldCard5, new Integer[]{7, -1});
            manuscript.put(goldCard6, new Integer[]{9, -1});
            manuscript.put(goldCard7, new Integer[]{11, -1});

            manuscript.put(initialCard, new Integer[]{0, 0});

            manuscript.put(resourceCard, new Integer[]{-2, 0});
            manuscript.put(resourceCard4, new Integer[]{2, 0});
            manuscript.put(resourceCard5, new Integer[]{4, 0});
            manuscript.put(resourceCard6, new Integer[]{6, 0});
            manuscript.put(resourceCard7, new Integer[]{8, 0});
            manuscript.put(resourceCard8, new Integer[]{10, 0});
            manuscript.put(resourceCard9, new Integer[]{12, 0});

            //manuscript.put(resourceCard5, new Integer[]{, 0});

            tui.showPlayerTable(manuscript);
            System.out.println();
            System.out.println("You did it!");
        }



    }

