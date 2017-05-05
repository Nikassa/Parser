package com.home.parser;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class Frame extends JFrame {

    //задаем переменные для создания таблицы в приложении
    static TableModel tableModel = new TableModel();
    static JTable table = new JTable(tableModel);
    static JScrollPane scrollPane = new JScrollPane(table);

    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame("Список объявлений"); //создаем фрейм

        ButtonPanel buttonPanel = new ButtonPanel(); //создаем панель для кнопок, JButton + ActionListener
        buttonPanel.setPreferredSize(new Dimension(120, 500)); //задаем размеры панели
        frame.getContentPane().add(buttonPanel, BorderLayout.EAST); //задаем месторасположение панели на фрейме

        //задаем максимальную ширину колонкам
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setMaxWidth(120);
        table.getColumnModel().getColumn(2).setMaxWidth(200);
        table.getColumnModel().getColumn(3).setMaxWidth(430);

        frame.getContentPane().add(scrollPane, BorderLayout.WEST); //добавляем область с прокруткой на фрейм
        scrollPane.setPreferredSize(new Dimension(810, 500)); //задаем размеры области с прокруткой

        frame.pack(); //устанавливаем минимальный размер фрейма, который достаточен для отображения всех компонентов
        frame.setLocationRelativeTo(null); //располагаем фрейм по центру экрана монитора
        frame.setVisible(true); //задаем видимость фрейму
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE); //задаем автозавершение программы при выходе из приложения
    }
}



