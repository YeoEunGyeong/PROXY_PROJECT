<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>

<script type="text/javascript">
$(document).ready(function () {
});
</script>
</head>
<body>
    <h2>Board Detail</h2>
    <form action="detail" method="get">
    </form>
    <table border="1">
        <thead>
            <tr>
                <th>글번호</th>
                <th>작성자</th>
                <th>ID</th>
                <th>제목</th>
                <th>내용</th>
                <th>작성일</th>
                <th>조회수</th>
            </tr>
        </thead>
         <tbody>
            <tr>
                <td>${board.seq}</td>
                <td>${board.mem_name}</td>
                <td>${board.mem_id}</td>
                <td>${board.board_subject}</td>
                <td>${board.board_content}</td>
                <td><fmt:formatDate value="${board.reg_date}" pattern="yyyy-MM-dd" /></td>
                <td>${board.view_cnt}</td>
            </tr>
        </tbody>
        </table>
        
		<c:if test="${not empty file}">
		    <ul>
		        <c:forEach var="fileItem" items="${file}">
		            <li>
		                <a href="download?fileName=${fileItem.real_name}">
		                    ${fn:escapeXml(fileItem.real_name)}
		                </a>
		            </li>
		        </c:forEach>
		    </ul>
		</c:if>

		<form action="update" method="post">
		    <input type="hidden" name="seq" value="${board.seq}" />
		    <label for="author">작성자:</label>
		    <input type="text" id="author" name="mem_name" /><br>
		    <label for="title">제목:</label>
		    <input type="text" id="title" name="board_subject" /><br>
		    <label for="content">내용:</label>
		    <textarea id="content" name="board_content"></textarea><br>
		    <input type="submit" value="수정">
		</form>
</body>
</html>