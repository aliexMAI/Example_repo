package ru.aliex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляет CRUD репозиторий к БД
 */
public class DatabaseRepository {

    private String url = "jdbc:sqlite:";
    private Connection connection;

    public DatabaseRepository() {
    }

    /**
     * Создание подключения с БД
     * @param path путь к бд
     * @return true если подключение установленно иначе false
     */
    public boolean connectOn(String path) {

        try {
            connection = DriverManager.getConnection(url + path);
            return true;

        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Разрыв соединения с БД
     * @return true или false
     */
    public boolean connectOff() {

        if(isConnect()) {
            try {
                connection.close();
                connection = null;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Проверка наличия подключения к БД
     * @return true если подключение есть
     */
    public boolean isConnect() {
        return connection != null;
    }

    /**
     * Получить список названий таблиц в БД
     * @return список названий или null при ошибке доступа к БД
     */
    public List<String> getListTables() {

        if(!isConnect()) return null;

        List<String> list = new ArrayList<>();
        DatabaseMetaData metaData;
        try {
            metaData = connection.getMetaData();
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

        try(ResultSet res = metaData.getTables(null, null, "%", null)) {

            while(res.next()) {
                String name = res.getString("TABLE_NAME");
                list.add(name);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * Получить список колонок в таблице
     * @param tableName название таблицы
     * @return список колонок или null
     */
    public List<String> getListColumns(String tableName) {

        if(!isConnect()) return null;

        List<String> list = new ArrayList<>();
        DatabaseMetaData metaData;
        try {
            metaData = connection.getMetaData();
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

        try(ResultSet res = metaData.getColumns(null, null, tableName, null)) {

            while(res.next()) {
                String name = res.getString("COLUMN_NAME");
                list.add(name);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * Получение объекта с данными ResultSet по запросу в БД
     * @param query запрос в БД
     * @return объект с данными, либо null
     */
    public ResultSet getDataQuery(String query) {

        if(!isConnect()) return null;

        try {
            return connection.createStatement().executeQuery(query);
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
