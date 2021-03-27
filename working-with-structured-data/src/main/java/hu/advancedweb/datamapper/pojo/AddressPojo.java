package hu.advancedweb.datamapper.pojo;

public class AddressPojo {

    private String city;
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "AddressPojo [city=" + city + ", country=" + country + "]";
    }

}
