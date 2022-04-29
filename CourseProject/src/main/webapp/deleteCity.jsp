<%@ page isELIgnored="false" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<title>DELETE CITY PAGE</title>
</head>

<body>
	<br>
	<center>
		<h2>Список городов в БД</h2>
	</center>
	<hr><br>
	<center><h3>${emptyMessage}</h3></center>
	<c:forEach items="${allCities}" var="city">
    	ID города: ${city.id} <br>
    	Город: ${city.name} <br><br>
    	<form method="POST" action="delete">
    		<input name="id" value="${city.id}" hidden>
    		<input type="submit" value="Удалить">
    	</form>
    	<hr><br>
    </c:forEach>
</body>

</html>