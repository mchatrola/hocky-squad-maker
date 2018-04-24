package com.hockey.dao;
/**
 * This class implements IPlayer interface 
 * It has all attributes of Player object and its getter and setter methods.
 * @author Mitsu Chatrola
 * @version 1.0 
 */
public class Player implements IPlayer
{
    private String playerId;
    private String firstName;
    private String lastName;
    private Long shooting;
    private Long skating;
    private Long checking;
    private Long total;
    private String formattedName;
    
    public Player()
    {
        super();
    }
    
    public String formattedName()
    {
        return firstName + " " + lastName;
    }
    
    public String getFormattedName()
    {
        return firstName + " " + lastName;
    }
    
    public String getPlayerId() 
    {
        return playerId;
    }
    
    public void setPlayerId(String playerId) 
    {
        this.playerId = playerId;
    }
    
    public String getFirstName() 
    {
        return firstName;
    }
    
    public void setFirstName(String firstName) 
    {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) 
    {
        this.lastName = lastName;
    }
    
    public Long getShooting() {
        return shooting;
    }
    
    public void setShooting(Long shooting) 
    {
        this.shooting = shooting;
    }
    
    public Long getSkating() {
        return skating;
    }
    
    public void setSkating(Long skating) 
    {
        this.skating = skating;
    }
    
    public Long getChecking() {
        return checking;
    }
    
    public void setChecking(Long checking) 
    {
        this.checking = checking;
    }

    public Long getTotal() 
    {
        return total;
    }

    public void setTotal(Long total) 
    {
        this.total = total;
    }

    public void setFormattedName(String formattedName) 
    {
        this.formattedName = formattedName;
    }
}
