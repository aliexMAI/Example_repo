<%@ page isELIgnored="false" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="ru">
<!-- Подключаем заголовок -->
<jsp:include page="headerBS.jsp" />

<body>

    <nav class="navbar navbar-light bg-light">
      <div class="container-fluid">
        <a class="navbar-brand" href="/travel">
        На главную страницу
        </a>
      </div>
    </nav>

    <!-- Заголовок -->
    <div class="container-fluid overflow-hidden" style="height: 200px">
        <div class="row text-center">
            <div class="col-12">
            <div class="cloud">
                <div class="cloudcontent">
                <h1>${city.name}</h1>
            </div>
                <img src="/cloud-01.png" alt="" class="cloud1">
                <img src="/cloud-02.png" alt="" class="cloud2">
                <img src="/cloud-03.png" alt="" class="cloud3">
                <img src="/cloud-04.png" alt="" class="cloud4">
            </div>
            </div>
        </div>
    </div>

    <div class="container">

        <div class="row row row-cols-1 row-cols-md-2 text-center py-5" style="height: 100%;">

            <div class="col">
                <img src="/img${city.id}.jpg" class="img-fluid" alt="">
            </div>

            <div class="col py-2 fs-5">
                    <i>
                    Численность населения: ${city.population} человек.<br>
                    Географические координаты: ${latitude} &nbsp ${longitude}<br><br>
                    Города поблизости (${radius} км): <br>
                    <ul>
                        <c:forEach items="${cities}" var="city">
                            <a href="/travel/city?cityId=${city.id}">
                                ${city.name} - ${city.distance} км<br>
                            </a>
                        </c:forEach>
                    </ul>
                    </i>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <p>${city.description}</p>
            </div>
        </div>

        <div class="row">
            <div class="col-12 py-3">
                <h3 class="text-center">Климат</h3>
                <p>${city.climate}</p>
            </div>
        </div>

        <div class="row">
            <div class="col-12 text-center">
                <a href="/travel">Вернуться на главную страницу</a>
            </div>
        </div>

	</div>

<!-- Подключаем подвал -->
<jsp:include page="footerBS.jsp" />

</body>
</html>