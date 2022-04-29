package ru.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.edu.error.CustomException;
import ru.edu.error.Errors;
import ru.edu.service.CityInfo;
import ru.edu.service.CityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/travel")
public class CityController {

    private CityService cityService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Показать все города.
     */
    @GetMapping
    public ModelAndView getAllCitiesView() {

        List<CityInfo> cities = cityService.getAll();

        Map<String, String> weathers = new HashMap<>();
        for(CityInfo info: cities) {
            String temp = cityService.getWeather(info.getId());
            weathers.put(info.getId(), temp);
        }

        ModelAndView view = new ModelAndView();
        view.setViewName("/cityListBS.jsp");
        if(cities.size() == 0) {
            view.addObject("emptyMessage", "В базе данных нет городов.<br>" +
                    "Обратитесь к администратору ресурса.");
        }
        view.addObject("allCities", cities);
        view.addObject("weathers", weathers);

        return view;
    }

    /**
     * Показать один город.
     *
     * @param cityId - city id
     */
    @GetMapping("/city")
    public ModelAndView getCityView(@RequestParam("cityId") String cityId) {

        CityInfo city = cityService.getCity(cityId);
        List<CityInfo> cities = cityService.getCityNear(cityId);

        String geoLatitude = getGeoCoordinate(city.getLatitude(), "x");
        String geoLongitude = getGeoCoordinate(city.getLongitude(), "y");

        ModelAndView view = new ModelAndView();
        view.setViewName("/detailedCityBS.jsp");
        view.addObject("city", city);
        view.addObject("latitude", geoLatitude);
        view.addObject("longitude", geoLongitude);
        view.addObject("cities", cities);
        view.addObject("radius", cityService.getRadius());

        return view;
    }

    /**
     * Показать страницу создания нового города.
     */
    @GetMapping("/city/create")
    public ModelAndView getCreateCityView() {

        List<CityInfo> cities = cityService.getAll();

        ModelAndView view = new ModelAndView();
        view.addObject("allCities", cities);
        view.setViewName("/createCityBS.jsp");
        return view;
    }

    /**
     * Создать новый город.
     */
    @PostMapping("/city/create")

    public ModelAndView createCity(@RequestParam("name") String name,
                                   @RequestParam("description") String description,
                                   @RequestParam("climate") String climate,
                                   @RequestParam("latitude") String latitudeStr,
                                   @RequestParam("longitude") String longitudeStr,
                                   @RequestParam("population") String populationStr) {

        String message;
        try {
            double latitude = parseCoordinates(latitudeStr);
            double longitude = parseCoordinates(longitudeStr);
            int population = parsePopulation(populationStr);

            CityInfo cityInfo = new CityInfo();
            cityInfo.setName(name);
            cityInfo.setDescription(description);
            cityInfo.setClimate(climate);
            cityInfo.setLatitude(latitude);
            cityInfo.setLongitude(longitude);
            cityInfo.createId();
            cityInfo.setPopulation(population);
            cityService.create(cityInfo);
            cityService.saveCityImage(cityInfo.getId(), name);

            message = "Город " + name + " успешно добавлен в базу данных.";
            ModelAndView view = getCreateCityView();
            view.addObject("message", message);
            return view;

        } catch (CustomException ex) {
            message = "При внесении города " + name +
                    " в базу данных, произошла ошибка:<br>" + ex.getMessage();
            ModelAndView view = getCreateCityView();
            view.addObject("message", message);
            return view;
        }
    }

    /**
     * Показать страницу редактирования города.
     *
     * @param cityId - city id
     */
    @GetMapping("/city/update")
    public ModelAndView getUpdateCityView(@RequestParam("cityId") String cityId) {

        CityInfo city = cityService.getCity(cityId);

        String latitude = getStringCoordinate(city.getLatitude());
        String longitude = getStringCoordinate(city.getLongitude());

        ModelAndView view = new ModelAndView();
        view.setViewName("/updateCityBS.jsp");
        view.addObject("city", city);
        view.addObject("latitude", latitude);
        view.addObject("longitude", longitude);

        return view;
    }

    /**
     * Редактировать город.
     */
    @PostMapping("/city/update")
    public ModelAndView updateCity(@RequestParam("id") String id,
                                   @RequestParam("name") String name,
                                   @RequestParam("description") String description,
                                   @RequestParam("climate") String climate,
                                   @RequestParam("latitude") String latitudeStr,
                                   @RequestParam("longitude") String longitudeStr,
                                   @RequestParam("population") String populationStr) {

        String message;
        try {
            double latitude = parseCoordinates(latitudeStr);
            double longitude = parseCoordinates(longitudeStr);
            int population = parsePopulation(populationStr);

            CityInfo cityInfo = new CityInfo();
            cityInfo.setId(id);
            cityInfo.setName(name);
            cityInfo.setDescription(description);
            cityInfo.setClimate(climate);
            cityInfo.setLatitude(latitude);
            cityInfo.setLongitude(longitude);
            cityInfo.setPopulation(population);
            cityService.update(cityInfo);

            message = "Город " + name + " успешно обновлен в базе данных.";
            ModelAndView view = getUpdateCityView(id);
            view.addObject("message", message);
            return view;

        } catch (CustomException ex) {
            message = "При обновлении города " + name +
                    " в базе данных, произошла ошибка:<br>" + ex.getMessage();
            ModelAndView view = getUpdateCityView(id);
            view.addObject("message", message);
            return view;
        }
    }

    /**
     * Показать страницу удаления города.
     */
    @GetMapping("/city/delete")
    public ModelAndView getDeleteCityView() {

        List<CityInfo> cities = cityService.getAll();

        ModelAndView view = new ModelAndView();
        view.setViewName("/deleteCityBS.jsp");
        view.addObject("allCities", cities);
        if(cities.size() == 0) {
            view.addObject("emptyMessage", "База данных пуста!");
        }
        return view;
    }

    /**
     * Удалить город.
     */
    @PostMapping("/city/delete")
    public ModelAndView deleteCity(@RequestParam("id") String cityId) {

        try {
            cityService.delete(cityId);
            return getDeleteCityView();
        } catch (Exception ex) {
            return getDeleteCityView();
        }
    }

    /**
     * Получение координат в радианах из строки.
     *
     * @param value координаты в виде - 35, 55/30, 120/23/45
     */
    private double getRadians(String value) {

        List<Integer> list = Stream.of(value.split("/"))
                .map((String s) -> {
                    return Integer.parseInt(s);
                })
                .collect(Collectors.toList());

        int size = list.size();
        double result = list.get(0);
        int sign = (result < 0) ? -1 : 1;

        result += (size == 3) ? ((list.get(1) + list.get(2) / 60.0) / 60) * sign :
                (size == 2) ? (list.get(1) / 60.0) * sign : 0;

        return Math.toRadians(result);
    }

    /**
     * Получение координат в виде строки хх/хх/хх из радиан.
     *
     * @param radian координата в радианах
     */
    private String getStringCoordinate(double radian) {

        double digit = 180 / Math.PI * radian;
        String result = "" + (int) (digit);

        digit = Math.abs((digit - (int) digit) * 60);
        result += "/" + (int) digit;

        digit = Math.abs((digit - (int) digit) * 60);
        result += "/" + Math.round(digit);

        return result;
    }

    /**
     * Получение строки с географическими координатами из радиан.
     *
     * @param radian  координата в радианах
     * @param geoSymbol x/X или y/Y для установления широты(latitude)/долготы(longitude)
     */
    private String getGeoCoordinate(double radian, String geoSymbol) {

        String str = getStringCoordinate(radian);
        List<Integer> list = Stream.of(str.split("/"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int firstNum = (list.get(0) < 0) ? list.get(0) * -1 : list.get(0);
        String value = ("x".equalsIgnoreCase(geoSymbol)) ? "ш." : "д.";
        String result = firstNum + "°" + list.get(1) + "′" + list.get(2) + "″";

        if(value.equals("ш.")) {
            result += (list.get(0) < 0) ? " ю." + value : " с." + value;
        } else {
            result += (list.get(0) < 0) ? " з." + value : " в." + value;
        }
        return result;
    }

    /**
     * Проверка корректности координат населенного пункта, введенных пользователем.
     *
     * @param coordinate  координата в строковом виде
     * @throws ru.edu.error.CustomException с кодом ARGUMENT_ERROR, если параметр задан неверно
     */
    private double parseCoordinates(String coordinate) {
        try {
            return getRadians(coordinate);

        } catch (NumberFormatException ex) {
            throw new CustomException("Поля заполнены неверно!", Errors.ARGUMENT_ERROR);
        }
    }

    /**
     * Получить численное значение населения из строкового типа.
     *
     * @param populationStr  количество населения в строковом виде
     * @throws ru.edu.error.CustomException с кодом ARGUMENT_ERROR, если параметр задан неверно
     */
    private int parsePopulation(String populationStr) {
        try {
            String population = populationStr.replaceAll(" ", "");
            return Integer.parseInt(population);

        } catch (NumberFormatException ex) {
            throw new CustomException("Поля заполнены неверно!", Errors.ARGUMENT_ERROR);
        }
    }
}
