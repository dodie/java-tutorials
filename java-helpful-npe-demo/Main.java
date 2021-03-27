import java.io.StringReader;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class Main {

    // Sample XML of departments and employees.
    static final String xml = """
        <?xml version="1.0" encoding="UTF-8"?>
        <departments>
                <department id="1">
                        <name>HR</name>
                        <employees>
                            <employee id="1">
                                 <firstname>John</firstname >
                                 <lastname>Doe</lastname>
                                 <age>30</age>
                            </employee>
                            <employee id="2">
                                 <firstname>Jane</firstname >
                                 <lastname>Doe</lastname>
                                 <age>29</age>
                            </employee>
                    </employees>
                </department>
                <department id="2">
                        <employees>
                            <employee id="3">
                                 <firstname>Programmer</firstname >
                                 <lastname>Paul</lastname>
                                 <age>30</age>
                            </employee>
                            <employee id="4">
                                 <firstname>Developer</firstname >
                                 <lastname>Daniel</lastname>
                                 <age>25</age>
                            </employee>
                    </employees>
                </department>
        </departments>
    """.trim();


    public static void main(String[] args) throws Exception {

         // Parse XML
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();

         InputSource is = new InputSource(new StringReader(xml));
         Document document = builder.parse(is);

         // Find the `employee` nodes and call `processEmployee` with each
         NodeList departments = document.getDocumentElement().getChildNodes();
         for (int i = 0; i < departments.getLength(); i++) {
              Node department = departments.item(i);

              if (department.getNodeType() == Node.ELEMENT_NODE) {
                   Element departmentElem = (Element) department;
                   NodeList employees = departmentElem.getElementsByTagName("employees").item(0).getChildNodes();
                   for (int j = 0; j < employees.getLength(); j++) {
                        Node employee = employees.item(j);

                        if (employee.getNodeType() == Node.ELEMENT_NODE) {
                             Element employeeElem = (Element) employee;
                             processEmployee(employeeElem);
                        }
                   }
              }
         }
    }

    // Print the name and the department for the given employee.
    public static void processEmployee(Element employeeElem) {
         String firstName = employeeElem.getElementsByTagName("firstname").item(0).getChildNodes().item(0).getNodeValue();
         String lastName = employeeElem.getElementsByTagName("lastname").item(0).getChildNodes().item(0).getNodeValue();

         Element department = (Element)employeeElem.getParentNode().getParentNode();

         // This line will blow up with a NullPointerException.
         // With Helpful NullPointerExceptions it's much easier to find out the exact source of the problem.
         String departmentName = department.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();

         System.out.println(firstName + " " + lastName + " from " + departmentName);
    }

}
