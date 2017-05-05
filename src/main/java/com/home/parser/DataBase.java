package com.home.parser;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    static ArrayList<ArrayList<String>> dbData = new ArrayList<>(); //задаем главную коллекцию, которая будет содержать все передаваемые данные в БД и обратно

    //конфигурация подключения к базе данных
    private static final String URL = "jdbc:postgresql://192.168.1.120:5432/db_apartment";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static ArrayList<ArrayList<String>> connectDataBase(ArrayList<ArrayList<String>> dbData, String query) throws SQLException {
        //подключаемся к БД и отправляем запросы, результат сохраняем в главную коллекцию(ArrayList<ArrayList<String>> dbData)
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ArrayList<String> inner = new ArrayList<>(); //создаем внутреннюю коллекцию для колонок

                //заполняем колонки
                int id = resultSet.getInt("id");
                inner.add(String.valueOf(id));
                String price = resultSet.getString("price");
                inner.add(price);
                String description = resultSet.getString("description");
                inner.add(description);
                String url = resultSet.getString("url");
                inner.add(url);

                dbData.add(inner); //заполняем главную коллекцию
            }

        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } finally {
            preparedStatement.close();
            connection.close();
        }

        return dbData;
    }

    public static void exportDataToDB() throws SQLException {
        //подключаемся к БД и отправляем одной строкой запрос по сохранению данных в таблицу из главной коллекции(ArrayList<ArrayList<String>> dbData)

        final String TRUNCATE = "TRUNCATE apartment_list RESTART IDENTITY;"; //sql-запрос: удаление всех данных таблицы, обнуляем счетчик ключа

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(TRUNCATE); //очищаем таблицу от данных перед передачей новой выборки
            preparedStatement.executeUpdate();

            String query = new String(); //запрос, передаваемый в БД в виде строки

            int lastPage = ConnectAvito.getAvitoQuerySize().get(0); //получаем количество всех страниц по нашей выборке

            for (int i = 0; i < lastPage; i++) { //выполняем для каждой страницы - формируем ссылку, получаем данные и добавляем в query

                //формируем ссылку для определенной страницы
                if (i == 0) { //для первой страницы ничего не меняется
                    ConnectAvito.avitoElements = ConnectAvito.getMetaElements(ConnectAvito.queryURL, "class", "description");
                }
                if (i > 0) { //изменяем ссылки последущих страниц, путем добавления номера страницы в адресную строку
                    int page = i + 1;
                    ConnectAvito.avitoElements = ConnectAvito.getMetaElements(ConnectAvito.queryURL.substring(0, 47).toString() + "p=" + page +
                            "&user=1&" + ConnectAvito.queryURL.substring(47, 59).toString(), "class", "description");
                    //где 0 - начало строки, 47 - место вставления переменной в строку, 59 - конец строки
                }

                //используем цикл для встраивания всех передаваемых данных в одну строку запроса
                int rowsSize = ConnectAvito.getAvitoPricesList().size(); //количество добавляемых строк на одной странице
                String partQuery = new String(); //часть запроса, которая содержит данные с одной страницы
                for (int j = 0; j < rowsSize; j++) {

                    partQuery = partQuery + "('" + ConnectAvito.getAvitoPricesList().get(j) + "', '" + ConnectAvito.getAvitoApartmentsList().get(j)
                            + "', 'https://www.avito.ru" + ConnectAvito.getAvitoApartmentsUrlList().get(j) + "')";
                    if (j != rowsSize - 1) { //проверка если строка не последняя, то добавляем запятую, если последняя - все добавляем в запрос
                        partQuery = partQuery + ",";
                    } else {
                        query = query + "INSERT INTO apartment_list (price, description, url) VALUES " + partQuery + ";";
                    }
                }
            }

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();

        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        } finally {
            preparedStatement.close();
            connection.close();
        }
    }

    public static ArrayList<ArrayList<String>> getDataFromDB() throws SQLException {
        //создаем sql-запрос и передаем главную коллекцию(ArrayList<ArrayList<String>> dbData) для наполнения данными

        String GET_ALL = "SELECT * FROM apartment_list"; //sql-запрос: получение всех данных таблицы

        dbData = new ArrayList<>(); //обнуляем данные для замены

        //подключаемся к БД и отправляем запрос, результат сохраняем в главную коллекцию(ArrayList<ArrayList<String>> dbData)
        dbData = connectDataBase(dbData, GET_ALL);

        return dbData;
    }

    public static ArrayList<ArrayList<String>> getSearchedDataFromDB(String textField) throws SQLException {
        //создаем sql-запрос и передаем главную коллекцию(ArrayList<ArrayList<String>> dbData) вместе с текстовым запросом для наполнения данными

        //sql-запрос: получение данных таблицы с ограничением
        String SEARCH = "SELECT * FROM apartment_list WHERE price LIKE '%" + textField + "%' or description LIKE '%" + textField + "%';";

        dbData = new ArrayList<>(); //обнуляем данные для замены

        //подключаемся к БД и отправляем запрос, результат сохраняем в главную коллекцию(ArrayList<ArrayList<String>> dbData)
        dbData = connectDataBase(dbData, SEARCH);

        return dbData;
    }
}
