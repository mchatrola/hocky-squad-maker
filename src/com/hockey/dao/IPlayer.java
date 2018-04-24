/**
 * 
 */
package com.hockey.dao;

/**
 * This Intereface represent Player Object
 * @author Mitsu Chatrola
 * @version 1.0 
 */
public interface IPlayer 
{
    public String getPlayerId();
    public void setPlayerId(String playerId);
    public String getFirstName() ;
    public void setFirstName(String firstName);
    public String getLastName();
    public void setLastName(String lastName);
    public Long getShooting();
    public void setShooting(Long shooting);
    public Long getSkating();
    public void setSkating(Long skating);
    public Long getChecking();
    public void setChecking(Long checking);
    public Long getTotal();
    public void setTotal(Long total);
}
