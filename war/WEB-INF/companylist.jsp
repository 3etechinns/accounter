<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
        <meta content="IE=100" http-equiv="X-UA-Compatible">
		<link rel="shortcut icon" href="../images/favicon.ico" />
		<% String version = application.getInitParameter("version"); %>
      <link type="text/css" rel="stylesheet" href="../css/ss.css?version=<%= version%>">
  </head>
  <body>
  <div id="commanContainer">
	<div>
		<img src="../images/Accounter_logo_title.png" class="accounterLogo" />
		
	</div>
    <div class="company_lists">
       
       
        <c:if test="${message != null}">
       		<div class="common-box create-company-message">${message}</div>
        </c:if>
       <div class="form-box">
      	<div> <a href="/createcompany" class="create_new_company">Create New Company </a></div>
      	<ul>
	    <c:if test="${companeyList != null}">
		   <c:forEach var="company" items="${companeyList}">
			   <c:set var='url' value="/companies?companyId=${company.id}"/>
			    <c:set var='deleteurl' value="/deletecompany?companyId=${company.id}"/>
			   <div class="companies-list"><a href=${url}>${company.companyName}</a> <a href=${deleteurl}>Delete</a></div>
		   </c:forEach>
	    </c:if>
	    
	   </ul>
	  </div>
    </div>
    <div class="form-bottom-options">
      <a href="/do/logout">Logout</a>
    </div>
   </div>
   
   <!-- Footer Section-->
   
   <div id="mainFooter"  >
   <div>
      <span>&copy Vimukti Technologies Pvt Ltd</span> |
      <a target="_blank" href="/site/termsandconditions"> Terms & Conditions </a> |
      <a target="_blank" href="/site/privacypolicy"> Privacy Policy </a> |
      <a target="_blank" href="/site/support"> Support </a>
   </div>
</div>
   
   <script type="text/javascript" charset="utf-8">
			var is_ssl = ("https:" == document.location.protocol);
			var asset_host = is_ssl ? "https://s3.amazonaws.com/getsatisfaction.com/" : "http://s3.amazonaws.com/getsatisfaction.com/";
			document.write(unescape("%3Cscript src='" + asset_host + "javascripts/feedback-v2.js' type='text/javascript'%3E%3C/script%3E"));
		</script>
		<script type="text/javascript" charset="utf-8">
			var feedback_widget_options = {};
			
			feedback_widget_options.display = "overlay";  
  			feedback_widget_options.company = "vimukti";
			feedback_widget_options.placement = "left";
			feedback_widget_options.color = "#222";
			feedback_widget_options.style = "idea";
		
			var feedback_widget = new GSFN.feedback_widget(feedback_widget_options);
		</script>
  </body>
</html>