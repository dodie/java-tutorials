package hu.advancedweb.datamapper.pojo;

public class PersonPojo {

    private String name;
    private Integer age;
    private AddressPojo address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public AddressPojo getAddress() {
        return address;
    }

    public void setAddress(AddressPojo address) {
        this.address = address;
    }

}
