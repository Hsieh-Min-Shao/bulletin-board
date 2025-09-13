<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %> <%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>${isCreate ? '新增公布事項' : '編輯公布事項'}</title>
    <%@ include file="/WEB-INF/views/_layout/head.jspf" %>
  </head>
  <body>
    <div class="container">
      <c:choose>
        <c:when test="${isCreate}">
          <c:url var="formAction" value="/announce/create" />
        </c:when>
        <c:otherwise>
          <c:url var="formAction" value="/announce/${announce.id}/edit" />
        </c:otherwise>
      </c:choose>
      <form action="${formAction}" method="post" enctype="multipart/form-data">
        <!-- 標題 -->
        <div class="row g-2 align-items-center mb-3">
          <div class="col-2">
            <label for="title" class="col-form-label">標題</label>
          </div>
          <div class="col-10">
            <input
              type="text"
              id="title"
              name="title"
              class="form-control form-control-sm"
              value="${announce.title}"
              required
            />
          </div>
        </div>

        <!-- 發佈日期 -->
        <div class="row g-2 align-items-center mb-3">
          <div class="col-2">
            <label for="publishDate" class="col-form-label">發佈日期</label>
          </div>
          <div class="col-10">
            <input
              type="date"
              id="publishDate"
              name="publishDate"
              class="form-control form-control-sm"
              value="${announce.publishDate}"
              required
            />
          </div>
        </div>

        <!-- 截止日期 -->
        <div class="row g-2 align-items-center mb-3">
          <div class="col-2">
            <label for="dueDate" class="col-form-label">截止日期</label>
          </div>
          <div class="col-10">
            <input
              type="date"
              id="dueDate"
              name="dueDate"
              class="form-control form-control-sm"
              value="${announce.dueDate}"
            />
          </div>
        </div>

        <!-- 公布者 -->
        <div class="row g-2 align-items-center mb-3">
          <div class="col-2">
            <label for="inputPassword6" class="col-form-label">公布者</label>
          </div>
          <div class="col-10">
            ${announce.publisher}
            <input
              type="hidden"
              name="publisher"
              value="${announce.publisher}"
            />
          </div>
        </div>

        <!-- 文字編譯 -->
        <div class="mb-3">
          <textarea id="editor" name="content">${announce.content}</textarea>
        </div>

        <!-- 檔案上傳（多檔） -->
        <div class="mb-3">
          <label class="form-label" for="attachments">附件上傳</label>
          <input
            class="form-control"
            type="file"
            id="attachments"
            name="attachments"
            multiple
            accept="*/*"
          />
        </div>

        <!-- 舊檔案 -->
        <c:if test="${!isCreate and not empty annFiles}">
          <div class="form-text">已存在的檔案</div>

          <c:forEach var="annFile" items="${annFiles}">
            <div
              class="row mb-2 annFileObj"
              data-file-id="${annFile.id}"
              data-ann-id="${announce.id}"
            >
              <div class="col-11">${annFile.originalName}</div>
              <div
                class="col-1 btn-close"
                aria-label="Close"
                style="cursor: pointer"
              ></div>
            </div>
          </c:forEach>
        </c:if>

        <!-- 送出 -->
        <div class="d-grid gap-2">
          <button type="submit" class="btn btn-outline-secondary btn-sm mb-3">
            <c:choose>
              <c:when test="${isCreate}">確定新增</c:when>
              <c:otherwise>確定修改</c:otherwise>
            </c:choose>
          </button>
        </div>
      </form>
    </div>
  </body>

  <%@ include file="/WEB-INF/views/_layout/foot.jspf" %>
  <script>
    $(function () {
      // 文字編譯器
      ClassicEditor.create(document.querySelector("#editor"), {
        toolbar: [
          "heading",
          "|",
          "bold",
          "italic",
          "link",
          "bulletedList",
          "numberedList",
          "blockQuote",
          "insertTable",
          "undo",
          "redo",
        ],
      })
        .then((newEditor) => {
          console.log("CKEditor 5 初始化成功", newEditor);
          editor = newEditor;
        })
        .catch((error) => {
          console.error("CKEditor 5 初始化失敗", error);
        });
    });

    // 刪除檔案
    const ctx = "${pageContext.request.contextPath}";
    $(".annFileObj").on("click", ".btn-close", function () {
      const $btn = $(this).parent(".annFileObj");
      const annFileId = $btn.data("file-id");
      const annId = $btn.data("ann-id");
      $.ajax({
        url: ctx + "/announce/" + annId + "/files/" + annFileId,
        type: "DELETE",
        success: function (res) {
          $btn.remove();
          // todo  記得彈跳窗
        },
        error: function (e) {
          alert("刪除失敗" + e);
        },
      });
    });
  </script>
</html>
