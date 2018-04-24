<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.hockey.delegate.SquadMakerDelegate" %>  
<%@ page import="java.util.List" %>  
<%@ page import="com.hockey.dao.Player" %>  
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="hockey.css" type="text/css">
</link>
<script type="text/javascript">
function loadJsonFile() 
{
    <%
    if(request.getAttribute("playerWaitList") == null)
    {
        request.getRequestDispatcher("squadMakerServlet").include(request,response);
    }
    %>
}

  function checkInput(n)
  {	  
	  var waitingPlayerTotal = '${requestScope.playerWaitList.size()}';
	  var numberOfSquad = '${requestScope.squadList.size()}';
	  	  
	  for(var i = 0; i < numberOfSquad; i ++)
	  {		  
		  waitingPlayerTotal = Number(waitingPlayerTotal)+Number('${requestScope.squadList.get(i).size()}');
		  //alert(waitingPlayerTotal);
	  }
	  
	  //alert(waitingPlayerTotal);
  	  //alert(document.getElementById("squadId").value);
      var x=document.getElementById("squadId").value;
      if (isNaN(x)) 
      {
        alert("Please enter number only.");
        document.getElementById("squadId").value = null;
      }
      else if(Number(document.getElementById("squadId").value) > Number(waitingPlayerTotal))
      {
    	  alert("You can not enter more number of squad than number of available players.");
          document.getElementById("squadId").value = null;
      }
  }
</script>
</head>
<body onload="loadJsonFile()">    
    <h1 style="color:green;">Hockey Squad Maker</h1>        
    <br/>
    <table style="width:100%" border="0">
    <tr>
        <td>
            <form action="squadMakerServlet" method="post">    
                <label>Enter number of squads required :</label>
                <input type="text" name="number" id="squadId" onkeyup="checkInput()" required>  
                <input type="hidden" name="type" value="makeSquad">
                  <input type="submit" value="Submit">          
            </form> 
        </td>
        <td align="right">
            <form action="squadMakerServlet" method="post">                
                  <input type="submit" value="Reset">          
            </form>
        </td>
    </tr>    
    </table>
    
    <br/>
    <h2 style="color:green;">Waiting List</h2>  
    
    <display:table name="requestScope.playerWaitList" requestURI="#tab1" pagesize="5" export="true" sort="page" uid="one">    
        <display:column property="formattedName" title="Player" sortable="true" headerClass="sortable" />
        <display:column property="skating" title="Skating" sortable="true" headerClass="sortable" />
        <display:column property="shooting" title="Shooting" sortable="true" headerClass="sortable" />
        <display:column property="checking" title="Shecking" sortable="true" headerClass="sortable" />
    </display:table>    
    
    <br/>
    
    <c:forEach var = "squad" items="${requestScope.squadList}" varStatus="status">
        <h2 style="color:green;">Squad <c:out value="${status.count}" /></h2>        
        <table style="width:100%">
            <tr>
                <th>Player</th>
                <th>Skating</th> 
                <th>Shooting</th>
                <th>Checking</th>
              </tr>
              <c:set var="c" value="${0}" />
              <c:set var="sk" value="${0}" />
              <c:set var="sh" value="${0}" />
              <c:set var="ch" value="${0}" />
              <c:forEach var = "team" items="${squad}" varStatus="status1">
                  <c:set var="c" value="${c + 1}" />
                  <c:set var="sk" value="${sk + team.skating}" />
                  <c:set var="sh" value="${sh + team.shooting}" />
                  <c:set var="ch" value="${ch + team.checking}" />
                <tr>
                    <td>${team.formattedName}</td>
                    <td>${team.skating}</td> 
                    <td>${team.shooting}</td>
                    <td>${team.checking}</td>
                </tr>
            </c:forEach>
            <c:set var="ask" value="${sk/c}" />
              <c:set var="ash" value="${sh/c}" />
              <c:set var="ach" value="${ch/c}" />
              <fmt:parseNumber var="averageSk" integerOnly="true" type="number" value="${ask}" />
              <fmt:parseNumber var="averageSh" integerOnly="true" type="number" value="${ash}" />
              <fmt:parseNumber var="averageCh" integerOnly="true" type="number" value="${ach}" />
            
            <tr>
                    <td>Average</td>
                    <td>${averageSk}</td> 
                    <td>${averageSh}</td>
                    <td>${averageCh}</td>
            </tr>
        </table>
        <br/>    
    </c:forEach>
</body>
</html>