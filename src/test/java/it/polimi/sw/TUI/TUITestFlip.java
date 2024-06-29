package it.polimi.sw.TUI;

import it.polimi.sw.model.*;
import it.polimi.sw.model.Object;
import it.polimi.sw.view.TUI;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class TUITestFlip {
    static TUI tui=new TUI();
    static Corner corner1=new Corner(null, Resources.FUNGI,1,true,null);
    static Corner corner2=new Corner(null,null,2,false,null);
    static Corner corner3=new Corner(null,null,3,true,null);
    static Corner corner4=new Corner(null,null,4,true,null);
    static Resources resources1=Resources.PLANT;
    static Resources resources2=Resources.PLANT;
    static Resources resources3=Resources.INSECT;
    static ArrayList<Resources> necessaryResources=new ArrayList<>(Arrays.asList(resources1,resources2,resources3));
    static ArrayList<Corner> corners=new ArrayList<>(Arrays.asList(corner1,corner2,corner3,corner4));


    static ResourceCard resourceCard=new ResourceCard(corners,1,2,"red",false,false,false,2, Resources.FUNGI);

    static ResourceCard goldcard =new ResourceCard(corners,0,2,"red",false,false,false,1, Resources.FUNGI);


    public static void main (String[] args) throws RemoteException {
         ResourceCard resourceCard=new ResourceCard(corners,1,2,"red",false,false,false,2, Resources.FUNGI);

         ResourceCard goldcard =new ResourceCard(corners,0,2,"red",false,false,false,1, Resources.FUNGI);


/*
        ArrayList<Card> frontHand = new ArrayList<Card>();
        ArrayList<Card> backHand = new ArrayList<Card>();
        frontHand.add(goldcard);
        backHand.add(resourceCard);

        tui.frontHand.add(goldcard);
        tui.backHand.add(resourceCard);
        System.out.println("Card frontID: " +  tui.frontHand.get(0).getId()+ ", Class: " + tui.frontHand.getFirst().getClass().getSimpleName());
        System.out.println("front side corner:"+tui.frontHand.get(0).getSide());
        System.out.println("Card backID: " +  tui.backHand.get(0).getId()+ ", Class: " + tui.backHand.getFirst().getClass().getSimpleName());
        System.out.println("back side corner:"+tui.backHand.get(0).getSide());


        if (frontHand.isEmpty()) {
            System.out.println("Card index out of bounds: " + 1);
        }else {

                tui.cardToFlip(1);
        }

        System.out.println("Card frontID: " +  tui.frontHand.get(0).getId()+ ", Class: " + tui.frontHand.getFirst().getClass().getSimpleName());
        System.out.println("front side corner:"+tui.frontHand.get(0).getSide());
        System.out.println("Card backID: " +  tui.backHand.get(0).getId()+ ", Class: " + tui.backHand.getFirst().getClass().getSimpleName());
        System.out.println("back side corner:"+tui.backHand.get(0).getSide());

*/

    }
}
