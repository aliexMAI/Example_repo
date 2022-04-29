package ru.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.edu.service.CityInfo;
import ru.edu.service.CityService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/city", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CityRestController {

    private CityService cityService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Получить все города.
     *
     * @author -  Меньщиков Константин
     */
    @GetMapping("/all")
    public List<CityInfo> getAll() {
        return cityService.getAll();
    }

    /**
     * Получить город по идентификатору. Возвращает null, если город не найден.
     *
     * @author - Мелёхин Алексей
     */
    @GetMapping
    public CityInfo getCity(@RequestParam("cityId") String cityId) {

        try {
            return cityService.getCity(cityId);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Создать новый город.
     *
     * @author - Истомин Михаил
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CityInfo create(@RequestBody CityInfo info) {

        try {
            info.createId();
            return cityService.create(info);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Обновить существующий город. Не изменять идентификатор.
     *
     * @author - Насибуллина Гульназ
     */
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CityInfo update(@RequestBody CityInfo info) {

        try {
            return cityService.update(info);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Удалить город по идентификатору.
     *
     * @author - Истомин Михаил
     */
    @DeleteMapping("/delete/{cityId}")
    public CityInfo delete(@PathVariable("cityId") String cityId) {

        try {
            return cityService.delete(cityId);
        } catch (Exception ex) {
            return null;
        }
    }
}
