<%@ page isELIgnored="false" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<title>CITY PAGE</title>
	<meta charset="utf-8">
</head>

<body>
	<br>
	<center>
		<h1>Информация о городе ${name}</h1>
	</center>
	${description}
    <h2>Климат</h2>
	${climate}
	<br><br>
	Население: ${population} человек.<br>
	Географические координаты: <br><br>
	Города поблизости (${radius} км): <br>
	<ul>
		<c:forEach items="${cities}" var="city">
			<a href="/travel/city?cityId=${city.id}">
				<li>${city.name} - ${city.distance} км</li>
			</a>
		</c:forEach>
	</ul>
	<hr>
	<center>
		<a href="/travel">
			<input type="button" value=" На главную ">
		</a>
	</center>
</body>

</html>