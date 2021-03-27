package hu.advancedweb.datamapper;

import java.io.File;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.advancedweb.datamapper.pojo.PersonPojo;
import hu.advancedweb.datamapper.record.AddressRecord;
import hu.advancedweb.datamapper.record.PersonRecord;
import org.junit.jupiter.api.Test;

/**
 * Deep merging Maps works by default, but in order to deep merge POJOs the object mapper
 * has to be configured with one of the following:
 * - enable deep merging of a field with the @JsonMerge annotation
 * - enable deep merging of a specific type with
 *   objectMapper.configOverride(MyNestedClass.class).setMergeable(true);
 * - enable deep merging by default with
 *   objectMapper.setDefaultMergeable(true);
 *
 * More info:
 * https://medium.com/@cowtowncoder/jackson-2-9-features-b2a19029e9ff
 */
public class DeepMergeTest {

	@Test
	public void deep_merging_maps_works_by_default() throws Exception {
		final String jsonPath = getClass().getClassLoader().getResource("record.json").getFile();
		Map<String, Object> person = new ObjectMapper().readValue(new File(jsonPath), Map.class);

		// Original data from record.json
		assert "Robert".equals(person.get("name"));
		assert Integer.valueOf(25).equals(person.get("age"));
		assert "United Kingdom".equals(((Map) person.get("address")).get("country"));
		assert "London".equals(((Map) person.get("address")).get("city"));

		// Let's update the address, by changing the city to Birmingham,
		// without affecting address.country
		Map<String, Object> updates = Map.of("address", Map.of("city", "Birmingham"));
		new ObjectMapper().updateValue(person, updates);

		// Successfully updated the nested map:
		// - address.city is now Birmingham, not London
		// - all other fields, including address.country remain the same
		assert "Robert".equals(person.get("name"));
		assert Integer.valueOf(25).equals(person.get("age"));
		assert "Birmingham".equals(((Map) person.get("address")).get("city"));
		assert "United Kingdom".equals(((Map) person.get("address")).get("country"));
	}

	@Test
	public void deep_merging_pojos_does_not_work_by_default() throws Exception {
		final String jsonPath = getClass().getClassLoader().getResource("record.json").getFile();
		PersonPojo personPojo = new ObjectMapper().readValue(new File(jsonPath), PersonPojo.class);

		// Original data from record.json
		assert "Robert".equals(personPojo.getName());
		assert Integer.valueOf(25).equals(personPojo.getAge());
		assert "United Kingdom".equals(personPojo.getAddress().getCountry());
		assert "London".equals(personPojo.getAddress().getCity());

		// Let's update the address, by changing the city to Birmingham,
		// without affecting address.country
		Map<String, Object> updates = Map.of("address", Map.of("city", "Birmingham"));
		new ObjectMapper().updateValue(personPojo, updates);

		// Failed to updated the nested Address object:
		// - address.country is now NULL
		assert "Robert".equals(personPojo.getName());
		assert Integer.valueOf(25).equals(personPojo.getAge());
		assert "Birmingham".equals(personPojo.getAddress().getCity());
		assert "United Kingdom".equals(personPojo.getAddress().getCountry()); // This assertion fails: address.country is null!
	}

	@Test
	public void deep_merging_pojos_with_json_merge() throws Exception {
		final String jsonPath = getClass().getClassLoader().getResource("record.json").getFile();

		ObjectMapper objectMapper = new ObjectMapper();
		// Enable deep merge:
		// https://medium.com/@cowtowncoder/jackson-2-9-features-b2a19029e9ff
		// https://github.com/FasterXML/jackson-databind/issues/2477
		objectMapper.setDefaultMergeable(true);

		PersonPojo personPojo = objectMapper.readValue(new File(jsonPath), PersonPojo.class);

		// Original data from record.json
		assert "Robert".equals(personPojo.getName());
		assert Integer.valueOf(25).equals(personPojo.getAge());
		assert "United Kingdom".equals(personPojo.getAddress().getCountry());
		assert "London".equals(personPojo.getAddress().getCity());

		// Let's update the address, by changing the city to Birmingham,
		// without affecting address.country
		Map<String, Object> updates = Map.of("address", Map.of("city", "Birmingham"));
		objectMapper.updateValue(personPojo, updates);

		// Successfully updated the nested Address object:
		// - address.city is now Birmingham, not London
		// - all other fields, including address.country remain the same
		assert "Robert".equals(personPojo.getName());
		assert Integer.valueOf(25).equals(personPojo.getAge());
		assert "Birmingham".equals(personPojo.getAddress().getCity());
		assert "United Kingdom".equals(personPojo.getAddress().getCountry());
	}

	@Test
	public void merging_records_with_json_merge() throws Exception {
		/*
		objectMapper.updateValue does not work with records as their fields are final.

		In this scenario I do the following to create a new, modified record:
		- convert the original record into a map
		- modify the map
		- convert the map to the new record

		This approach however does not support deep merging.
		 */

		PersonRecord originalRecord = new PersonRecord("John", 15,
				new AddressRecord("Birmingham", "United Kingdom"));

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> mutable = objectMapper.convertValue(originalRecord, Map.class);

		mutable.put("name", "Jane");

		PersonRecord newRecord = objectMapper.convertValue(mutable, PersonRecord.class);

		System.out.println(originalRecord);
		/*
		Prints the original record:
		PersonRecord[name=John, age=15, address=AddressRecord[city=Birmingham, country=United Kingdom]]
		 */
		System.out.println("newRecord = " + newRecord);
		/*
		Prints the updated record:
		PersonRecord[name=Jane, age=15, address=AddressRecord[city=Birmingham, country=United Kingdom]]
		 */
	}

	@Test
	public void deep_merging_records_with_json_merge_is_not_working() throws Exception {
		PersonRecord originalRecord = new PersonRecord("John", 15,
				new AddressRecord("Birmingham", "United Kingdom"));

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> updates = Map.of("name", "Jane");
		//Map<String, Object> updates = Map.of("address", Map.of("city", "Birmingham"));
		objectMapper.updateValue(originalRecord, updates);
		// ↑ JsonMappingException: Can not set final java.lang.String field a.b.c.Person.name to java.lang.String
		// Fields of a record are final
		/*
		https://github.com/FasterXML/jackson-databind/issues/3079
		 */
	}

}
