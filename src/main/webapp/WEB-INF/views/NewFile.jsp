<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f3f3f3;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        table {
            width: 80%;
            border-collapse: collapse;
            background-color: #fff;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
            font-size: 14px;
        }

        th {
            background-color: #673AB7;
            color: #fff;
        }

        td[data-job]:hover {
            cursor: pointer;
            background-color: #E1BEE7;
        }
    </style>
</head>
<body>

<table>
    <tbody>
        <tr>
            <td>kim</td>
            <td>20</td>
            <td data-job="student">student</td>
        </tr>
        <tr>
            <td>lee</td>
            <td>25</td>
            <td data-job="programmer">programmer</td>
        </tr>
        <tr>
            <td>choi</td>
            <td>30</td>
            <td data-job="doctor"></td>
        </tr>
    </tbody>
</table>

<script>
    // 첫 번째 문제: choi의 직업에 'doctor' 추가
    $('td[data-job="doctor"]').text('doctor');

    // 두 번째 문제: 각 직업에 대한 마우스 오버 효과
    $('td[data-job]').hover(
        function() {
            var job = $(this).data('job');
            $(this).attr('title', getJobTooltip(job));
        },
        function() {
            $(this).removeAttr('title');
        }
    );

    function getJobTooltip(job) {
        switch (job) {
            case 'student':
                return '학생';
            case 'programmer':
                return '개발자';
            case 'doctor':
                return '의사';
            default:
                return '';
        }
    }
</script>

</body>
</html>