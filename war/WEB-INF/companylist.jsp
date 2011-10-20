<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
  <title>Companies list| Accounter
  </title>
        <meta content="IE=100" http-equiv="X-UA-Compatible" />
		<link rel="shortcut icon" href="/images/favicon.ico" />
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css" rel="stylesheet" />
  </head>
  <body>
  <div id="commanContainer">
	<div>
		<img src="/images/Accounter_logo_title.png" class="accounterLogo" alt="loading" />
		
	</div>
    <div class="company_lists">
       
       
        <c:if test="${message != null}">
       		<div class="common-box create-company-message">${message}</div>
        </c:if>
       <div class="form-box">
      	<div> <a href="/main/companies?create=true" class="create_new_company">Create New Company </a></div>
      	<ul><li>
	    <c:if test="${companeyList != null}">
		   <c:forEach var="company" items="${companeyList}">
			   <c:set var='url' value="/main/companies?companyId=${company.id}"/>
			    <c:set var='deleteurl' value="/main/deletecompany?companyId=${company.id}"/>
			   <div class="companies-list"><a href= '${url}'>${company.preferences.tradingName} - ${company.registeredAddress.countryOrRegion} </a> <a class="delete_company" href= '${deleteurl}' >Delete</a></div>
		   </c:forEach>
	    </c:if>
	    </li>
	   </ul>
	  </div>
    </div>
    <div class="form-bottom-options">
      <a href="/main/logout">Logout</a>
    </div>
   </div>
   
   <!-- Footer Section-->
   
   <div id="mainFooter"  >
   <div>
      <span>&copy 2011 Vimukti Technologies Pvt Ltd</span> |
      <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
      <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
      <a target="_blank" href="/site/support"> Support </a>
   </div>
</div>
   
<%@ include file="./scripts.jsp" %>
  </body>
</html>