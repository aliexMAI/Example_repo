package ru.edu.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.edu.error.CustomException;
import ru.edu.error.Errors;
import ru.edu.service.CityInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляет CRUD репозиторий к таблице CityInfo
 */
@Component
public class CityRepository {

    private Connection connection;

    @Autowired
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Конструктор по умолчанию
     */
    public CityRepository() {
    }

    /**
     * Возвратить список всех городов.
     */
    public List<CityInfo> getAll() {

        try(ResultSet resultSet = connection.createStatement()
                .executeQuery("SELECT * FROM CityInfo")) {

            List<CityInfo> cityList = new ArrayList<>();
            while(resultSet.next()) {
                cityList.add(getCityInfo(resultSet));
            }
            return cityList;

        } catch (Exception ex) {
            throw new CustomException("Failed is method .getAll", ex, Errors.DATABASE_ERROR);
        }
    }

    /**
     * Получить город по id. Возвращает null, если город не найден.
     * @param cityId id города
     */
    public CityInfo getCity(String cityId) {

        try(ResultSet resultSet = connection.createStatement()
                .executeQuery("SELECT * FROM CityInfo WHERE id = '" + cityId + "'")) {

            if(!resultSet.next()) {
                return null;
            }
            return getCityInfo(resultSet);

        } catch (Exception ex) {
            throw new CustomException("Failed is method .getCity", ex, Errors.DATABASE_ERROR);
        }
    }

    /**
     * Добавить новый город в БД.
     */
    public CityInfo create(CityInfo info) {

        try(Statement statement = connection.createStatement()) {

            String script = String.format("INSERT INTO CityInfo (" +
                            "'id', 'name', 'description', 'climate', 'latitude', 'longitude', 'population') " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    info.getId(), info.getName(), info.getDescription(), info.getClimate(),
                    info.getLatitude(), info.getLongitude(), info.getPopulation());

            int rows = statement.executeUpdate(script);
            System.out.println(">> Create City complete, rows= " + rows);
            return getCity(info.getId());

        } catch (Exception ex) {
            throw new CustomException("Failed is method .create", ex, Errors.DATABASE_ERROR);
        }
    }

    /**
     * Обновить существующий город без изменения id.
     */
    public CityInfo update(CityInfo info) {

        String script = "UPDATE CityInfo SET name = ?, description = ?, climate = ?, " +
                "latitude = ?, longitude = ?, population = ? WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(script)) {

            statement.setString(1, info.getName());
            statement.setString(2, info.getDescription());
            statement.setString(3, info.getClimate());
            statement.setDouble(4, info.getLatitude());
            statement.setDouble(5, info.getLongitude());
            statement.setInt(6, info.getPopulation());
            statement.setString(7, info.getId());

            int rows = statement.executeUpdate();
            System.out.println(">> Update CityInfo complete, rows= " + rows);
            return getCity(info.getId());

        } catch (Exception ex) {
            throw new CustomException("Failed is method .update", ex, Errors.DATABASE_ERROR);
        }
    }

    /**
     * Удалить город по id.
     */
    public CityInfo delete(String cityId) {

        CityInfo city = getCity(cityId);

        if(city != null) {

            try (PreparedStatement statement =
                         connection.prepareStatement("DELETE FROM CityInfo WHERE id = ?")) {

                statement.setString(1, cityId);
                int rows = statement.executeUpdate();
                System.out.println(">> Delete CityInfo complete, rows= " + rows);
                return city;

            } catch (Exception ex) {
                throw new CustomException("Failed is method .delete", ex, Errors.DATABASE_ERROR);
            }
        }
        return null;
    }

    /**
     * Вернуть город из БД
     * @param resultSet параметр типа ResultSet
     * @return возвращает объект CityInfo
     */
    private CityInfo getCityInfo(ResultSet resultSet) throws SQLException {

        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String climate = resultSet.getString("climate");
        double latitude = resultSet.getDouble("latitude");
        double longitude = resultSet.getDouble("longitude");
        int population = resultSet.getInt("population");

        CityInfo city = new CityInfo();
        city.setId(id);
        city.setName(name);
        city.setDescription(description);
        city.setClimate(climate);
        city.setLatitude(latitude);
        city.setLongitude(longitude);
        city.setPopulation(population);

        return city;
    }

    /**
     * Создание таблицы CityInfo
     */
    public void createTable() {

        try(Statement statement = connection.createStatement()) {
            String script = "CREATE TABLE CityInfo (" +
                    " id varchar(100) primary key," +
                    " name varchar(50) ," +
                    " description varchar(1500)," +
                    " climate varchar(5000)," +
                    " latitude double," +
                    " longitude double," +
                    " population int );";
            int rows = statement.executeUpdate(script);
            System.out.println(">> Create table complete, rows= " + rows);
        } catch (Exception ex) {
            throw new CustomException("Failed is method .createTable", Errors.DATABASE_ERROR);
        }
    }

    /**
     * Удаление таблицы CityInfo
     */
    public void dropTable() {

        try(Statement statement = connection.createStatement()) {
            int rows = statement.executeUpdate("DROP TABLE CityInfo");
            System.out.println(">> Delete table complete, rows= " + rows);
        } catch (Exception ex) {
            throw new CustomException("Failed is method .dropTable", Errors.DATABASE_ERROR);
        }
    }
}
