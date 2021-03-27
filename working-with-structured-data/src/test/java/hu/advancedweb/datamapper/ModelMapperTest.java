package hu.advancedweb.datamapper;

import hu.advancedweb.datamapper.pojo.AddressPojo;
import hu.advancedweb.datamapper.record.AddressRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

public class ModelMapperTest {

    @Test
    void mapping_pojos_to_pojos() {
        ModelMapper modelMapper = new ModelMapper();

        AddressPojo source = new AddressPojo();
        source.setCity("London");
        source.setCountry("United Kingdom");

        AddressPojo destinationPojo = modelMapper.map(source, AddressPojo.class);

        System.out.println("source = " + source);
        System.out.println("destinationPojo = " + destinationPojo);
    }

    @Test
    void mapping_records_to_pojos() {
        ModelMapper modelMapper = new ModelMapper();

        AddressRecord source = new AddressRecord("London", "United Kingdom");

        AddressPojo destinationPojo = modelMapper.map(source, AddressPojo.class);

        System.out.println("source = " + source);
        System.out.println("destinationPojo = " + destinationPojo);
    }

    @Disabled("https://github.com/modelmapper/modelmapper/issues/546")
    @Test
    void mapping_pojos_to_records() {
        ModelMapper modelMapper = new ModelMapper();

        AddressPojo source = new AddressPojo();
        source.setCity("London");
        source.setCountry("United Kingdom");

        AddressRecord destinationRecord = modelMapper.map(source, AddressRecord.class);

        System.out.println("source = " + source);
        System.out.println("destinationPojo = " + destinationRecord);
    }

    @Test
    void mapping_records_to_records() {
        ModelMapper modelMapper = new ModelMapper();

        AddressRecord source = new AddressRecord("London", "United Kingdom");

        AddressRecord destinationRecord = modelMapper.map(source, AddressRecord.class);

        System.out.println("source = " + source);
        System.out.println("destinationPojo = " + destinationRecord);
    }
}
