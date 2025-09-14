<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-Hant">
  <head>
    <meta charset="UTF-8" />
    <title>系統錯誤 - 500</title>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <%@ include file="/WEB-INF/views/_layout/header.jsp" %>
    <div class="container py-5">
      <div class="text-center">
        <h1 class="display-5 fw-bold">500</h1>
        <p class="lead mb-4">系統發生未預期的錯誤，請稍後再試。</p>
        <a
          class="btn btn-outline-secondary"
          href="<%= request.getContextPath() %>/"
          >回首頁</a
        >
      </div>
    </div>
  </body>
</html>
