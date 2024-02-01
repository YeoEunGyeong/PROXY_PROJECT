<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script>
        $(document).ready(function () {
            $('#fileBtn').click(function () {
                var fileItem = $('<div class="file-item"></div>');

                var input = $('<input type="file" name="uplfiles" multiple>');
                input.change(function () {
                    checkFile(input[0].files[0]); // 체크 함수 호출
                });

                var label = $('<label></label>').append(input);

                var cancelBtn = $('<span class="cancel-btn">X</span>');
                cancelBtn.click(function () {
                    fileItem.remove();
                });

                fileItem.append(label).append(cancelBtn);
                $('#fileContainer').append(fileItem);
            });

         // 파일 체크 함수
            function checkFile(file) {
                if (file) {
                    // 이미지 형식 체크
                    var allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
                    if (allowedTypes.indexOf(file.type) === -1) {
                        alert('이미지 파일만 등록 가능합니다.');
                        clearFileInput(); // 파일 입력 비우기
                        return;
                    }

                    // 이미지 사이즈 체크
                    var reader = new FileReader();

                    // 파일 읽기가 완료되면 실행되는 이벤트 핸들러
                    reader.onload = function (e) {
                        var img = new Image();
                        img.src = e.target.result;

                        img.onload = function () {
                            // 이미지 사이즈 검사
                            var maxWidth = 500;
                            var maxHeight = 500;

                            if (img.width > maxWidth || img.height > maxHeight) {
                                // 차원 초과시 경고후 해당 이미지의 차원도 보여줌
                                alert('이미지 차원은 500x500 픽셀 이하여야 합니다.');
                                clearFileInput(); // 파일 입력 필드 초기화
                            }
                        };
                    };

                    // 파일을 읽기 시작
                    reader.readAsDataURL(file);
                }
            }

            // 파일 입력 비우는 함수
            function clearFileInput() {
                $('input[name="uplfiles"]').val('');
            }
        });
    </script>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <h2>글쓰기 폼</h2>

	<form action="boardWrite" method="post" enctype="multipart/form-data">
    	<div>
	        <label for="author">작성자:</label>
	        <input type="text" id="author" name="author" required><br>
	
	        <label for="id">ID:</label>
	        <input type="text" id="id" name="id" required><br>
	
	        <label for="title">제목:</label>
	        <input type="text" id="title" name="title" required><br>
	
	        <label for="content">내용:</label>
	        <textarea id="content" name="content" rows="4" cols="50" required></textarea><br>
		</div>
        
		<div class="file-container" id="fileContainer">
            <!-- 파일 선택란이 동적으로 추가될 공간 -->
        </div>

        <input type="button" id="fileBtn" value="파일 추가">
        <input type="submit" value="등록">
    </form>
</body>
</html>