package com.home.parser;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TableModel extends AbstractTableModel {

    private int tableColumnCount = 4; //задаем количество колонок в таблице
    private static ArrayList<ArrayList<String>> applicationData; //задаем таблицу

    public TableModel() { //при запуске приложения создается пустая таблица

        applicationData = new ArrayList<>();

        for (int i = 0; i < applicationData.size(); i++) {
            applicationData.add(new ArrayList<>());
        }
    }

    @Override
    public int getRowCount() { //получаем размер(длину) таблицы по количеству строк в ней
        return applicationData.size();
    }

    @Override
    public int getColumnCount() { //получаем размер(ширину) таблицы из заданной переменной
        return tableColumnCount;
    }

    @Override
    public String getColumnName(int tableColumnIndex) { //задаем наименования колонкам

        ArrayList<String> columnNames = new ArrayList<>();

        columnNames.add("Номер");
        columnNames.add("Цена");
        columnNames.add("Описание");
        columnNames.add("Адрес");

        return columnNames.get(tableColumnIndex);
    }

    @Override
    public Object getValueAt(int tableRowIndex, int tableColumnIndex) { //получаем значение по заданным координатам
        ArrayList<String> rows = applicationData.get(tableRowIndex);
        return rows.get(tableColumnIndex);
    }

    public static void addToApplicationData(ArrayList<ArrayList<String>> dbData) { //добавляем данные из коллекции на интерфейс приложения

        for (int i = 0; i < dbData.size(); i++) {

            ArrayList<String> inner = new ArrayList<>(); //создаем внутреннюю коллекцию для колонок

            for (int j = 0; j < 4; j++) { //перебор всех колонок
                inner.add(dbData.get(i).get(j));
            }

            applicationData.add(inner);
        }
    }

    public void removeFromApplicationData() { //удаляем данные из интерфейса приложения
        applicationData.removeAll(applicationData);
    }
}
