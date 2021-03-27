package hu.advancedweb.datamapper;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonStructure;

import hu.advancedweb.datamapper.pojo.PersonPojo;
import hu.advancedweb.datamapper.record.PersonRecord;
import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

/**
 * Compare and show differences of Maps, POJOs, Records and JSON Structures.
 *
 * Diffing with Maps.difference from Guava and ReflectionDiffBuilder from Apache Commons Lang
 * does shallow diffing, and reports the entire nested object as a difference in case at least one
 * of its properties are different.
 *
 * These diff results can be improved with some tricks:
 * - if the structured data only has a few nested properties, diff them separately and merge the diff results
 * - when diffing Maps, flatten the hierarchical structure
 *
 * Alternatively, convert the data to JSON and use JSON-P, which does provide deep comparison.
 *
 * More information : https://stackoverflow.com/questions/50967015/how-to-compare-json-documents-and-return-the-differences-with-jackson-or-gson
 */
public class DiffTest {

	@Test
	public void diff_map() throws Exception {
		final String jsonPath = getClass().getClassLoader().getResource("record.json").getFile();
		Map<String, Object> original = new ObjectMapper().readValue(new File(jsonPath), Map.class);

		final String etalonJsonPath = getClass().getClassLoader().getResource("record_etalon.json").getFile();
		Map<String, Object> etalon = new ObjectMapper().readValue(new File(etalonJsonPath), Map.class);

		MapDifference<String, Object> diff = Maps.difference(original, etalon);
		Map<String, ValueDifference<Object>> entriesDiffering = diff.entriesDiffering();

		System.out.println("original = " + original);
		System.out.println("etalon = " + etalon);
		System.out.println("diff = " + entriesDiffering);

		// Maps.difference provides shallow diff, and reports the contents of the entire address field
		// when at least one of it's properties are different.

		// diff = {
		//   name=(Robert, John),
		//   age=(25, 23),
		//   address=({city=London, country=United Kingdom}, {city=Birmingham, country=United Kingdom})}
	}

	@Test
	public void diff_pojo() throws Exception {
		final String jsonPath = getClass().getClassLoader().getResource("record.json").getFile();
		PersonPojo original = new ObjectMapper().readValue(new File(jsonPath), PersonPojo.class);

		final String etalonJsonPath = getClass().getClassLoader().getResource("record_etalon.json").getFile();
		PersonPojo etalon = new ObjectMapper().readValue(new File(etalonJsonPath), PersonPojo.class);

		List<Diff<?>> diff = new ReflectionDiffBuilder<>(original, etalon, new RecursiveToStringStyle()).build().getDiffs();

		System.out.println(diff);

		/*
		ReflectionDiffBuilder provides shallow diff, and reports the contents of the entire address field
		when at least one of it's fields are different.

		In this case it means that even both objects have the same address.country value, they are reported in the diff.

		diff = [
		   [name: Robert, John],
		   [age: 25, 23],
		   [address: Address [city=London, country=United Kingdom], Address [city=Birmingham, country=United Kingdom]]]

		 */
	}

	@Test
	public void diff_record() throws Exception {
		final String jsonPath = getClass().getClassLoader().getResource("record.json").getFile();
		PersonRecord original = new ObjectMapper().readValue(new File(jsonPath), PersonRecord.class);

		final String etalonJsonPath = getClass().getClassLoader().getResource("record_etalon.json").getFile();
		PersonRecord etalon = new ObjectMapper().readValue(new File(etalonJsonPath), PersonRecord.class);

		List<Diff<?>> diff = new ReflectionDiffBuilder<>(original, etalon, new RecursiveToStringStyle()).build().getDiffs();

		System.out.println(diff);

		/*
		ReflectionDiffBuilder works with records just as much as it works with POJOs, see diff_pojo.
		 */
	}

	@Test
	public void diff_json_structures() throws Exception {
		// Source: https://stackoverflow.com/questions/50967015/how-to-compare-json-documents-and-return-the-differences-with-jackson-or-gson
		final String jsonPath = getClass().getClassLoader().getResource("record.json").getFile();
		JsonStructure jsonOriginal = Json.createReader(
				new DataInputStream(new FileInputStream(new File(jsonPath)))).read();

		final String etalonJsonPath = getClass().getClassLoader().getResource("record_etalon.json").getFile();
		JsonStructure jsonEtalon = Json.createReader(
				new DataInputStream(new FileInputStream(new File(etalonJsonPath)))).read();

		JsonPatch diff = Json.createDiff(jsonOriginal, jsonEtalon);

		System.out.println(diff);

		// JSON-P can compare hierarchical structures. The diff object can also be used to
		// patch existing JSON documents.

		// [
		//   {"op":"replace","path":"/name","value":"John"},
		//   {"op":"replace","path":"/age","value":23},
		//   {"op":"replace","path":"/address/city","value":"Birmingham"}]
	}

}
