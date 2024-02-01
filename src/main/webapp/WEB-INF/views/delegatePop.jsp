<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>대리결재</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
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

        #main-container {
            max-width: 600px;
            width: 100%;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            overflow: auto;
            max-height: 80vh;
        }

        p {
            margin-bottom: 15px;
        }

        select {
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid #dbdbdb;
            border-radius: 4px;
            background-color: #fafafa;
            margin-right: 10px;
        }

        #displayRank
        ,#subSigner
        ,#mainSigner {
            font-size: 14px;
            font-weight: 600;
            margin-bottom: 10px;
            color: #8e44ad;
        }

        form {
            display: flex;
            justify-content: space-between;
        }

        input[type="submit"], input[type="button"] {
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

        input[type="submit"]:hover, input[type="button"]:hover {
            background-color: #6c3483;
        }
    </style>
    <script>
        $(document).ready(function () {
        	$("#proxyMember").change(function () {
                var selectedId = $(this).val();
                console.log("Selected ID:", selectedId); // 선택된 member의 id값 확인
                
                // Ajax 요청을 통해 직급 가져오기
                $.ajax({
                    url: 'getMemberRank', 
                    type: 'POST',
                    data: { memberId: selectedId }, // 선택된 대리결재자의 ID를 전송
                    
                    success: function (rank) {
                        console.log(rank);
                        $("#displayRank").text("직급: " + rank); 
                    },
                    error: function (xhr, status, error) {
                        console.error("Error occurred while fetching rank:", error);
                        console.error("Error status:", status); // 에러 상태를 콘솔에 로깅
                        console.error("Error xhr:", xhr); // 에러 객체를 콘솔에 로깅
                    }
                });
            });
        	
            $("#approvalBtn").click(function () {
                var selectedId = $("#proxyMember").val();
                //console.log("Selected ID:", selectedId); // 선택된 member의 id값 확인

                // 현재 사용자의 ID 가져오기
                var currentId = "${userId}";
                //console.log("Current ID:", currentId); // 현재 사용자의 ID 확인

                $('<input>').attr({
                    type: 'hidden',
                    id: 'proxyId',
                    name: 'proxyId',
                    value: selectedId
                }).appendTo('#approveFrm');

                $('<input>').attr({
                    type: 'hidden',
                    id: 'apperId',
                    name: 'apperId',
                    value: currentId
                }).appendTo('#approveFrm');

                // 확인용 console.log
                //console.log("Proxy ID Input Value:", $("#proxyId").val());
                //console.log("Apper ID Input Value:", $("#apperId").val());

                // 폼 제출
                $("#approveFrm").submit();
            });

            $("#cancelBtn").click(function () {
                window.close();
            });
        });
    </script>
</head>
<body>
    <div id="main-container">
        <form id="approveFrm" action="approveInsert" method="post">
	        <p id="subSigner">대리결재자 :
                <select name="proxyMember" id="proxyMember">
                    <c:forEach var="member" items="${mList}">
                        <option value="${member.id}">${member.name}</option>
                    </c:forEach>
                </select>
        	</p>
        </form>
        <p id="displayRank">직급: </p>
        <p id="mainSigner">대리자: ${userName}(${userRank})</p>
        
        <form>
            <input type="submit" id="cancelBtn" value="취소">
            <input type="button" id="approvalBtn" value="승인">
        </form>
    </div>
</body>
</html>
