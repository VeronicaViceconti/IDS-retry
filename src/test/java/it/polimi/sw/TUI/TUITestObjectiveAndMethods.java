package it.polimi.sw.TUI;

import it.polimi.sw.model.*;
import it.polimi.sw.model.Object;
import it.polimi.sw.view.TUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TUITestObjectiveAndMethods {

    static TUI tui=new TUI();
    static Player p=new Player("pippo", Pion.pion_yellow);
    static HashMap<String,Integer> numOfResourceAndObject=new HashMap<>();
    static Resources resource1=Resources.FUNGI;
    static Resources resource2=Resources.FUNGI;
    static Resources resource3=Resources.FUNGI;
    static List<Resources> resourcesRequired=new ArrayList<>(Arrays.asList(resource1,resource2,resource3));
    static Conditions condition2=new Conditions(97,0,ObjectiveTypes.ResourceFilling,resourcesRequired,null,null,null,null,null);
    static Conditions condition1=new Conditions(101,3, ObjectiveTypes.ThreeSameColourPlacing,null,null,"blue",null,2,null);
    static Objective objective=new Objective(2,condition1,false);
    static Objective objective2=new Objective(3,condition2,false);
    static ArrayList<Objective> objectives=new ArrayList<>(Arrays.asList(objective,objective2));

    static Corner corner3_1=new Corner(null,Resources.FUNGI,1,true,null);
    static Corner corner3_2=new Corner(null,Resources.PLANT,2,true,null);
    static Corner corner3_3=new Corner(null,Resources.INSECT,3,true,null);
    static Corner corner3_4=new Corner(null,Resources.ANIMAL,4,true,null);
    static ArrayList<Corner> corners3= new ArrayList<>(Arrays.asList(corner3_1,corner3_2,corner3_3,corner3_4));
    static Resources resources3_1=Resources.ANIMAL;
    static Resources resources3_3=Resources.PLANT;
    static Resources resources3_2=Resources.INSECT;
    static ArrayList<Resources> permanentResources=new ArrayList<>(Arrays.asList(resources3_1,resources3_2,resources3_3));
    static InitialCard initialCard=new InitialCard(corners3,0,1,null,false,false,false,1,permanentResources);



    static Corner corner1=new Corner(null, Resources.FUNGI,1,false,initialCard);
    static Corner corner2=new Corner(null,null,2,true,null);
    static Corner corner3=new Corner(null,null,3,true,null);
    static Corner corner4=new Corner(null,null,4,true,null);

    static ArrayList<Corner> corners=new ArrayList<>(Arrays.asList(corner1,corner2,corner3,corner4));
    static ResourceCard resourceCard=new ResourceCard(corners,1,2,"red",false,false,false,1, Resources.FUNGI);


    static Corner corner2_1=new Corner(Object.INKWELL,null,1,true,null);
    static Corner corner2_2=new Corner(null,null,2,true,null);
    static Corner corner2_3=new Corner(null,null,3,true,null);
    static Corner corner2_4=new Corner(null,null,4,false, initialCard);

    static Resources resources1=Resources.PLANT;
    static Resources resources2=Resources.PLANT;
    static Resources resources3=Resources.INSECT;
    static ArrayList<Resources> necessaryResources=new ArrayList<>(Arrays.asList(resources1,resources2,resources3));
    static ArrayList <Corner> corners_2=new ArrayList<>(Arrays.asList(corner2_1,corner2_2,corner2_3,corner2_4));
    static GoldCard goldCard=new GoldCard(corners_2,2,13,"green",false,false,false,1,Resources.PLANT,"MANUSCRIPT",necessaryResources);

    static ArrayList<Card> newHand=new ArrayList<>(Arrays.asList(goldCard,resourceCard,initialCard));

    public static void main(String[] args){
        numOfResourceAndObject.put("ANIMAL",3);
        numOfResourceAndObject.put("PLANT", 2);
        numOfResourceAndObject.put("QUILL", 1);
        numOfResourceAndObject.put("INSECT", 0);
        p.setPoints(4);
        p.setId(2);
        p.setNumOfResourceAndObject(numOfResourceAndObject);
        tui.showPlayerState(p);
        p.setHand(newHand);


        /*tui.showPrivateObjective(objective);
        System.out.print("\n");
        tui.showCommonObjectives(objectives);*/

        tui.showPlayerState(p);
        //tui.showPlayerHand(p.getHand());
        //tui.addPoints(p,5);
    }





}


