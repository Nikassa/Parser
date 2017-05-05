package com.home.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectAvito {

    //будем работать с такой выборкой - объявления по продаже квартир частными лицами в городе Краснодаре(Центральный район)
    static final String queryURL = "https://www.avito.ru/krasnodar/kvartiry/prodam?user=1&district=362";

    static Elements avitoElements = ConnectAvito.getMetaElements(queryURL, "class", "description");
    static Elements avitoQuerySize = ConnectAvito.getMetaElements(queryURL, "class", "pagination-page");

    public static Elements getMetaElements(String url, String attribute, String value) {
        //подключаем библиотеку, определяем атрибут и значение для получения по ним данных

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = doc.getElementsByAttributeValue(attribute, value);
        return elements;
    }

    public static ArrayList<String> getAvitoPricesList() { //получаем список цен

        ArrayList<String> pricesList = new ArrayList<>();
        avitoElements.forEach(divElement -> {
            Element childDivElement = divElement.child(1);
            String description = childDivElement.text();
            pricesList.add(description);
        });

        return pricesList;
    }

    public static ArrayList<String> getAvitoApartmentsList() { //получаем список описаний

        ArrayList<String> apartmentsList = new ArrayList<>();

        avitoElements.forEach(divElement -> {
            Element h3Element = divElement.child(0);
            String title = h3Element.text();
            apartmentsList.add(title);
        });
        return apartmentsList;
    }

    public static ArrayList<String> getAvitoApartmentsUrlList() { //получаем список url

        ArrayList<String> apartmentsUrlList = new ArrayList<>();

        avitoElements.forEach(divElement -> {
            Element h3Element = divElement.child(0);
            Element aElement = h3Element.child(0);
            String url = aElement.attr("href");
            apartmentsUrlList.add(url);
        });
        return apartmentsUrlList;
    }

    public static ArrayList<Integer> getAvitoQuerySize() { //получаем количество всех страниц по заданному запросу

        ArrayList<Integer> querySize = new ArrayList<>();

        avitoQuerySize.forEach(aElement -> {
            if (aElement.text().contains("Последняя")) { //перебираем значения атрибутов(ссылки) "1, 2, 3 ... n, Последняя" на главной странице
                String href = aElement.attr("href"); //получаем строковое значение атрибута, содержащее в значении "Последняя"

                //находим расположение в строке
                int start = href.indexOf("p=");
                int end = href.indexOf("&");
                String stringQuerySize = href.substring(start + 2, end); //получаем цифру в строковом виде
                int i = Integer.parseInt(stringQuerySize); //конвертируем строку в цифру

                querySize.add(i);
            }
        });
        return querySize;
    }
}
