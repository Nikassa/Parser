package com.home.parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class ButtonPanel extends JPanel implements ActionListener { //создаем фрейм, кнопки и задаем действия для кнопок

    public ButtonPanel() throws IOException {

        //кнопка Старт
        JButton startButton = new JButton("Старт"); //задаем название кнопки
        startButton.addActionListener(new ActionListener() { //задаем действия кнопки
            @Override
            public void actionPerformed(ActionEvent e) { //реакция на клик ЛКМ

                Frame.tableModel.removeFromApplicationData(); //удаляем данные для замены

                try {
                    DataBase.exportDataToDB(); //экспорт данных из главной коллекции(ArrayList<ArrayList<String>> dbData) в БД
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    DataBase.getDataFromDB(); //получаем данные из БД, записываем в главную коллекцию(ArrayList<ArrayList<String>> dbData)
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                TableModel.addToApplicationData(DataBase.dbData); //выводим на интерфейс содержимое главной коллекции (ArrayList<ArrayList<String>> dbData)

                //обновляем интерфейс
                refreshInterface();

                //показываем информационное сообщение о завершении работы(получения данных, экспорта в БД и вывода на интерфейс приложения)
                JOptionPane.showConfirmDialog(null, "Данные загружены и сохранены!", "Получение данных", JOptionPane.PLAIN_MESSAGE);
            }
        });

        //кнопка Просмотр
        JButton viewButton = new JButton("Просмотр"); //задаем название кнопки
        viewButton.addActionListener(new ActionListener() { //задаем действия кнопки
            @Override
            public void actionPerformed(ActionEvent e) { //реакция на клик ЛКМ

                //получаем расположение выбранной строки в таблице
                int idRow = Frame.table.getSelectedRow();

                //проверяем выбрана ли строка
                if (idRow == -1) {
                    //показываем информационное сообщение об ошибке
                    JOptionPane.showConfirmDialog(null, "Нужно выбрать строку!", "Просмотр данных", JOptionPane.PLAIN_MESSAGE);
                } else {
                    final int idUrlColumn = 3; //задаем константу - 3й столбец содержит url
                    String url = Frame.tableModel.getValueAt(idRow, idUrlColumn).toString(); //получаем значение столбца(url) по заданной строке
                    OpenURI.open(url); //открываем вкладку в браузере(по умолчанию)
                }
            }
        });


        JTextField textField = new JTextField(10); //задаем текстовое поле для кнопки Поиск, параметр - ширина

        //кнопка Поиск
        JButton searchButton = new JButton("Поиск"); //задаем название кнопки
        searchButton.addActionListener(new ActionListener() { //задаем действия кнопки
            @Override
            public void actionPerformed(ActionEvent e) { //реакция на клик ЛКМ

                String textFieldText = textField.getText(); //получаем данные из текстового поля(запрос)

                Frame.tableModel.removeFromApplicationData(); //удаляем данные для замены

                //сначала выводим те данные, которые содержатся в БД
                try {
                    if (DataBase.getDataFromDB().size() != 0) { //получаем данные из БД, узнаем размер, если пустая - выдаем ошибку
                        //(используется главная колелкция ArrayList<ArrayList<String>> dbData
                        try {
                            DataBase.getSearchedDataFromDB(textFieldText); //получаем данные из БД c учетом передаваемого запроса, затем записываем в главную коллекцию
                            //(ArrayList<ArrayList<String>> dbData)
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        TableModel.addToApplicationData(DataBase.dbData); //выводим на интерфейс содержимое главной коллекции (ArrayList<ArrayList<String>> dbData)

                        //обновляем интерфейс
                        refreshInterface();

                        //показываем информационное сообщение о завершении работы(получения данных, экспорта в БД и вывода на интерфейс приложения)
                        JOptionPane.showConfirmDialog(null, "Производится выборка данных по запросу \"" + textFieldText + "\"",
                                "Выборка данных", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        //показываем информационное сообщение об ошибке
                        JOptionPane.showConfirmDialog(null, "База данных пуста. Сперва нужно нажать кнопку Старт",
                                "Получение данных", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //задаем размер кнопкам
        setSizeButton(startButton);
        setSizeButton(viewButton);
        setSizeButton(searchButton);

        //добавляем кнопки на панель
        add(startButton);
        add(viewButton);
        add(searchButton);
        add(textField);
    }

    public void refreshInterface() { //обновляем интерфейс
        Frame.scrollPane.revalidate();
        Frame.scrollPane.repaint();
        Frame.tableModel.fireTableDataChanged();
    }

    public void setSizeButton(JButton button) { //задаем размер передаваемым кнопкам
        button.setPreferredSize(new Dimension(100, 50));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}


