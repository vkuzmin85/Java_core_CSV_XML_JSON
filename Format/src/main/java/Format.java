package org.example;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Format {
    public static List<Employee> parseCSV(String[] s, String fileName) {
        List<Employee> staff = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(s[0], s[1], s[2], s[3], s[4]);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
            return staff;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        //Gson gson = builder.create();
        Gson gson = builder.setPrettyPrinting().create();
        return gson.toJson(list, listType);
    }

    public static void fileWrite(String fileName, String text) {
        try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static List<Employee> read(Node root) {
        List<String> items = new ArrayList<>();
        List<Employee> list = new ArrayList<>();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("employee")) {
                NodeList nodeList_ = node.getChildNodes();
                for (int j = 0; j < nodeList_.getLength(); j++) {
                    Node node_ = nodeList_.item(j);
                    if (Node.ELEMENT_NODE == node_.getNodeType()) {
                        items.add(node_.getTextContent());
                    }
                }
                list.add(new Employee(Long.parseLong(
                        items.get(0)),
                        items.get(1),
                        items.get(2),
                        items.get(3),
                        Integer.parseInt(items.get(4))));
                items.clear();
            }
        }
        return list;
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));
        Node root = doc.getDocumentElement();
        return read(root);
    }

    public static String readString(String fileName) {
        String s;
        String out = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            //чтение построчно
            while ((s = br.readLine()) != null) {
                out += s;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return out;
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> list;// = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type collectionType = new TypeToken<List<Employee>>() {}.getType();
        list = gson.fromJson(json, collectionType);
        return list;
    }
}
