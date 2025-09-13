<%@ page import="java.io.*" %>
<pre>
contextPath: <%= request.getContextPath() %>
realPath:    <%= application.getRealPath("/") %>
spring-mvc-servlet.xml exists? 
  <%= new File(application.getRealPath("/WEB-INF/spring-mvc-servlet.xml")).exists() %>

WEB-INF content:
<%
File d = new File(application.getRealPath("/WEB-INF"));
if (d != null && d.exists()) {
  for (String f : d.list()) out.println(" - " + f);
} else {
  out.println(" (WEB-INF not found)");
}
%>
</pre>
