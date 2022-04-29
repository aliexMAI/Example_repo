<%@ page contentType="text/html;charset=utf-8" %>
<%@ page isELIgnored = "false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <title>CREATE CITY PAGE</title>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>

<header>
	<h1>Внесение нового города в базу данных</h1>
</header>

	<content>
	     <div>
            <form method="POST" action="">
                <div class="main">
                    <div class="field">
                        <label for="name">Название города</label>
                        <input type="text" id="name" name="name">
                    </div>
                    <div class="field">
                        <label for="description">Описание города</label>
                        <input type="text" id="description" name="description">
                    </div>
                    <div class="field">
                        <label for="climate">Климат города</label>
                        <input type="text" id="climate" name="climate">
                    </div>
                    <div class="field">
                        <label for="latitude">Широта в формате xx/xx/xx</label>
                        <input type="text" id="latitude" name="latitude">
                    </div>
                    <div class="field">
                        <label for="longitude">Долгота в формате xx/xx/xx</label>
                        <input type="text" id="longitude" name="longitude">
                    </div>
                    <div class="field">
                        <label for="population">Население</label>
                        <input type="text" id="population" name="population">
                    </div>
                       <input type="submit" value="Создать">
                </div>
            </form>
         </div>
         <br>
         <p>${message}</p>
	</content>
    <br>
    <content>
        <h2>Список городов в БД:</h2>
        <ul>
            <c:forEach items="${allCities}" var="city">
            <li> ${city.name}, id: ${city.id}    
                <a href="/travel/city/update?cityId=${city.id}">
                    <input type="button" value="Редактировать">
                </a>
            </li><br> 
            </c:forEach>
        </ul>
    </content>
</body>
</html>