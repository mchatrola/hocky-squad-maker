package com.hockey.delegate;

import com.hockey.dao.Player;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test class for SquadMakerDelegate.java
 * @author Mitsu Chatrola
 * @version 1.0
 */
public class SquadMakerDelegateTest {
    
    public SquadMakerDelegateTest() {
    }

    private List<Player> getListOfPlayersForTest() throws Exception
    {
        String path = ".\\test\\resources\\players.json";
        DataFetcherDelegate instance = new DataFetcherDelegate();
        List<Player> list = instance.loadAllPlayers(path);
        return list;
    }
    
    @Test
    public void calculateAverageOfTotal() throws Exception
    {
        List<Player> list = getListOfPlayersForTest();
        SquadMakerDelegate squadMakerDelegate = new SquadMakerDelegate();
        long averageOfTotal = squadMakerDelegate.calculateAverageOfTotal(list); 
        assertEquals(175, averageOfTotal);
    }
    
    @Test
    public void makeSquad_ZeroSquard() throws Exception
    {
        SquadMakerDelegate squadMakerDelegate = new SquadMakerDelegate();
        List list = squadMakerDelegate.makeSquad(0, ".\\test\\resources\\players.json"); 
        assertEquals(1, list.size());
        assertEquals(10, ((List)list.get(0)).size());
    }
    
    @Test
    public void makeSquad_FourSquard() throws Exception
    {
        SquadMakerDelegate squadMakerDelegate = new SquadMakerDelegate();
        List list = squadMakerDelegate.makeSquad(4, ".\\test\\resources\\players.json"); 
        assertEquals(2, list.size());
        assertEquals(2, ((List) list.get(0)).size()); // This is wait list size
        
        List<List<Player>> teams = (List<List<Player>>) list.get(1);
        assertEquals(4, teams.size());
        
        assertEquals(2, teams.get(0).size());
        assertEquals(198, (long) teams.get(0).get(0).getTotal());
        assertEquals(146, (long) teams.get(0).get(1).getTotal());
        
        assertEquals(2, teams.get(1).size());
        assertEquals(194, (long) teams.get(1).get(0).getTotal());
        assertEquals(158, (long) teams.get(1).get(1).getTotal());
        
        assertEquals(2, teams.get(2).size());
        assertEquals(183, (long) teams.get(2).get(0).getTotal());
        assertEquals(164, (long) teams.get(2).get(1).getTotal());
        
        assertEquals(2, teams.get(3).size());
        assertEquals(177, (long) teams.get(3).get(0).getTotal());
        assertEquals(168, (long) teams.get(3).get(1).getTotal());
    }

    @Test
    public void sortPlayer() throws Exception
    {
        List<Player> list = getListOfPlayersForTest();
        SquadMakerDelegate squadMakerDelegate = new SquadMakerDelegate();
        squadMakerDelegate.sortPlayer(list); 
        assertEquals(221, (long) list.get(0).getTotal());
        assertEquals(198, (long) list.get(1).getTotal());
        assertEquals(194, (long) list.get(2).getTotal());
        assertEquals(183, (long) list.get(3).getTotal());
        assertEquals(177, (long) list.get(4).getTotal());
        assertEquals(168, (long) list.get(5).getTotal());
        assertEquals(164, (long) list.get(6).getTotal());
        assertEquals(158, (long) list.get(7).getTotal());
        assertEquals(146, (long) list.get(8).getTotal());
        assertEquals(143, (long) list.get(9).getTotal());
    }

    @Test
    public void createWaitList() throws Exception
    {
        List<Player> list = getListOfPlayersForTest();
        SquadMakerDelegate squadMakerDelegate = new SquadMakerDelegate();
        List<Player> waitList = squadMakerDelegate.createWaitList(2, 10, list); 
        assertEquals(221, (long) waitList.get(0).getTotal());
        assertEquals(158, (long) waitList.get(1).getTotal());
    }
}
