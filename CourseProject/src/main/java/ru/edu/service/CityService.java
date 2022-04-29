package ru.edu.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.edu.dao.CityRepository;
import ru.edu.error.CustomException;
import ru.edu.error.Errors;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс предоставляет API для работы с репозиторием
 */
@Component
public class CityService {

    private static final Logger log = LogManager.getLogger(CityService.class);

    private static final String GRADUS = "°C";
    private CityRepository repository;
    private RestTemplate template;
    private String imageUrl;
    private String appKey;
    private int radius;

    /**
     * Конструктор по умолчанию.
     */
    public CityService() {
    }

    @Autowired
    public void setRepository(CityRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setTemplate(RestTemplate template) {
        this.template = template;
    }

    @Autowired
    public void setImageUrl(@Value("${image.url}") String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Autowired
    public void setAppKey(@Value("${weather.appKey}") String key) {
        this.appKey = key;
    }

    @Autowired
    public void setRadius(@Value("${cityNear.radius}") int radius) {
        this.radius = radius;
    }

    public int getRadius() { return radius; }

    /**
     * Получить список объектов всех городов.
     */
    public List<CityInfo> getAll() {

        log.info(">> Method .getAll started");
        try {
            List<CityInfo> result = repository.getAll();
            log.info(">> Method .getAll completed, cityList size={}", result.size());
            return result;
        } catch (Exception ex) {
            log.info(">> Failed to method .getAll, error={}", ex.toString());
            throw new CustomException("Не удалось подключить к базе данных", ex, Errors.DATABASE_ERROR);
        }
    }

    /**
     * Получить город по id, возвращает null, если не найден.
     *
     * @param cityId идентификатор города
     * @throws ru.edu.error.CustomException с кодом CITY_NOT_FOUND, если город не существует
     */
    public CityInfo getCity(String cityId) {

        log.info(">> Method .getCity started");
        CityInfo info = repository.getCity(cityId);

        if(info == null) {
            log.info(">> City id={} not found", cityId);
            throw new CustomException("Город с id=" + cityId + " не найден!!", Errors.CITY_NOT_FOUND);
        }
        log.info(">> Method .getCity completed, city={}", info);
        return info;
    }

    /**
     * Создать новый город.
     *
     * @throws ru.edu.error.CustomException с кодом CITY_ALREADY_EXISTS, если город с текущим идентификатором уже существует
     * @throws ru.edu.error.CustomException с кодом ARGUMENT_ERROR, если аргумент неверен
     */
    public CityInfo create(CityInfo info) {

        log.info(">> Method .create started");
        if(controlCity(info)) {
            throw new CustomException("Поля заполнены неверно!", Errors.ARGUMENT_ERROR);
        }
        CityInfo cityInfo = repository.getCity(info.getId());

        if(cityInfo != null) {
            log.info(">> City name={} is duplicate", info.getName());
            throw new CustomException("Данный город " + info.getName() + " уже есть в базе данных!", Errors.CITY_ALREADY_EXISTS);
        }
        cityInfo = repository.create(info);
        log.info(">> Method .create completed, city={}", cityInfo);
        return cityInfo;
    }

    /**
     * Обновить существующий город без изменения id.
     *
     * @throws ru.edu.error.CustomException с кодом CITY_NOT_FOUND, если город не существует
     * @throws ru.edu.error.CustomException с кодом ARGUMENT_ERROR, если аргумент неверен
     */
    public CityInfo update(CityInfo info) {

        log.info(">> Method .update started");
        if(controlCity(info)) {
            throw new CustomException("Поля заполнены неверно!", Errors.ARGUMENT_ERROR);
        }
        CityInfo cityInfo = repository.getCity(info.getId());

        if(cityInfo == null) {
            log.info(">> City name={} not found", info.getName());
            throw new CustomException("Город " + info.getName() + " не найден в базе данных!", Errors.CITY_NOT_FOUND);
        }
        log.info(">> Method .update completed, city={}", info);
        return repository.update(info);
    }

    /**
     * Удалить город по id.
     *
     * @throws ru.edu.error.CustomException с кодом CITY_NOT_FOUND, если город не существует
     */
    public CityInfo delete(String cityId) {

        log.info(">> Method .delete started");
        CityInfo cityInfo = repository.getCity(cityId);

        if(cityInfo == null) {
            log.info(">> City id={} not found", cityId);
            throw new CustomException("Город с id=" + cityId + " не найден в базе данных!", Errors.CITY_NOT_FOUND);
        }
        log.info(">> Method .delete completed, city={}", cityInfo);
        return repository.delete(cityId);
    }

    /**
     * Получить погоду в городе.
     *
     * @throws ru.edu.error.CustomException с кодом CITY_NOT_FOUND, если город не существует
     */
    public String getWeather(String cityId) {

        log.info(">> Method .getWeather started");
        CityInfo cityInfo = repository.getCity(cityId);
        if(cityInfo == null) {
            log.info(">> City id={} not found", cityId);
            throw new CustomException("Город с id=" + cityId + " не найден в базе данных!", Errors.CITY_NOT_FOUND);
        }

        int temp;
        double wind, press;
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityInfo.getName() +
                    "&appid=" + appKey;
            String infoWeather = template.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(infoWeather);

            double KELVIN = 273.15;
            temp = (int) Math.round(((node.at("/main/temp").asDouble() - KELVIN) * 10) / 10.0);

            wind = node.at("/wind/speed").asDouble();

        } catch (Exception ex) {
            log.info(">> Error connect, URL or city name incorrect");
            return "Не удалось обновить данные о погоде!";
        }

        String strTemp = temp > 0 ? ("+" + temp + GRADUS) : (temp + GRADUS);
        String result = "> температура воздуха:  " + strTemp + "<br>" +
                "> скорость ветра:  " + wind + " м/с ";
        log.info(">> Method .getWeather completed, weather={}", result);

        return result;
    }

    /**
     * Получить расстояние между городами в километрах.
     *
     * @throws ru.edu.error.CustomException с кодом CITY_NOT_FOUND, если город не существует
     */
    public int getDistance(String fromCityId, String toCityId) {

        log.info(">> Method .getDistance started");
        CityInfo fromCity = repository.getCity(fromCityId);
        CityInfo toCity = repository.getCity(toCityId);

        if(fromCity == null || toCity == null) {
            String cityId = fromCity == null ? fromCityId : toCityId;
            log.info(">> City id={} not found", cityId);
            throw new CustomException("Город с id=" + cityId + " не найден в базе данных!", Errors.CITY_NOT_FOUND);
        }
        //средний радиус Земли в км
        final double radEarth = 6371.01;
        //широта и долгота исходного(fromCity) и конечного(toCity) городов в радианах
        double fromLatitude = fromCity.getLatitude();
        double fromLongitude = fromCity.getLongitude();
        double toLatitude = toCity.getLatitude();
        double toLongitude = toCity.getLongitude();
        //получение угловой длины
        double angular = Math.acos(Math.sin(fromLatitude) * Math.sin(toLatitude) +
                         Math.cos(fromLatitude) * Math.cos(toLatitude) *
                         Math.cos(Math.abs(toLongitude - fromLongitude)));

        int distance = (int)(radEarth * angular);
        log.info(">> Method .getDistance completed, distance={}", distance);

        return distance;
    }

    /**
     * Получить список всех городов рядом
     * с текушим городом в указанном радиусе.
     *
     * @param cityId id текущего города
     * @return список городов
     */
    public List<CityInfo> getCityNear(String cityId) {

        log.info(">> Method .getCityNear started");
        CityInfo city = repository.getCity(cityId);
        if(city == null) {
            log.info(">> City id={} not found", cityId);
            throw new CustomException("Город с id=" + cityId + " не найден в базе данных!", Errors.CITY_NOT_FOUND);
        }

        List<CityInfo> cityNear = getAll().stream()
                .filter((CityInfo info) -> {
                    int distance = getDistance(cityId, info.getId());
                    if(!city.getName().equalsIgnoreCase(info.getName()) && distance < radius) {
                        info.setDistance(distance);
                        return true;
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        log.info(">> Method .getCityNear completed, cityNear size={}", cityNear.size());
        return cityNear;
    }

    /**
     * Получить ссылку на изображение города.
     *
     * @param cityName название города
     * @throws ru.edu.error.CustomException с кодом CITY_IMAGE_URL_ERROR, если если произошла ошибка парсинга
     */
    private String getCityImageURL(String cityName) {

        log.info(">> Method .getCityImageURL started");
        String srcImageURL;

        try {
            String strEncoded = URLEncoder.encode(cityName, "UTF-8");
            String fullUrl = imageUrl + strEncoded;
            log.info(">> Method .getCityImageURL fullUrl= {}",fullUrl);

            Document docImage = Jsoup.connect(fullUrl).get();
            Element image = docImage.selectFirst("img.serp-item__thumb");

            if(image != null) {
                srcImageURL = "https:" + image.attr("src");
                log.info(">> Method .getCityImageURL imageUrl= {}",srcImageURL);
            } else {
                srcImageURL = "";
                log.info(">> Method .getCityImageURL imageUrl= {}",srcImageURL);
            }
        } catch (UnsupportedEncodingException uee) {
            log.info(">> City with name: {} unsupported URL Encoding.", cityName);
            throw new CustomException("Данная кодировка символов не поддерживается!!", uee, Errors.CITY_IMAGE_URL_ERROR);

        } catch (Exception ex) {
            log.info(">> City with name: {} failed I/O exception.", cityName);
            throw new CustomException("Произошло исключение типа ввод/вывод!", ex, Errors.CITY_IMAGE_URL_ERROR);
        }
        log.info(">> Method .getCityImageURL completed, srcImageURL={}", srcImageURL);
        return  srcImageURL;
    }

    /**
     * Сохранить изображение города.
     */
    public void saveCityImage(String cityId, String cityName) {

        log.info(">> Method .saveCityImage started");
        String imageName = "/img" + cityId + ".jpg";
        String imageUrl = getCityImageURL(cityName);

        try {
            File file = new File(imageName);
            BufferedInputStream bis = Jsoup.connect(imageUrl)
                    .ignoreContentType(true).execute().bodyStream();

            try(FileOutputStream fos = new FileOutputStream(this.getClass().getResource("/static")
                    .getFile() + imageName)) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                bis.close();
            } catch (FileNotFoundException ex) {
                log.info(">> File {} not found.", file, ex);
                throw new CustomException("Файл не найден!", ex, Errors.CITY_IMAGE_ERROR);
            }
        } catch (Exception ex) {
            log.info(">> City with id: {} failed I/O exception.", cityId, ex);
            throw new CustomException("Произошло исключение типа ввод/вывод!", ex, Errors.CITY_IMAGE_ERROR);
        }
        log.info(">> Method .saveCityImage completed");
    }

    /**
     * Проверка отсутствия данных в полях города
     * @param info объект города
     * @return true/false
     */
    private boolean controlCity(CityInfo info) {

        if(StringUtils.isBlank(info.getId())) {
            log.info(">> Error! City id is blank");
            return true;
        }
        if(StringUtils.isBlank(info.getName())) {
            log.info(">> Error! City name is blank");
            return true;
        }
        if(StringUtils.isBlank(info.getDescription())) {
            log.info(">> Error! City description is blank");
            return true;
        }
        if(StringUtils.isBlank(info.getClimate())) {
            log.info(">> Error! City climate is blank");
            return true;
        }
        if(info.getLatitude() == 0 || info.getLongitude() == 0) {
            log.info(">> Error! City latitude or longitude incorrect");
            return true;
        }
        if(info.getPopulation() <= 0) {
            log.info(">> Error! City population incorrect");
            return true;
        }
        return false;
    }
}
