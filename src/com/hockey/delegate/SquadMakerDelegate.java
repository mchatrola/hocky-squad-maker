/**
 * 
 */
package com.hockey.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;

import com.hockey.dao.Player;

/**
 * This delegate class has all business logic for making squads
 * @author Mitsu Chatrola
 * @version 1.0 
 */
public class SquadMakerDelegate 
{    
    
    static Logger log = Logger.getLogger("SquadMakerDelegate.class");

    /**
     * This method creates number of squads from the player's list and provide list of players in waiting list. 
     * @param numberOfSquads
     * @param path
     * @return List
     * @throws Exception
     */
    public List makeSquad(int numberOfSquads, String path) throws Exception  
    {
        log.info("Start of makeSquad method");
        DataFetcherDelegate dataFetcher = new DataFetcherDelegate();
        List<Player> playerList = dataFetcher.loadAllPlayers(path);
        List squadList = new ArrayList();
        
        try 
        {
            if(playerList != null && playerList.size() > 0 && numberOfSquads > 0 && numberOfSquads <= playerList.size())
            {
                log.info("Total number of Squads: " + numberOfSquads);
                
                // Find average of total skills of all player
                long averageOfTotalSkills = calculateAverageOfTotal(playerList);   
                
                // Sort all players according to their total skills
                sortPlayer(playerList);
                
                // Find number of players we need to put in wait list
                int numberOfPlayersInWaitList = playerList.size() % numberOfSquads;
                log.debug("Number of players in wait list: " + numberOfPlayersInWaitList);
                
                // Take out wait list players from player list
                List<Player> waitList = createWaitList(numberOfPlayersInWaitList, averageOfTotalSkills,  playerList);
                
                // Creating empty ArrayLists for each squad 
                List<List<Player>> teams = new ArrayList<List<Player>>(numberOfSquads);
                for(int i = 0; i < numberOfSquads; i++)
                {
                    List<Player> lst = new ArrayList<Player>();
                    teams.add(lst);
                }

                boolean addingPlayerInForwardDirection = true;
                //looping through players to distribute them in squads.
                //Taking number of players same as number of squads in one execution of the loop and adding one of them into each team
                for(int i = 0; i < playerList.size(); i = i + numberOfSquads)
                {
                    //In even execution of the loop adding players in teams as per their index order. 
                    if(addingPlayerInForwardDirection)
                    {
                        for(int j = 0; j < numberOfSquads; j ++)
                        {
                            log.debug("Player with total " + playerList.get(i + j).getTotal() + " is added to team number " + j);
                            teams.get(j).add(playerList.get(i + j));
                        }   
                    }
                    //In odd execution of the loop adding players in teams as per their reverse index order. 
                    else
                    {
                        for(int j = numberOfSquads - 1; j >= 0; j--)
                        {
                            log.debug("Player with total = " + playerList.get(i + (numberOfSquads - 1) - j).getTotal() + " is added to team " + j);
                            teams.get(j).add(playerList.get(i + (numberOfSquads - 1) - j));
                        }
                    }
                    addingPlayerInForwardDirection = !addingPlayerInForwardDirection;
                }

                //Add wait list as first element in the List and squads list as second element.
                squadList.add(waitList);                
                squadList.add(teams);
            }
            else
            {
                //If number of squads given by user is 0 or user requests reset of squads then return all players in wait list
                squadList.add(playerList);                
            }    
            return squadList;
        } 
        catch(Exception e) 
        {
            log.error("Got exception while making squads : " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * This method calculates average of total from given list of players
     * @param playerList
     * @return long
     */
    long calculateAverageOfTotal(List<Player> playerList)
    {
        log.info("Start of calculateAverageOfTotal method");
        long averageOfTotalSkills = 0; 
        for(int i = 0; i < playerList.size() ; i++)
        {
            averageOfTotalSkills = averageOfTotalSkills + playerList.get(i).getTotal();
        }
        averageOfTotalSkills = averageOfTotalSkills / playerList.size();        
        log.info("Average of total skills of all players: " + averageOfTotalSkills);
        return averageOfTotalSkills;
    }
    
    /**
     * This method sort player's list in descending order of their total 
     * @param playerList
     * @return List<Player>
     */
    void sortPlayer(List<Player> playerList)
    {
        log.info("Start of sortPlayer method");
        // Sort players as per their skill's total.
        Comparator<Player> comparator = Collections.reverseOrder(new ComparePlayer());
        Collections.sort(playerList, comparator);  
        for(int i = 0; i < playerList.size(); i++)
        {
            log.debug("Total for player " + playerList.get(i).getFirstName() + " is: " + playerList.get(i).getTotal());
        }
    }
    
    /**
     * This method creates wait list from the given player list and return wait list as first element and remaining player list as second element
     * @param waitNumber
     * @param fAverage
     * @param playerList
     * @return List
     */
    List<Player> createWaitList(int waitNumber, long averageOfTotalSkills, List<Player> playerList)
    {
        log.info("Start of createWaitList method");
        List<Player> waitList = new ArrayList<Player>();
        //Removing players whose skill's total are far from average total and put them in wait list
        for(int i = 0; i < waitNumber; i++)
        {
            Player firstPlayer = playerList.get(0);
            Player lastPlayer = playerList.get(playerList.size() - 1);
            
            if((firstPlayer.getTotal() - averageOfTotalSkills) > (averageOfTotalSkills - lastPlayer.getTotal()))
            {
                log.debug("Excluding player with total: " + firstPlayer.getTotal());
                waitList.add(firstPlayer);
                playerList.remove(0);
            }            
            else
            {
                log.debug("Excluding player with total: " + lastPlayer.getTotal());
                waitList.add(lastPlayer);
                playerList.remove(playerList.size() - 1);
            }
        }
        return waitList;
    }
}

/**
 * Comparator to sort players according to their skill total
 * @author Mitsu Chatrola
 * @version 1.0
 *
 */
class ComparePlayer implements Comparator<Player>
{
    @Override
    public int compare(Player p1, Player p2)
    {
        return p1.getTotal().compareTo(p2.getTotal());
    }
}
