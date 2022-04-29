<%@ page isELIgnored="false" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="ru">
<!-- Подключаем заголовок -->
<jsp:include page="headerBS.jsp" />

<body class="bg-image" style="background-image: url(/background.jpg); height: 100vh">

<!-- Подключаем меню -->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="/travel">Главная страница</a>

        <button class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent"
                aria-expanded="false"
                aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">

            <ul class="navbar-nav ml-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/travel/city/create">Создать/обновить город в базе данных</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Удалить город из базы данных</a>
                </li>
            </ul>

        </div>
    </div>
</nav>

    <div class="container py-10">

        <div class="row">
            <div class="col-12 text-center">
                <h1>Удаление города из базы данных</h1>
            </div>
        </div>

        <div class="col-12 text-center">
             <h5>${emptyMessage}</h5>
        </div>

        <div class="row">
             <c:forEach items="${allCities}" var="city">
                <div class="col-12 col-sm-6 col-lg-4 col-xl-3 text-center g-4">
                    <div class="card h-100" style="color: #4B515D; border-radius: 15px;">

                        <div class="card-body">
                            <h6>
                            ID города: ${city.id} <br>
                            Город: ${city.name}
                            </h6>
                            <form method="POST" action="delete">
                                <input name="id" value="${city.id}" hidden>
                                <button type="submit" class="btn btn-danger">Удалить</button>
                            </form>
                        </div>

                    </div>
                </div>

             </c:forEach>
        </div>


    </div>

<!-- Подключаем подвал -->
<jsp:include page="footerBS.jsp" />

</body>
</html>