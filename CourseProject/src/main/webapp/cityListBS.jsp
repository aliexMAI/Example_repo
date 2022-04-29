<%@ page isELIgnored="false" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="ru">
<!-- Подключаем заголовок -->
<jsp:include page="headerBS.jsp" />

<body>

    <!-- Заголовок -->
    <div class="container-fluid overflow-hidden" style="height: 300px">
        <div class="row text-center">
            <div class="col-12">
            <div class="cloud">
                <div class="cloudcontent">
                <h1>TravelApp</h1>
            </div>
                <img src="/cloud-01.png" alt="" class="cloud1">
                <img src="/cloud-02.png" alt="" class="cloud2">
                <img src="/cloud-03.png" alt="" class="cloud3">
                <img src="/cloud-04.png" alt="" class="cloud4">
            </div>
            </div>
        </div>
    </div>


    <div class="container ">

        <div class="row text-center py-10">
            <div class="col-12">
                <h4 class="text-center">${emptyMessage}</h4>
            </div>
        </div>


        <div class="row row-cols-1 row-cols-md-2 row-cols-xl-3 g-4 py-5">
            <c:forEach items="${allCities}" var="city">
            <div class="col">

                <div class="card h-100" style="color: #4B515D; border-radius: 15px;">

                    <img src="/img${city.id}.jpg" class="" alt="..." style = "max-height: 200px;">

                    <div class="card-body">
                        <div>
                            <h3 class="card-title text-center city-header">${city.name}</h3>
                            <p class="card-text">${city.description}</p>
                        </div>
                    </div>

                    <div class="card-footer d-flex align-items-center">
                        <div class="flex-grow-1 fw-weight-bold fs-6">
                            <b><div class="fw-weight-bold">Погода:<br></div>
                            <div class="fw-weight-bold">${weathers.get(city.id)}</div></b>
                        </div>
                        <div>
                            <a href="/travel/city?cityId=${city.id}" class="btn btn-outline-primary">Подробнее</a>
                        </div>
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
