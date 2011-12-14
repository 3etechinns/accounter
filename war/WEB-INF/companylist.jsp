<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/i18n.tld"%>
<html>
  <head>
  <title><i18n:i18n msg='companieslist'/>| Accounter
  </title>
        <meta content="IE=100" http-equiv="X-UA-Compatible" />
		<link rel="shortcut icon" href="/images/favicon.ico" />
		<%@ include file="./feedback.jsp" %>
		<link type="text/css" href="../css/ss.css" rel="stylesheet" />
	<script type="text/javascript">
		function goto(comp){
			$(document).ready(function() {
				var params= {
					companyId: comp,
					isTouch: touchDeviceTest()
				};
				document.location = '/main/companies' + '?' + $.param(params);
			});
		};
		function touchDeviceTest() {
			var el = document.createElement('div');
			el.setAttribute('ongesturestart', 'return;');
			if(typeof el.ongesturestart == "function"){
				return true;
			}else {
				return false;
			}
		}
	</script>
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
      	<div> <a href="/main/companies?create=true" class="create_new_company"><i18n:i18n msg='createNewCompany'/></a></div>
      	<ul><li>
	    <c:if test="${companeyList != null}">
		   <c:forEach var="company" items="${companeyList}">
			   <c:set var='url' value="/main/companies?companyId=${company.id}"/>
			    <c:set var='deleteurl' value="/main/deletecompany?companyId=${company.id}"/>
			   <div class="companies-list"><a onClick=goto(${company.id}) href="#">${company.preferences.tradingName} - ${company.registeredAddress.countryOrRegion} </a> <a class="delete_company" href= '${deleteurl}' ><i18n:i18n msg='delete'/></a></div>
		   </c:forEach> 
	    </c:if>
	    </li>
	   </ul>
	  </div>
    </div>
    <div class="form-bottom-options">
      <a style="float:left" href="/main/logout"><i18n:i18n msg='logoutHTML'/></a>
      <a style="float:right" href="/main/deleteAccount"><i18n:i18n msg='deleteAccount'/></a>
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