package ru.edu.service;

import java.util.Objects;

/**
 * Класс объекта города
 */
public class CityInfo {

    private String id, name, description, climate;   // идентификатор, название, описание и климат города
    private double latitude, longitude;              // широта и долгота в радианах
    private int population;                          // численность населения города
    private int distance;                            // расстояние между городами

    /**
     * Конструктор по умолчанию
     */
    public CityInfo() {
    }

    //сеттеры и геттеры приватных полей
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Создание id города на основе названия и хэшкода города
     */
    public void createId() {
        this.id = "" + hashCode();
    }

    // строковое представление широты и долготы города
    private final String latitd = String.valueOf(latitude);
    private final String longtd = String.valueOf(longitude);

    @Override
    public boolean equals(Object ob) {
        if(this == ob) return true;
        if(ob == null) return false;
        if(this.getClass() != ob.getClass()) return false;
        CityInfo info = (CityInfo) ob;
        return id.equals(info.id) && name.equalsIgnoreCase(info.name)
                && String.valueOf(latitude).equals(info.latitd)
                && String.valueOf(longitude).equals(info.longtd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), latitude, longitude);
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                " id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", climate='" + climate + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", population=" + population + '}';
    }
}
