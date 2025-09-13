<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>瀏覽公布事項</title>
    <%@ include file="/WEB-INF/views/_layout/head.jspf" %>
    <style>
      /* 固定日期與操作欄寬度；讓標題欄自適應其餘空間 */
      th.col-date,
      td.col-date {
        width: 120px;
      }
      th.col-action,
      td.col-action {
        width: 70px;
      }
      .opDetails {
        cursor: pointer;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="d-grid gap-2 mb-1">
        <a
          class="btn btn-outline-secondary btn-sm mb-3"
          href="<c:url value='/announce/new'/>"
        >
          建立新的公告
        </a>
      </div>

      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th scope="col">標題</th>
              <th scope="col" class="col-date">發布日期</th>
              <th scope="col" class="col-date">截止日期</th>
              <th scope="col" class="col-action" colspan="2">操作</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="announce" items="${announces}">
              <tr
                data-title="${announce.title}"
                data-publisher="${announce.publisher}"
                data-publish-date="${announce.publishDate}"
                data-due-date="${announce.dueDate}"
                data-ann-id="${announce.id}"
              >
                <td class="opDetails" style="white-space: nowrap">
                  <!-- 隱藏html 內容不放在data 較安全點-->
                  <div class="ann-body d-none">${announce.content}</div>
                  ${announce.title}
                </td>
                <td class="col-date opDetails">${announce.publishDate}</td>
                <td class="col-date opDetails">${announce.dueDate}</td>
                <td class="col-action updateBtn">
                  <a
                    class="btn btn-sm btn-outline-primary w-100"
                    href="<c:url value='/announce/${announce.id}/edit'/>"
                    >修改</a
                  >
                </td>
                <td class="col-action deleteBtn" data-ann-id="${announce.id}">
                  <a class="btn btn-sm btn-danger w-100"> 刪除 </a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>

      <c:set var="pageNums" value="3" />
      <c:set
        var="blockStart"
        value="${ (page - 1) - ((page - 1) mod pageNums) + 1 }"
      />
      <c:set var="blockEnd" value="${ blockStart + pageNums - 1 }" />
      <c:if test="${blockEnd > totalPage}">
        <c:set var="blockEnd" value="${totalPage}" />
      </c:if>
      <nav>
        <ul class="pagination justify-content-center">
          <!-- 上一頁 -->
          <li class="page-item ${page <= 1 ? 'disabled' : ''}">
            <a class="page-link" href="?page=${page > 1 ? page - 1 : 1}"
              >&laquo;</a
            >
          </li>

          <!-- 當前 3 頁區間 -->
          <c:forEach var="p" begin="${blockStart}" end="${blockEnd}">
            <li class="page-item ${p == page ? 'active' : ''}">
              <a class="page-link" href="?page=${p}">${p}</a>
            </li>
          </c:forEach>

          <!-- 下一頁 -->
          <li class="page-item ${page >= totalPage ? 'disabled' : ''}">
            <a
              class="page-link"
              href="?page=${page < totalPage ? page + 1 : totalPage}"
              >&raquo;</a
            >
          </li>
        </ul>
      </nav>
    </div>

    <!-- Scrollable modal -->

    <!-- Modal -->
    <div
      class="modal fade"
      id="details"
      tabindex="-1"
      role="dialog"
      aria-modal="true"
      aria-labelledby="detail-title"
      aria-hidden="true"
    >
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="detail-title">Modal title</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <p>發布者：<span id="detail-publisher"></span></p>
            <p>發布日期：<span id="detail-publishDate"></span></p>
            <p>截止日期：<span id="detail-dueDate"></span></p>
            <span id="detail-content"></span>
            <div id="detail-files" class="mt-3 d-none">
              <div class="fw-bold mb-1">附件</div>
              <ul id="detail-file-list" class="list-unstyled mb-0"></ul>
            </div>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              data-bs-dismiss="modal"
            >
              Close
            </button>
          </div>
        </div>
      </div>
    </div>
  </body>
  <%@ include file="/WEB-INF/views/_layout/foot.jspf" %>

  <script>
    const ctx = "${pageContext.request.contextPath}";

    $(function () {
      $(".updateBtn").on("click", function (e) {
        e.stopPropagation(); // 阻止冒泡
      });

      //刪除
      $(".deleteBtn").on("click", function (e) {
        e.stopPropagation(); // 阻止冒泡
        const $btn = $(this);
        const annId = $btn.data("ann-id");
        $.ajax({
          url: ctx + "/announce/" + annId,
          type: "DELETE",

          success: function () {
            const params = new URLSearchParams(window.location.search);
            const currentPage = params.get("page") || 1;
            window.location.href = ctx + "/?page=" + currentPage;
          },
          error: function (e) {
            alert("刪除失敗" + e);
          },
        });
      });

      //公告詳細頁面
      $(document).on("click", ".opDetails", function () {
        const $row = $(this).closest("tr");

        const annId = $row.data("ann-id");
        const title = $row.data("title");
        const publisher = $row.data("publisher");
        const publishDate = $row.data("publish-date");
        const dueDate = $row.data("due-date");
        const content = $row.find(".ann-body").html(); // ✅ 從隱藏區塊取 HTML

        $("#detail-title").text(title);
        $("#detail-publisher").text(publisher);
        $("#detail-publishDate").text(publishDate);
        $("#detail-dueDate").text(dueDate);
        $("#detail-content").html(content);

        $("#details").modal("show");

        $.ajax({
          url: ctx + "/announce/" + annId + "/files",
          type: "GET", // GET | POST | PUT | DELETE | PATCH
          dataType: "json", // 預期會接收到回傳資料的格式： json | xml | html
          success: function (response) {
            const $box = $("#detail-files");
            const $ul = $("#detail-file-list").empty();
            if (Array.isArray(response) && response.length) {
              response.forEach(function (f) {
                var url =
                  ctx + "/announce/" + annId + "/files/download/" + f.id;
                $ul.append(
                  $("<li>").append(
                    $("<a>", {
                      href: url,
                      target: "_blank",
                      rel: "noopener",
                    }).text(f.originalName)
                  )
                );
              });
              $box.removeClass("d-none");
            } else {
              $box.addClass("d-none");
            }
          },
          error: function () {
            $("#detail-files").addClass("d-none");
          },
        });
      });

      //樣式
      $(document)
        .on("mouseenter", ".opDetails", function () {
          $(this).closest("tr").addClass("table-secondary");
        })
        .on("mouseleave", ".opDetails", function () {
          $(this).closest("tr").removeClass("table-secondary");
        });
    });
  </script>
</html>
