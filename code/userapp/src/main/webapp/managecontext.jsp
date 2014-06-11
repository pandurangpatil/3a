<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head profile="http://www.w3.org/2005/10/profile">
<link rel="icon" type="image/png" href="images/favicon.png">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<!--                                                               -->
<!-- Consider inlining CSS to reduce the number of requested files -->
<!--                                                               -->
<link type="text/css" rel="stylesheet" href="managecontext.css">
<link type="text/css" rel="stylesheet" href="ManageContext/ua-common.css">
<!-- <link type="text/css" rel="stylesheet" href="Buttons.css"> -->

<!--                                           -->
<!-- Any title is fine                         -->
<!--                                           -->
<title>User Administration</title>

<!--                                           -->
<!-- This script loads your compiled module.   -->
<!-- If you add any GWT meta tags, they must   -->
<!-- be added before this line.                -->
<!--                                           -->
<script type="text/javascript" src="ManageContext/ManageContext.nocache.js"></script>
</head>

<!--                                           -->
<!-- The body can have arbitrary html, or      -->
<!-- you can leave the body empty if you want  -->
<!-- to create a completely dynamic UI.        -->
<!--                                           -->
<body class="useradmin-scope">
	<div id="pageLoader"
		style="position: fixed; z-index: 1; font-size: 16; left: 50%; top: 50%; height: 20px; width: 30px;">
		<img src="images/page-loader.gif">
	</div>

	<!-- OPTIONAL: include this if you want history support -->
	<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>

	<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
	<noscript>
		<div
			style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
			Your web browser must have JavaScript enabled in order for this
			application to display correctly.</div>
	</noscript>
	<div id="header" style="height: 65px;"></div>
	<section id="content" class="content"></section>
	<div id="footer"></div>
</body>
</html>
