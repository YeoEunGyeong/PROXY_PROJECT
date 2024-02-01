<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>글쓰기 화면</title>
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

        #write-container {
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
    </style>
</head>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script>
    $(document).ready(function() {
        // 버튼 클릭 이벤트 처리
        $(".action-button").click(function() {
            // 임시 저장과 결재 버튼 클릭 시 action -> write 이동
            if ($(this).val() !== "reject") {
                $("#writefrm").attr("action", "write");
            }

            $("#writefrm").submit();
        });
        
        // 게시글 작성 시는 모든 rank 반려 숨김
        if("${userRank}" === "사원" || "${userRank}" === "대리" || "${userRank}" === "과장" || "${userRank}" === "부장") {
            $("button[value='reject']").hide();
        }

    });
</script>
<body>
    <div id="write-container">
        <form id="writefrm" action="/write" method="POST">
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
                <span>글번호:</span> ${maxSeq + 1}<br>
                <span>작성자:</span> ${userName} 
            </div>

            <label for="post-title">제목:</label>
            <input type="text" id="post-title" name="subject">

            <label for="post-content">내용:</label>
            <input type="text" id="post-content" name="content">

            <div id="action-buttons">
                <button class="action-button" name="action" value="reject">반려</button>
                <button class="action-button" name="action" value="save">임시저장</button>
                <button class="action-button" name="action" value="approve">결재</button>
            </div>
        </form>
    </div>
</body>
</html>
