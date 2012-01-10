<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html>
  <head>
  <title><i18n:i18n msg='deletecompany'/>| Accounter
  </title>
        <meta content="IE=100" http-equiv="X-UA-Compatible" />
		<link rel="shortcut icon" href="/images/favicon.ico" />
		
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet" />
  </head>
  <body>
  <div id="commanContainer">
	<div>
		<img src="/images/Accounter_logo_title.png" class="accounterLogo" />
	</div>
    <div >
       
       <c:if test="${message != null}">
       		<div class="common-box create-company-message">${message}</div>
        </c:if>
        
	   <form class="accounterform" action="/main/deletecompany" method="post">
	  	 	<c:if test="${canDeleteFromSingle}">
            	<input type="radio" name="delete" value="deleteUser">
            		<i18n:i18n msg='deletecompanyfromaccount'/>
				<p class="delete_message"><i18n:i18n msg='deletecompanyWarningMsg'/></p>
			</c:if>
			<c:if test="${canDeleteFromAll}">
            <br>
            <input type="radio" name="delete" value="deleteAllUsers">
                <i18n:i18n msg='deletecompanyfromallusers'/>
            <p class="delete_message"><i18n:i18n msg='deleteMsg'/></p>
			</c:if>
			<br>
            <br>
            <div class="company_list_buttons">
	            <input type="submit" value="<i18n:i18n msg='delete'/>" class="allviews-common-button">
	            <input type="button" value="<i18n:i18n msg='cancel'/>" class="allviews-common-button" onclick="parent.location='/main/deletecompany?isCancel=true'">
            </div>
        </form>
	    
    </div>
   </div>
    <!-- Footer Section-->
   
   <div id="mainFooter"  >
   <div>
	       <span><i18n:i18n msg='atTherateCopy'/></span> |
	       <a target="_blank" href="/site/termsandconditions"><i18n:i18n msg='termsConditions'/></a> |
	       <a target="_blank" href="/site/privacypolicy"><i18n:i18n msg='privacyPolicy'/></a> |
	       <a target="_blank" href="/site/support"><i18n:i18n msg='support'/></a>
   </div>
</div>
<%@ include file="./scripts.jsp" %>
  </body>
</html>