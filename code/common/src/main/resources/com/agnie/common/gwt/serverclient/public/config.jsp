<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.google.inject.Injector" %>
<%@ page import="com.agnie.common.gwt.serverclient.client.helper.URLConfiguration" %>
<%@ page import="com.agnie.common.gwt.serverclient.client.helper.URLGenerator" %>
<script>
<%
	Injector inj = (Injector) pageContext.getServletContext().getAttribute(Injector.class.getName());
	URLConfiguration urc = (URLConfiguration) inj.getInstance(URLConfiguration.class);
	out.println("var constants = new Object();");
	out.println("constants['" + URLGenerator.USER_ADMIN_ROOT_ENDPOINT + "'] = '" + urc.get3ABaseURL() +"'" );
	out.println("constants['" + URLGenerator.RECAPTCHA_PUBLIC_KEY + "'] = '" + urc.recaptchaPublicKey() +"'" );
%>

</script>
