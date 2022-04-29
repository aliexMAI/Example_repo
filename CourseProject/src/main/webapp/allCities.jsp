<%@ page isELIgnored="false" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<title>ALL CITIES PAGE</title>
</head>

<body>
	<br>

	<center>
		<h1>Travel/Путешествие</h1>
		<hr>
    </center>

    <center><h3>${emptyMessage}</h3></center>

    <c:forEach items="${allCities}" var="city">
    	<h3>${city.name}</h3>
    	<img src="${cityImages.get(city.id)}" width="300px" height="200px">
    	<br><br> 
    	${city.description} <br><br>
    	Погода <br> ${weathers.get(city.id)} <br><br>
    	<a href="/travel/city?cityId=${city.id}">
    		<input type="button" value=" Подробнее ">
    	</a>
    	<hr><br>
    </c:forEach>
</body>

</html>