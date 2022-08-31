package org.example;

import org.example.Format;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        //CSV - JSON парсер
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = Format.parseCSV(columnMapping, fileName);
        String jsonString = Format.listToJson(list);
        Format.fileWrite("JSON.json", jsonString);

        //XML - JSON парсер
        jsonString = Format.listToJson(Format.parseXML("data.xml"));
        Format.fileWrite("data2.json", jsonString);

        //JSON парсер (со звездочкой *)
        String json = Format.readString("data2.json");
        List<Employee> jList = Format.jsonToList(json);
        jList.forEach(System.out::println);
    }
}