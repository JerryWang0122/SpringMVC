<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%-- JSTL 標籤 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%-- Spring Form 表單標籤 --%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="zh-Hant-TW">
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, user-scalable=yes, minimum-scale=1.0, maximum-scale=3.0">
    <meta charset="UTF-8">
    <title>User 首頁</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css">
</head>
<body style="padding: 15px">
    <h1>User 資料維護</h1>
    <hr>
    <table>
        <tr>
            <%-- User 表單 --%>
            <td></td>
            <%-- User 圖表 --%>
            <td></td>
        </tr>

        <tr>
            <%-- User 列表 --%>
            <td colspan="2">
                <%@include file="userlist.jspf"%>
            </td>
        </tr>
    </table>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
