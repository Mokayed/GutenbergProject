package entity;

public class City {

    private int id;
    private String cityName;
    private double latitude;
    private double longitude;
    private int population;
    private String countryCode;
    private String continent;

    public City(int id, String cityName, double latitude, double longitude, int population, String countryCode, String continent) {
        this.id = id;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population = population;
        this.countryCode = countryCode;
        this.continent = continent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "City{" + "id=" + id + ", cityName=" + cityName + ", latitude=" + latitude + ", longitude=" + longitude + ", population=" + population + ", countryCode=" + countryCode + ", continent=" + continent + '}';
    }

}
