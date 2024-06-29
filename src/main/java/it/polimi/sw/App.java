package it.polimi.sw;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.sw.model.*;
import it.polimi.sw.network.Client;
import it.polimi.sw.network.ClientModel.Match;
import it.polimi.sw.network.RMI.ClientRMI;
import it.polimi.sw.view.GraphicalUserInterface.GUI;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
/**
 * This class likely represents the main entry point for the application.
 * It's responsible for setting up the game environment and potentially starting the game loop.
 */
public class App
{
    private static final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(2);

    private static ArrayList<Player> players = new ArrayList<>();
    private static HashMap<Card,Card> corrGold;
    private static HashMap<Card,Card> corrRes;
    private static HashMap<Card,Card> corrInit;
    private static final ArrayList<Card> initialCard = new ArrayList<>();
    private static ArrayList<Card> resourceCard = new ArrayList<>();
    private static final ArrayList<Card> goldCard = new ArrayList<>();
    private static final ArrayList<Objective> objectiveCards = new ArrayList<>();
    private static Game game;


    public static void main(String[] args) throws InterruptedException, NotBoundException, RemoteException {

    }
}
