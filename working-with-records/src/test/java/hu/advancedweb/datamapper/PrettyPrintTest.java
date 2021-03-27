package hu.advancedweb.datamapper;

import java.io.File;

import hu.advancedweb.datamapper.pojo.PersonPojo;
import hu.advancedweb.datamapper.record.PersonRecord;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

/**
 * By default the toString implementation is not too informative for POJOs. To get something meaningful,
 * you either have to provide your own implementation or have to use some tools like ReflectionToStringBuilder.
 *
 * In contrast records have a toString implementation to print all their fields by default.
 */
public class PrettyPrintTest {

    @Test
    public void print_pojos() throws Exception {
        final String jsonPath = getClass().getClassLoader().getResource("record.json").getFile();
        PersonPojo original = new ObjectMapper().readValue(new File(jsonPath), PersonPojo.class);

        System.out.println(original);
        /*
        Prints:
        hu.advancedweb.datamapper.PersonPojo@525b461a
         */

        System.out.println(ReflectionToStringBuilder.toString(original, new RecursiveToStringStyle()));
        /*
        Prints:
        hu.advancedweb.datamapper.PersonPojo@525b461a[name=Robert,age=25,address=hu.advancedweb.datamapper.AddressPojo@6591f517[city=London,country=United Kingdom]]
         */
    }

    @Test
    public void print_records() throws Exception {
        final String jsonPath = getClass().getClassLoader().getResource("record.json").getFile();
        PersonRecord original = new ObjectMapper().readValue(new File(jsonPath), PersonRecord.class);

        System.out.println(original);
        /*
        By default, a record has a quite nice string representation:
        PersonRecord[name=Robert, age=25, address=AddressRecord[city=London, country=United Kingdom]]
        */

        System.out.println(ReflectionToStringBuilder.toString(original, new RecursiveToStringStyle()));
        /*
        But ReflectionToStringBuilder keeps working with it:
        hu.advancedweb.datamapper.PersonRecord@525b461a[name=Robert,age=25,address=hu.advancedweb.datamapper.AddressRecord@6591f517[city=London,country=United Kingdom]]
         */
    }

}
