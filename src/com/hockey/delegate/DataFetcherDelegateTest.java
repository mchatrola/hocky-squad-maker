package com.hockey.delegate;

import com.hockey.dao.Player;
import java.io.FileNotFoundException;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test class for DataFetcherDelegate.java
 * @author Mitsu Chatrola
 * @version 1.0
 */
public class DataFetcherDelegateTest {
    
    public DataFetcherDelegateTest() {
    }
    
    private JSONObject getValidPlayer()
    {
        JSONObject shooting = new JSONObject();
        shooting.put("type", "Shooting");
        shooting.put("rating", new Long(77));
        
        JSONObject skating = new JSONObject();
        skating.put("type", "Skating");
        skating.put("rating", new Long(61));
        
        JSONObject checking = new JSONObject();
        checking.put("type", "Checking");
        checking.put("rating", new Long(83));
        
        JSONArray skills = new JSONArray();
        skills.add(shooting);
        skills.add(skating);
        skills.add(checking);
        
        JSONObject playerAsJson = new JSONObject();
        playerAsJson.put("_id", "5adc357c9142996947237023");
        playerAsJson.put("firstName", "Karin");
        playerAsJson.put("lastName", "Hardin");
        playerAsJson.put("skills", skills);
        
        return playerAsJson;
    }
    
    @Test
    public void throwExceptionWithMessage() 
    {
        DataFetcherDelegate instance = new DataFetcherDelegate();
        try 
        {
            instance.throwExceptionWithMessage("my exception message 123.");
            fail("Expected an Exception");
        } 
        catch (Exception ex) {
            assertEquals("my exception message 123.", ex.getMessage());
        }
    }

    @Test
    public void getDataFromService_ValidFile() throws Exception
    {
        String path = ".\\test\\resources\\players.json";
        DataFetcherDelegate instance = new DataFetcherDelegate();
        JSONObject result = instance.getDataFromService(path);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void getDataFromService_InvalidJson() throws Exception
    {
        String path = ".\\test\\resources\\invalidJson.txt";
        DataFetcherDelegate instance = new DataFetcherDelegate();
        instance.getDataFromService(path);
    }

    @Test(expected = FileNotFoundException.class)
    public void getDataFromService_InvalidFile() throws Exception
    {
        String path = ".\\test\\resources\\DoNotExists.json";
        DataFetcherDelegate instance = new DataFetcherDelegate();
        instance.getDataFromService(path);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void loadAllPlayers_InvalidFile() throws Exception
    {
        String path = ".\\test\\resources\\DoNotExists.json";
        DataFetcherDelegate instance = new DataFetcherDelegate();
        instance.loadAllPlayers(path);
    }
    
    @Test
    public void loadAllPlayers_NoPlayersObject()
    {
        String path = ".\\test\\resources\\noPlayersObject.json";
        DataFetcherDelegate instance = new DataFetcherDelegate();
        try 
        {
            instance.loadAllPlayers(path);
            fail("Expected an Exception");
        } 
        catch (Exception ex) {
            assertEquals("Json file don't have 'players' object.", ex.getMessage());
        }
    }

    @Test
    public void loadAllPlayers_WithNullPlayersObject()
    {
        String path = ".\\test\\resources\\oneNullPlayer.json";
        DataFetcherDelegate instance = new DataFetcherDelegate();
        try 
        {
            instance.loadAllPlayers(path);
            fail("Expected an Exception");
        } 
        catch (Exception ex) {
            assertEquals("Some player records inside json data are corrupted.", ex.getMessage());
        }
    }

    @Test
    public void loadAllPlayers_ValidJson() throws Exception
    {
        String path = ".\\test\\resources\\players.json";
        DataFetcherDelegate instance = new DataFetcherDelegate();
        List<Player> list = instance.loadAllPlayers(path);
        assertEquals(10, list.size());
    }

    @Test
    public void createPlayerFromJsonObject_WithoutId()
    {
        JSONObject playerAsJson = getValidPlayer();
        playerAsJson.remove("_id");
        
        DataFetcherDelegate instance = new DataFetcherDelegate();
        assertNull(instance.createPlayerFromJsonObject(playerAsJson));
    }

    @Test
    public void createPlayerFromJsonObject_WithoutFirstName()
    {
        JSONObject playerAsJson = getValidPlayer();
        playerAsJson.remove("firstName");
        
        DataFetcherDelegate instance = new DataFetcherDelegate();
        assertNull(instance.createPlayerFromJsonObject(playerAsJson));
    }

    @Test
    public void createPlayerFromJsonObject_WithoutLastName()
    {
        JSONObject playerAsJson = getValidPlayer();
        playerAsJson.remove("lastName");
            
        DataFetcherDelegate instance = new DataFetcherDelegate();
        assertNull(instance.createPlayerFromJsonObject(playerAsJson));
    }

    @Test
    public void createPlayerFromJsonObject_WithoutRequiredSkills()
    {
        for(int i = 0; i < 3; i++)
        {
            JSONObject playerAsJson = getValidPlayer();
            JSONArray skills = (JSONArray) playerAsJson.get("skills");
            skills.remove(i);

            DataFetcherDelegate instance = new DataFetcherDelegate();
            assertNull(instance.createPlayerFromJsonObject(playerAsJson));
        }
    }

    @Test
    public void createPlayerFromJsonObject_WithExtraSkill()
    {
        JSONObject extraSkill = new JSONObject();
        extraSkill.put("type", "ExtraSkill");
        extraSkill.put("rating", new Long(77));
        
        JSONObject playerAsJson = getValidPlayer();
        JSONArray skills = (JSONArray) playerAsJson.get("skills");
        skills.add(extraSkill);
        
        DataFetcherDelegate instance = new DataFetcherDelegate();
        assertNull(instance.createPlayerFromJsonObject(playerAsJson));
    }

    @Test
    public void createPlayerFromJsonObject_Valid() throws ParseException
    {
        JSONObject playerAsJson = getValidPlayer();
        DataFetcherDelegate instance = new DataFetcherDelegate();
        Player player = instance.createPlayerFromJsonObject(playerAsJson);
        assertEquals(221, (long) player.getTotal());
    }
}
