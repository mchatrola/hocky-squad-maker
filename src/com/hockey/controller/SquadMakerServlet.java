package com.hockey.controller;

import com.hockey.delegate.DataFetcherDelegate;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.hockey.delegate.SquadMakerDelegate;

/**
 * Servlet implementation class SquadMakerServlet.
 * This servlet works as a controller for this application. All requests comes to it and it will call related delegate methods for business logic
 * @author Mitsu Chatrola
 * @version 1.0 
 */
@WebServlet(description = "SquadMakerServlet", urlPatterns = { "/squadMakerServlet" })
public class SquadMakerServlet extends HttpServlet 
{
    private static final long serialVersionUID = 1L;
    static Logger log = Logger.getLogger("SquadMakerServlet.class");
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SquadMakerServlet() 
    {
        super();       
    }
    
    private String getJsonFilePath()
    {
        ServletContext servletContext = getServletContext();  
        String rootPath = servletContext.getRealPath("/");
        return rootPath + "player.json";        
    }

    /**
     * When this application loads initially, it will send get request to load all players in waiting list.
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {        
        log.info("Get request called for SquadMakerServlet.");
        if(request.getAttribute("playerWaitList") == null || ((List)request.getAttribute("playerWaitList")).size() < 1)
        {
            log.debug("playerWaitList attribute is not available so reading json file and setting the attribute.");
            try 
            {
                DataFetcherDelegate dataFetcherDelegate = new DataFetcherDelegate();                
                List list = dataFetcherDelegate.loadAllPlayers(getJsonFilePath());
                
                //put result in request attribute to access in jsp page
                request.setAttribute("playerWaitList", list);                    
            } 
            catch (Exception e) 
            {            
                log.error("Got exception while executing doGet method of SquadMakerServlet "+e.getMessage());
                // TODO: this won't work. need to show user friendly error
                request.setAttribute("error", e.toString());                
                RequestDispatcher rd = getServletContext( ).getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            }
        }        
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    /**
     * When user wants to make squads , they will send post request with number of squads.
     * Reset squad will also be post method call without number of squads.
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {        
        log.info("Post request called for SquadMakerServlet.");
            try 
            {
                SquadMakerDelegate sm = new SquadMakerDelegate();
                List squadList = null;
                //If squad number is given then create squads otherwise put all players in waiting list
                if(request.getParameter("number") != null)
                {
                    squadList = sm.makeSquad(Integer.parseInt(request.getParameter("number")), getJsonFilePath());    
                }
                else
                {
                    squadList = sm.makeSquad(0, getJsonFilePath());
                }  
                
                //put result in request attribute to access in jsp page
                if(squadList != null && squadList.size() > 0)
                {
                    if(squadList.size() == 1)
                    {
                        request.setAttribute("playerWaitList", squadList.get(0));
                    }
                    else if(squadList.size() == 2)
                    {
                        request.setAttribute("playerWaitList", squadList.get(0));
                        request.setAttribute("squadList", squadList.get(1));
                    }        
                }            
                                
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
            } catch (Exception e) 
            {            
                e.printStackTrace();
                log.error("Got exception while executing doPost method of SquadMakerServlet "+e.getMessage());
                
                request.setAttribute("error", e.toString());                
                RequestDispatcher rd = getServletContext( ).getRequestDispatcher("/error.jsp");
                rd.forward(request, response);
            }
    }
}
