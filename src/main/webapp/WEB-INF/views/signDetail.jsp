<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상세 조회</title>
    <style>
        body {
            background-color: #fafafa;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
            color: #262626;
        }

        #detail-container {
            max-width: 600px;
            width: 100%;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        #approval-options {
            display: table;
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        .option-row {
            display: table-row;
        }

        .option-container {
            display: table-cell;
            padding: 10px;
            border: 1px solid #ddd; /* Border color */
            text-align: center;
            width: 33.33%; /* Set equal width for each cell */
        }

        .option-container label {
            display: block;
            font-weight: 600;
            margin-bottom: 5px;
        }

        #approval-options input {
            display: block;
            margin: 0 auto;
        }

        #post-info {
            margin-bottom: 20px;
        }

        #post-info span {
            font-weight: 600;
            margin-right: 5px;
        }

        #post-title,
        #post-content {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
            border: 1px solid #dbdbdb;
            border-radius: 4px;
            background-color: #fafafa;
            margin-bottom: 20px;
        }

        #action-buttons {
            display: flex;
            justify-content: space-between;
        }

        .action-button {
            background-color: #8e44ad;
            color: #fff;
            padding: 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 600;
            letter-spacing: 1px;
            transition: background-color 0.3s;
        }

        .action-button:hover {
            background-color: #6c3483;
        }
        
        #history-list {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
        }

        #history-list th,
        #history-list td {
            padding: 12px;
            text-align: center;
            border: 1px solid #ddd;
        }

        #history-list th {
            background-color: #8e44ad;
            color: #fff;
        }

        #history-list td {
            background-color: #fff;
            color: #262626;
        }

        #history-list tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        #history-list tr:hover {
            background-color: #e0e0e0;
        }
    </style>
</head>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script>
    $(document).ready(function() {
        $('<input>').attr({
            type: 'hidden',
            id: 'hidden-subject',
            name: 'hiddenSubject',
            value: '${board.subject}'
        }).appendTo('#detailfrm');

        $('<input>').attr({
            type: 'hidden',
            id: 'hidden-content',
            name: 'hiddenContent',
            value: '${board.content}'
        }).appendTo('#detailfrm');
        
        $('<input>').attr({
            type: 'hidden',
            id: 'hidden-id',
            name: 'hiddenId',
            value : '${board.id}'
        }).appendTo('#detailfrm');
        
     	// 버튼 클릭 이벤트 처리
        $(".action-button").click(function() {
            $("#detailfrm").attr("action", "writeupd");
            $("#hidden-subject").val($("#post-title").val());
            $("#hidden-content").val($("#post-content").val());
            $("#hidden-id").val();
            $("#detailfrm").submit();
        });
        
	     // 버튼 숨김 처리
	     $("button[value='reject']").toggle(!("${userRank}" === "사원" || "${userRank}" === "대리"));
	     $("button[value='save']").toggle(!("${userRank}" === "과장" || "${userRank}" === "부장"));
	

		// 사원, 대리이면서 결재상태가 결재요청, 결재중, 결재완료인 경우 결재, 반려 숨김
		if (("사원" === "${userRank}" || "대리" === "${userRank}") && ("APPROVE" === "${board.signStatus}" || "APPROVEWAIT" === "${board.signStatus}" || "ACCEPT" === "${board.signStatus}")) {
		    $("button[value='save'], button[value='approve']").hide();
		}
		
		// 과장이면서 결재상태가 결재중, 결재완료인 경우 결재, 반려 숨김
		if ("과장" === "${userRank}" && ("APPROVEWAIT" === "${board.signStatus}" || "ACCEPT" === "${board.signStatus}")) {
		    $("button[value='approve'], button[value='reject'], button[value='save']").hide();
		    
		    if ("APPROVE" === "${board.signStatus}") {
		        $("button[value='save']").hide();
		    }
		}
		
		// 부장이면서 결재상태가 결재완료, 반려인 경우 결재, 반려 숨김
		if ("부장" === "${userRank}" && ("ACCEPT" === "${board.signStatus}" || "REJECT" === "${board.signStatus}")) {
		    $("button[value='approve'], button[value='reject'], button[value='save']").hide();
		    
		    if ("APPROVEWAIT" === "${board.signStatus}") {
		        $("button[value='save']").hide();
		    }
		}

		
        // 체크박스 상태 업데이트 함수
        function updateCheckboxStates() {
            var signStatus = "${board.signStatus}";
            $("#approval-request, #approval-manager, #approval-director").prop("checked", false);

            // 결재요청
            if (signStatus === "APPROVE") {
                $("#approval-request").prop("checked", true);
            } 
            // 결재중 (과장)
            else if (signStatus === "APPROVEWAIT") {
                $("#approval-request, #approval-manager").prop("checked", true);
            } 
            // 결재완료 (부장)
            else if (signStatus === "ACCEPT") {
                $("#approval-request, #approval-manager, #approval-director").prop("checked", true);
            }
        }
        updateCheckboxStates();
    });
</script>
<body>
    <div id="detail-container">
        <form id="detailfrm" action="/writeupd" method="POST">
            <div id="approval-options" class="option-row">
                <div class="option-container">
                    <label for="approval-request">결재 요청</label>
                    <input type="checkbox" id="approval-request" disabled>
                </div>

                <div class="option-container">
                    <label for="approval-manager">과장</label>
                    <input type="checkbox" id="approval-manager" disabled>
                </div>

                <div class="option-container">
                    <label for="approval-director">부장</label>
                    <input type="checkbox" id="approval-director" disabled>
                </div>
            </div>

            <div id="post-info">
                <span>글번호:</span>${board.seq}<br>
                <span>작성자:</span>${board.name} ${board.id}
                
            </div>
            <input type="hidden" name="seq" value="${board.seq}">
			
			<label for="post-title">제목:</label>
			<c:choose>
			    <c:when test="${(board.signStatus eq 'SAVE' || board.signStatus eq 'REJECT') && board.id eq userId}">
			        <!-- SIGN_STATUS가 SAVE이거나 REJECT이고 board.id와 userId가 일치하는 경우 수정 가능한 input 필드 렌더링 -->
			        <input type="text" id="post-title" name="subject" value="${board.subject}">
			        <input type="hidden" id="hidden-subject" name="hiddenSubject" value="${board.subject}">
			    </c:when>
			    <c:otherwise>
			        <!-- SIGN_STATUS가 SAVE가 아니거나 REJECT가 아니거나 board.id와 userId가 일치하지 않는 경우 읽기 전용 input 필드 렌더링 -->
			        <input type="text" id="post-title" name="subject" value="${board.subject}" readonly>
			    </c:otherwise>
			</c:choose>
			
			<label for="post-content">내용:</label>
			<c:choose>
			    <c:when test="${(board.signStatus eq 'SAVE' || board.signStatus eq 'REJECT') && board.id eq userId}">
			        <!-- SIGN_STATUS가 SAVE이거나 REJECT이고 board.id와 userId가 일치하는 경우 수정 가능한 textarea 렌더링 -->
			        <textarea id="post-content" name="content">${board.content}</textarea>
			        <input type="hidden" id="hidden-content" name="hiddenContent" value="${board.content}">
			    </c:when>
			    <c:otherwise>
			        <!-- SIGN_STATUS가 SAVE가 아니거나 REJECT가 아니거나 board.id와 userId가 일치하지 않는 경우 읽기 전용 textarea 렌더링 -->
			        <textarea id="post-content" name="content" readonly>${board.content}</textarea>
			    </c:otherwise>
			</c:choose>

            <div id="action-buttons">
                <button class="action-button" name="action" value="reject">반려</button>
                <button class="action-button" name="action" value="save">임시저장</button>
                <button class="action-button" name="action" value="approve">결재</button>
            </div>
        </form><br>
        
        <table id="history-list">
            <thead>
                <tr>
                    <th>번호</th>
                    <th>결재자</th>
                    <th>결재일</th>
                    <th>결재상태</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="history" items="${historyList}">
                    <tr>
                        <td>${history.historySeq}</td>
                        <td>${history.name}<c:if test="${not empty history.apperName}">(${history.apperName})</c:if></td>
                        <td>${history.signDate}</td>
                        <td>${history.signStatus}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
