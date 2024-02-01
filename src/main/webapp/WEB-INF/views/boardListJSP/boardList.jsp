<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Board List</title>
</head>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
$(document).ready(function() {
    var searchForm = $("#searchForm");
    var totalPages; // 전역 변수로 추가
    var currentPage; // 전역 변수로 추가

    // 각 게시물 제목 클릭 시 상세보기 처리
    $("table tbody").on("click", "tr td a", function(event) {
        event.preventDefault(); // 기본 이벤트 동작 방지
        var seq = $(this).attr("href").split("=")[1];
        window.location.href = "detail?seq=" + seq;
    });

    // datepicker 초기화 스크립트
    $("#startDate, #endDate").datepicker();

    // 검색 버튼 클릭 시 이벤트 처리
    $("#searchButton").on("click", function () {
        var url = $("#searchForm").attr("action");
        var formData = $("#searchForm").serialize();
        updateBoardList(url + "?" + formData);
    });

    // 검색 후 페이지 이동 시 검색 조건 유지
    $("#searchForm").submit(function(event) { 
        event.preventDefault();
        var url = $(this).attr("action");
        var formData = $(this).serialize();
        updateBoardList(url + "?" + formData);
    });

    // 삭제 폼 submit 시 이벤트 처리
    $("#deleteForm").submit(function(event) {
        event.preventDefault();
    });

    // 날짜 포맷 변경 함수
    function formatDate(dateString) {
        var options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        return new Date(dateString).toLocaleDateString('en-US', options);
    }

 	// 페이지 번호 클릭 시 이벤트 처리
    $(document).on('click', '.pagination-link', function (e) {
        e.preventDefault();
        currentPage = parseInt($(this).data('page')); // Ensure currentPage is an integer
        var url = '/myapp/ajax/list?currentPage=' + currentPage;
        var formData = $("#searchForm").serialize(); // Get the form data
        updateBoardList(url + "&" + formData);
    });

	// 엑셀 다운로드 버튼 처리
	 $("#exceldownbtn").click(function () {
        // 액션 URL 설정 및 양식 제출
        $("#listfrm").attr("action", "axceldown")
                     .attr("method", "post")
                     .submit();
        
        console.log("엑셀 다운로드");
    });
 
	// boardSearch AJAX 처리
    function updateBoardList(url) {
        var currentPage;

        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (response) {
                console.log(response);

                // 기존 tbody 내용 삭제
                $("tbody").empty();

                // 각 데이터로 행 추가
                $.each(response.bList, function (index, board) {
                    var newRow = $("<tr>");
                    newRow.append("<td><input type='checkbox' name='chk' value='" + board.seq + "'></td>");
                    newRow.append("<td>" + board.seq + "</td>");
                    newRow.append("<td>" + board.mem_name + "</td>");
                    newRow.append("<td><a href='detail?seq=" + board.seq + "'>" + board.board_subject + "</a></td>");
                    newRow.append("<td>" + formatDate(board.reg_date) + "</td>");
                    newRow.append("<td>" + formatDate(board.upd_date) + "</td>");
                    newRow.append("<td>" + board.view_cnt + "</td>");

                    // 새로운 행을 tbody에 추가
                    $("tbody").append(newRow);
                });

                $("#pagination-container").empty();
                currentPage = response.currentPage;
                totalPages = response.totalPages;

                console.log(currentPage);
                console.log(totalPages);

                var blockSize = 5;
                var startBlock = Math.floor((currentPage - 1) / blockSize) * blockSize + 1;
                var endBlock = startBlock + blockSize - 1;
                endBlock = Math.min(endBlock, totalPages);

                if (currentPage >= 1) {
                    $("#pagination-container").append("<a href='#' class='pagination-link' data-page='1'>처음</a>");
                    $("#pagination-container").append("<a href='#' class='pagination-link' data-page='" + (currentPage - 1) + "'>이전</a>");
                }

                for (var i = startBlock; i <= endBlock; i++) {
                    if (i === currentPage) {
                        $("#pagination-container").append("<strong>" + i + "</strong>");
                    } else {
                        $("#pagination-container").append("<a href='#' class='pagination-link' data-page='" + i + "'>" + i + "</a>");
                    }
                }

                if (endBlock < totalPages) {
                    $("#pagination-container").append("<a href='#' class='pagination-link' data-page='" + (endBlock + 1) + "'>다음</a>");
                    $("#pagination-container").append("<a href='#' class='pagination-link' data-page='" + totalPages + "'>마지막</a>");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("AJAX Error:", textStatus, errorThrown);
                console.error("Response:", jqXHR.responseText);
            }
        });
    }
});
</script>

<body>
	<h2>Board List</h2>
	<!-- 글쓰기 -->
	<form action="insert" method="get">
		<input type="submit" value="글쓰기">
	</form>

	<!-- 엑셀 다운로드 -->
	<input type="button" id="exceldownbtn" name="exceldownbtn" value="엑셀 다운로드">
	

	<!-- 검색 -->
	<form id="searchForm" action="ajax/list" method="get">
	    <input type="text" id="search" name="search" placeholder="검색" value="${search}">
	    <select name="searchBar" class="typeBox">
	        <option value="전체" ${searchBar == '전체' ? 'selected' : ''}>전체</option>
	        <option value="제목" ${searchBar == '제목' ? 'selected' : ''}>제목</option>
	        <option value="제목/내용" ${searchBar == '제목/내용' ? 'selected' : ''}>제목/내용</option>
	        <option value="ID" ${searchBar == 'ID' ? 'selected' : ''}>ID</option>
	    </select>
	    <!-- 추가: 시작일과 종료일 datepicker -->
	    <input type="text" id="startDate" name="startDate" placeholder="시작일" value="${startDate}">
	    <input type="text" id="endDate" name="endDate" placeholder="종료일" value="${endDate}">
	    <input type="hidden" name="currentPage" value="1">
	    <input type="button" id="searchButton" value="검색">
	    <!-- <input type="submit" value="검색"> -->
	</form>
	
	<!-- 삭제 -->
	<form name="listfrm" id="listfrm" action="delete" method="post">
		<input type="submit" value="삭제">
		<table border="1">
			<thead>
				<tr>
					<th></th>
					<th>글번호</th>
					<th>작성자</th>
					<th>제목</th>
					<th>작성일</th>
					<th>수정일</th>
					<th>조회수</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="board" items="${boardList}">
					<tr>
						<td><input type="checkbox" name="chk" value="${board.seq}"></td>
						<td>${board.seq}</td>
						<td>${board.mem_name}</td>
						<td><a href="detail?seq=${board.seq}">${board.board_subject}</a>
						</td>
						<td><fmt:formatDate value="${board.reg_date}"
								pattern="yyyy/MM/dd" /></td>
						<td><fmt:formatDate value="${board.upd_date}"
								pattern="yyyy/MM/dd" /></td>
						<td>${board.view_cnt}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
    
<!-- Pagination -->
<div id="pagination-container">
    <!-- 처음과 이전 -->
    <c:if test="${currentPage >= 1}">
        <a href="#" class="pagination-link" data-page="1">처음</a>
        <a href="#" class="pagination-link" data-page="${currentPage - 1}">이전</a>
    </c:if>

    <!-- Display page numbers in blocks of 5 -->
    <c:set var="startBlock" value="${((currentPage - 1) / 5) * 5 + 1}" />
    <c:set var="endBlock" value="${startBlock + 4}" />
    <c:if test="${endBlock > totalPages}">
        <!-- If endBlock exceeds totalPages, set it to totalPages -->
        <c:set var="endBlock" value="${totalPages}" />
    </c:if>

    <c:forEach var="page" begin="${startBlock}" end="${endBlock}">
        <c:choose>
            <c:when test="${page == currentPage}">
                <!-- Highlight current page -->
                <strong>${page}</strong>
            </c:when>
            <c:otherwise>
                <a href="#" class="pagination-link" data-page="${page}">${page}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>

    <!-- 다음과 마지막 -->
    <c:if test="${currentPage < totalPages}">
        <a href="#" class="pagination-link" data-page="${currentPage + 1}">다음</a>
        <a href="#" class="pagination-link" data-page="${totalPages}">마지막</a>
    </c:if>
</div>

</body>
</html>