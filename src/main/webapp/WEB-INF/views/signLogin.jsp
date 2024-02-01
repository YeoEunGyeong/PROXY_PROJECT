<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <style>
        body {
            background-color: #fafafa;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        #loginfrm {
            max-width: 350px;
            width: 100%;
            padding: 40px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #262626;
            font-weight: 600;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 20px;
            box-sizing: border-box;
            border: 1px solid #dbdbdb;
            border-radius: 4px;
            background-color: #fafafa;
            color: #262626;
        }

        input[type="submit"] {
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
            display: block;
            margin: 0 auto;
        }

        input[type="submit"]:hover {
            background-color: #6c3483;
        }
    </style>
</head>
<body>
    <form id="loginfrm" name="loginfrm" action="login" method="post">
        <label for="username">아이디:</label>
        <input type="text" id="userid" name="userid" required>
        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" required>
        <input type="submit" value="로그인">
    </form>
    
    <c:if test="${not empty error}">
        <script>
            alert("${error}");
        </script>
    </c:if>
</body>
</html>
