/**
 * 
 */
package com.hockey.delegate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hockey.dao.Player;

/**
 * This delegate class has all business logic for reading and parsing json file
 * @author Mitsu Chatrola
 * @version 1.0 
 */
public class DataFetcherDelegate 
{    
    
    static Logger log = Logger.getLogger("DataFetcherDelegate.class");
    
    void throwExceptionWithMessage(String message) throws Exception
    {
        Exception e = new Exception(message);
        log.error(e.getMessage());
        throw e;
    }
    
    /** 
     * This method reads json file from the given path and returns data as JSONObject.
     * Once web service is available to fetch json data we will integrate it here.
     * 
     * First call to this method will call web service to get data, we will store that data in a file locally with expiry time (say 5 min).
     * All other call to this method will read data from local file for next 5 min. After that make call to web service again and get latest data.
     * 
     * @param path
     * @return JSONObject
     * @throws Exception
     */   
    JSONObject getDataFromService(String path) throws Exception
    {
        if(path == null || path.length() == 0)
        {
            throwExceptionWithMessage("Json file path is null or empty.");
        }
        
        JSONParser parser = new JSONParser();
        try 
        {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(path));
            if(jsonObject == null)
            {
                throwExceptionWithMessage("Invalid Json content in file: " + path);
            }
            return jsonObject;
        } 
        catch (FileNotFoundException e) 
        {
            log.error("Got FileNotFoundException while reading json file : " + e.getMessage());
            throw e;
        }
        catch (IOException e) 
        {
            log.error("Got IOException while reading json file : " + e.getMessage());
            throw e;
        }
        catch (ParseException e) 
        {
            log.error("Got ParseException while reading json file : " + e.getMessage());
            throw e;
        }
    }
    
    /** 
     * This method reads json file from the given path and creates an ArrayList of player objects from it.
     * 
     * @param path
     * @return List<Player>
     * @throws Exception
     */
    public List<Player> loadAllPlayers(String path) throws Exception  
    {        
        log.info("Start of loadAllPlayers method");
        
        try 
        {
            log.info("loading data from path: " + path);
            
            JSONObject jsonObject = getDataFromService(path);
            log.debug(jsonObject);

            JSONArray players = (JSONArray) jsonObject.get("players");
            if(players == null)
            {
                throwExceptionWithMessage("Json file don't have 'players' object.");
            }
            
            List<Player> listOfPlayers = new ArrayList<Player>();
            log.debug("Number of player objects found in json file :" + players.size());
            for (Object playerAsObj : players) 
            { 
                Player player = createPlayerFromJsonObject((JSONObject) playerAsObj);
                if(player != null)
                {
                    listOfPlayers.add(player);
                }
            }
            
            if(listOfPlayers.size() != players.size())
            {
                log.debug("Size of listOfPlayers = "+ listOfPlayers.size() + ", Size of json array = " + players.size());
                throwExceptionWithMessage("Some player records inside json data are corrupted.");
            }
            
            return listOfPlayers;
        }
        catch(Exception e) 
        {
            log.error("Got exception while fetching data: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * This method creates players object from JSONObject and return it
     * @param playerAsJson
     * @return Player
     * @throws Exception
     */
    Player createPlayerFromJsonObject(JSONObject playerAsJson)
    {
        log.info("Start of createPlayerFromJsonObject method");
        try
        {
            if(playerAsJson != null)
            {
                // Create player object 
                Player player = new Player();
                player.setPlayerId((String) playerAsJson.get("_id"));
                if(player.getPlayerId() == null || player.getPlayerId().isEmpty())
                {
                    throwExceptionWithMessage("Player record " + playerAsJson + " has invalid id");
                }
                
                player.setFirstName((String) playerAsJson.get("firstName"));
                if(player.getFirstName() == null || player.getFirstName().isEmpty())
                {
                    throwExceptionWithMessage("Player record " + playerAsJson + " has invalid firstName");
                }
                
                player.setLastName((String) playerAsJson.get("lastName"));
                if(player.getLastName() == null || player.getLastName().isEmpty())
                {
                    throwExceptionWithMessage("Player record " + playerAsJson + " has invalid lastName");
                }
                
                JSONArray skill = (JSONArray) playerAsJson.get("skills");
                Long sh = null;
                Long sk = null;
                Long ch = null;
                
                for (Object singleSkiil : skill) 
                {              
                    JSONObject skillObj = (JSONObject) singleSkiil;
                    String skillType = (String) skillObj.get("type");
                    if(skillType.equalsIgnoreCase("Shooting"))
                    {
                        sh = (Long) skillObj.get("rating");
                        player.setShooting(sh);
                    }                        
                    else if(skillType.equalsIgnoreCase("Skating"))
                    {
                        sk = (Long) skillObj.get("rating");
                        player.setSkating(sk);
                    }                        
                    else if(skillType.equalsIgnoreCase("Checking"))
                    {
                        ch = (Long) skillObj.get("rating");
                        player.setChecking(ch);
                    }
                    else
                    {
                        throwExceptionWithMessage("Player record " + playerAsJson + " has invalid skill type: " + skillType);
                    }
                }

                if(sh == null || sk == null || ch == null)
                {
                    throwExceptionWithMessage("Player record " + playerAsJson + " has invalid some skills missing.");
                }
                
                // Add total of all skill for each player in player object.
                player.setTotal(sh + sk + ch);  
                log.debug("Player details : name = " + player.formattedName() + ", total =  " + player.getTotal());
                return player;                
            }
            else
            {
                // No need to throw here as this will be captured by count at end
                return null;
            }    
        }
        catch(Exception e) 
        {
            log.info("Exception occured when trying to get player from json data: "+ playerAsJson);
            log.error("Got exception while creating player object from JSONObject : " + e.getMessage());
            // do not throw here as this will be captured by count of players at end
            return null;
        }
    }
}
