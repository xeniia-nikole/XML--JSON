import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);

        try (FileWriter file = new FileWriter("data2.json")) {
            file.write(json);
            file.flush();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newNSInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));

            Node node = document.getDocumentElement();
            System.out.println("Root node: " + node.getNodeName());
            document.getDocumentElement().normalize();
            System.out.println("Корневой элемент: " + document.getDocumentElement().getNodeName());
// 1. XML полностью загружен в память в виде объекта Document

            NodeList nodeList = document.getElementsByTagName("employee");

// 2. Список объектов employee
            for (int i = 0; i < nodeList.getLength(); i++) {
                employees.add(getEmployee(nodeList.item(i)));
            }

// 3. Вывод информации по каждому объекту в консоль
            for (Employee employee : employees) {
                System.out.println(employee.toString());
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return employees;
    }


// 4. Объект Employee из узла документа
    private static Employee getEmployee(Node node) {
        Employee employee = new Employee();
        NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node1 = nodeList.item(i);
                if (node1.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    employee.setId(Integer.parseInt(getTagValue("id", element)));
                    employee.setFirstName(getTagValue("firstName", element));
                    employee.setLastName(getTagValue("lastName", element));
                    employee.setCountry(getTagValue("country", element));
                    employee.setAge(Integer.parseInt(getTagValue("age", element)));
            }
        }
        return employee;
    }

// 5. Значение элемента по указанному тегу
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

// 6. В JSON
    private static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(list);
        JsonParser jp = new JsonParser();
        String uglyJSONString = json;
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return json;
    }

}
