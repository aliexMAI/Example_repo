<%@ page isELIgnored="false" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<title>UPDATE CITY PAGE</title>
</head>

<body>
	<br>
	<h2>Редактирование города ${city.name}</h2>
	ID: ${city.id} <br><br>
	<form method="POST" action="update">
    	<input name="id" value="${city.id}" hidden>
    	<div class="field">
            <label for="name">Название города</label>
            <p><input name="textfield" type="text" id="name" value="${city.name}"></p>
        </div>
    </form>

	<hr>
	<center>
		<a href="/travel/city/create">
			<input type="button" value=" Назад ">
		</a>
	</center>
</body>

</html>