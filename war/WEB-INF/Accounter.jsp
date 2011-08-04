<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>

  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
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
	
    <% String version = application.getInitParameter("version"); %>
    <!--<script type="text/javascript" src="https://getfirebug.com/firebug-lite.js"></script>-->

    <script type="text/javascript">
    var tabsEnabled=["Hr","Finance","Operations","Marketing","Sales","Users","Workflows","Purchases"];
    var helpurl="${helpUrl}";
    </script>
    <!--                                                               -->
    <!-- Consider inlining CSS to reduce the number of requested files -->
    <!--                                                               -->
	<link type="text/css" rel="stylesheet" href="../css/Finance.css?version=<%= version%>">
	<link type="text/css" rel="stylesheet" href="../css/calendar.css?version=<%= version%>">
	<%
   String app = request.getHeader( "Nativeapp" );
   boolean isNative = ( app != null && !app.equals(""));
   if( isNative ){ %>
   <link type="text/css" rel="stylesheet" href="../css/native.css?version=<%= version%>">
   <% } %>
	
    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>Accounter</title>
    
    <!--CSS for loading message at application Startup-->
    <style type="text/css">
        body { overflow:hidden }
        #loading {
            border: 1px solid #ccc;
            position: absolute;
            left: 42%;
            top: 35%;
            padding: 2px;
            z-index: 20001;
            height: auto;
        }

        #loading a {
            color: #225588;
        }
        

        #loading .loadingIndicator {
            background: white;
            font: bold 13px tahoma, arial, helvetica;
            padding: 10px;
            margin: 0;
            height: auto;
            color: #444;
            
        }

        #loadingMsg {
            font: normal 10px arial, tahoma, sans-serif;
        }
      
		.login{
	 		margin:140px auto;
	 		background-color:#f2f3f5;
	 		padding:10px;
	 		border:1px solid #000;
	 		width:600px;
 		}
 
 		body{
 			background:LightSteelBlue none repeat scroll 0 0;
 		}
 
		.image{
 			text-align:center;
 		}
 
		.clearfix:after{
	 		content:".";
	 		display:block;
	 		height:0;
	 		clear:both;
	 		visibility:hidden;
	 	}
		.fieldset{
			border:1px solid #000;
			padding:10px;
			background-color:#e9edf0;
			margin:5px 0px;
			width:300px;
		}

		.fieldset table td{
			float:left;
		}
		
		.login img[src="logo.png"]{
			text-align:center;
		}
		
		.login img[src="lock.png"]{
			float:left;
		}
		
		p{
			text-align:left;
			clear:left;
			line-height:30px;
			margin:0px;
		}
		
		.loginbox{
			float:right;	
		}
		
		.loginlabel{
			font-weight:bold;
			color:#c64933;
		}
		
		.loginbox a{
			font-weight:bold;
			color:#0000ff;
			margin-left:20px;
			text-decoration:none;
		}

		#bizantraForm input[type="text"],#bizantraForm input[type="password"]{
			border:1px solid #9cb0ce;
		}

		.loginbox a:hover{
			text-decoration:underline;
		}
			
		.hiddenPic {
			display:none; 
			float:left;
			margin-top:25%;
			margin-left:50%;
		}
		
		#import_anchor{
 			color:blue;
			font-weight:normal;
			margin-bottom:0;
			margin-left:0;
			margin-right:0;
			margin-top:0;
			text-decoration:underline;
		}
		
		#Import_Account{
			margin-left:200px;
		}
    </style>
    
    

  </head>

  <!--                                           -->
  <!-- The body can have arbitrary html, or      -->
  <!-- you can leave the body empty if you want  -->
  <!-- to create a completely dynamic UI.        -->
  <!--                                           -->
  <body>
<div id="hiddenDiv" class="hiddenPic">
		<img src="/images/loader.gif" 
  			alt="Loading" title="Loading">
    		</div>

  	<!--add loading indicator while the app is being loaded-->
	<div id="loadingWrapper" style="visibility:visible">
	<div id="loading">
	    <div class="loadingIndicator">
	        <img src="/images/Main-page-loading-bar.gif" /><br/>
	    </div>
	</div>
	</div>
  
  	<script type="text/javascript">document.getElementById('loadingMsg');</script>
	<!--include the application JS-->
	<script type="text/javascript">document.getElementById('loadingMsg');</script>
	
	<!--<table class="header" id="mainHeader" style="visibility:hidden">
	   <tr>
	      <td width="25%"><img src="/images/Accounter_logo_title.png" /></td>
	      <td width="50%"><div class="companyName">${companyName}</div></td>
	      <td width="25%">
	        <ul>
	           <li><img src="images/User.png" /><a href="">${userName}</a></li>
	           <li><img src="images/Help.png" /><a href='http://help.accounter.com'>Help</a></li>
	           <li><img src="images/logout.png" /><a href='/do/logout'>Logout</a></li>
	        </ul>
	      </td>
	   </tr>
	</table>-->
	<div id="mainWindow"></div>
	<script type="text/javascript" language="javascript" src="/accounter/accounter.nocache.js"></script>
	<!--<table width="100%" id="mainFooter" style="visibility:hidden">
	   <tr>
	      <td>
		     <table width="100%">
		       <tr>
			      <td><div class="left-corner"></div></td>
			      <td width="98%"><div class="mid-repeat"></div></td>
			      <td><div class="right-corner"></div></td>
			    </tr>
		     </table>
		   </td>
	   </tr>
	   <tr>
	     <td>
	      <table width="100%">
	       <tr>
		      <td><div class="footer-left"></div></td>
		      <td width="99%">
		         <div class="feedback-option">
		             <div class="vimukti-name">
		               <a target="/blank" href="http://www.vimukti.com/">Vimukti Technologies Pvt Ltd.</a> All rights reserved
		         </div>
		         <div class="feedback-name">
		             Send your feedback to: <a target="_blank" href="/site/support">support@accounterlive.com</a>
		         </div>
		         </div>
		      </td>
		      <td><div class="footer-right"></div></td>
		    </tr>
	     </table>
	    </td>
	   </tr>
	</table>-->
	
    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    <iframe id="__printingFrame" style="width: 0; height: 0; border: 0"></iframe>
	<jsp:include page="/WEB-INF/meteor.jsp"></jsp:include>
	<iframe id="__printingFrame" style="width: 0; height: 0; border: 0"></iframe>
	 <iframe id="__printingFrame" style="width: 0; height: 0; border: 0">
        </iframe>
        <iframe id="__generatePdfFrame" style="width: 0; height: 0; border: 0" src="#">
           
        </iframe>
        
<script type="text/javascript">

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-24502570-1']);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();

</script>

 </body>
</html>
