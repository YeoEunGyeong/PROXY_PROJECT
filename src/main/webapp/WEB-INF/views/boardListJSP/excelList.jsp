<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    response.setHeader("Content-Disposition", "attachment;filename=boardList.xls");
    response.setHeader("Content-Description", "JSP Generated Data");

    // Get the excelList from the model
    List<Map<String, Object>> excelList = (List<Map<String, Object>>) request.getAttribute("excelList");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Board List Excel</title>
</head>
<body>

    <table border="1">
        <thead>
            <tr>
                <th>글번호</th>
                <th>작성자</th>
                <th>제목</th>
                <th>작성일</th>
                <th>수정일</th>
                <th>조회수</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="board" items="${excelList}">
                <tr>
                    <td>${board['SEQ']}</td>
                    <td>${board['MEM_NAME']}</td>
                    <td>${board['BOARD_SUBJECT']}</td>
                    <td>${board['REG_DATE']}</td>
                    <td>${board['UPT_DATE']}</td>
                    <td>${board['VIEW_CNT']}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>
