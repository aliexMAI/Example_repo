<%@ page isELIgnored="false" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html lang="ru">
<!-- Подключаем заголовок -->
<jsp:include page="headerBS.jsp" />

<body class="bg-image" style="background-image: url(/background-9.jpg); height: 100vh">

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
                    <a class="nav-link" href="#">Создать/обновить город в базе данных</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/travel/city/delete">Удалить город из базы данных</a>
                </li>
            </ul>

        </div>
    </div>
</nav>

    <div class="container">

        <div class="row">
            <div class="col-12 text-center">
                <h1>Внесение изменений в базу данных</h1><br>
            </div>
        </div>

        <div class="row">
            <div class="col-12 text-center">
                <h3>Создание города в базе данных</h3><br>
            </div>

            <div class="col-12 text-center gy-2">
                 <h5>${message}</h5>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <form method="POST" action="">

                        <div class="mb-3">
                            <label for="name" class="form-label">Название города</label>
                            <input type="text" id="name" name="name" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label for="description" class="form-label">Описание города</label>
                            <textarea type="text" id="description" name="description" class="form-control" rows="10" ></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="climate" class="form-label">Климат города</label>
                            <textarea type="text" id="climate" name="climate" class="form-control" rows="10"></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="latitude" class="form-label">Широта местоположения</label>
                            <input type="text" id="latitude" name="latitude" class="form-control"
                                   placeholder="Пример: 23/12/45"  aria-describedby="latitudeHelpBlock">
                            <div id="latitudeHelpBlock" class="form-text">
                                Введите, пожалуйста, широту в формате градусы/минуты/секунды
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="longitude" class="form-label">Долгота местоположения</label>
                            <input type="text" id="longitude" name="longitude"  class="form-control"
                                   placeholder="Пример: -45/34/7"  aria-describedby="longitudeHelpBlock">
                            <div id="longitudeHelpBlock" class="form-text">
                                Введите, пожалуйста, долготу в формате градусы/минуты/секунды
                             </div>
                        </div>
                        <div class="mb-3">
                            <label for="population" class="form-label">Население</label>
                            <input type="text" id="population" name="population" class="form-control">
                        </div>
                           <button type="submit" class="btn btn-primary">Создать</button>
                </form>
                <br>


            </div>
        </div>

        <br>
            <div class="row">
                <div class="col-12 text-center">
                    <h2>Редактирование информации о городе</h2>
                </div>
                 <c:forEach items="${allCities}" var="city">
                     <div class="col-12 col-sm-6 col-lg-4 col-xl-3 text-center g-4">
                         <div class="card h-100" style="color: #4B515D; border-radius: 15px;">
                             <div class="card-body">
                                <h5>${city.name}</h5>
                                <a href="/travel/city/update?cityId=${city.id}">
                                <button type="submit" class="btn btn-primary">Редактировать</button>
                                </a><br>
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