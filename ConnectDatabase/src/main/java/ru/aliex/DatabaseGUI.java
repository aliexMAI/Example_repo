package ru.aliex;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

/**
 * Класс представляет GUI для подключения к БД SQLite
 */
public class DatabaseGUI extends JFrame {

    private int width, height;
    private JPanel pNorth, pSouth;
    private JLabel lInfo, lSql, lResult, lError;
    private JButton bOn, bOff, bExit, bAllTables, bAllColumns, bClear, bExecute;
    private JTextField tField, tSql;
    private JTextArea tArea;
    private JScrollPane sPane;
    private final String info = "<html> Укажите полный путь к базе данных, например: " +
            "D:/.../.../name_database.db</html>";
    private String path, querySql, noConnect = "Нет подключения";
    private DatabaseRepository repository = new DatabaseRepository();

    public DatabaseGUI() {

        super("Working with SQLite database");

        width = 1000; height = 650;

        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        lInfo = new JLabel(info);
        lInfo.setBounds(50, 10, 700, 30);
        lInfo.setFont(new Font("Cambria", Font.PLAIN, 16));

        tField = new JTextField();
        tField.setBounds(30, lInfo.getY() + 40, 600, 30);
        tField.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 16));

        lResult = new JLabel(noConnect);
        lResult.setBounds(30, tField.getY() + 40, 480, 30);
        lResult.setFont(new Font("Cambria", Font.BOLD, 17));
        lResult.setHorizontalAlignment(0);
        lResult.setBorder(BorderFactory.createLoweredBevelBorder());

        bOn = new JButton("Подключить");
        bOn.setBounds(tField.getWidth() + 60, lInfo.getY() + 40, 130, 30);
        bOn.setFocusPainted(false);
        bOn.addActionListener(e -> {

            path = tField.getText().trim();
            if(path.equals("") || path.isEmpty()) {
                if(!repository.isConnect()) {
                    lResult.setText(noConnect);
                }
                return;
            }
            if(checkPath(path) && repository.connectOn(path)) {
                lResult.setText("Подключение успешно создано");
            } else {
                lResult.setText("Ошибка подключения: проверьте путь или наличие БД");
            }
        });

        bOff = new JButton("Отключить");
        bOff.setBounds(bOn.getX() + 150,lInfo.getY() + 40, 130, 30 );
        bOff.setFocusPainted(false);
        bOff.addActionListener(e -> {

            if(repository.connectOff()) {
                tArea.setText("");
                tArea.setText("");
                lResult.setText(noConnect);
            }
        });

        bAllTables = new JButton("Список таблиц в БД");
        bAllTables.setBounds(lResult.getWidth() + 60, bOn.getY() + 40, 180, 30);
        bAllTables.setFocusPainted(false);
        bAllTables.addActionListener(e -> {

            if(!repository.isConnect()) return;
            List<String> list = repository.getListTables();
            if(list == null) {
                tArea.setText("Нет таблиц в БД");
            }
            tArea.setText("Таблицы: " + list);
        });

        bAllColumns = new JButton("Показать столбцы таблиц");
        bAllColumns.setBounds(bAllTables.getX() + 200, bOn.getY() + 40, 200, 30);
        bAllColumns.setFocusPainted(false);
        bAllColumns.addActionListener(e -> {

            if(!repository.isConnect()) return;
            List<String> tables = repository.getListTables();
            if(tables == null) return;

            String text = "";
            for(int i = 0; i < tables.size(); i++) {

                String table = tables.get(i);
                List<String> columns = repository.getListColumns(table);
                text += "Таблица: " + table + "\nСтолбцы: " + columns + "\n\n";
            }
            tArea.setText(text);
        });

        pNorth = new JPanel(null);
        pNorth.setPreferredSize(new Dimension(getWidth(), 140));
        pNorth.add(lInfo);
        pNorth.add(tField);
        pNorth.add(lResult);
        pNorth.add(bOn);
        pNorth.add(bOff);
        pNorth.add(bAllTables);
        pNorth.add(bAllColumns);

        tArea = new JTextArea();
        tArea.setFont(new Font(Font.DIALOG, Font.PLAIN, 17));
        tArea.setEditable(false);
        sPane = new JScrollPane(tArea);
        sPane.setBounds(20, lResult.getY() + 40, 950, 150);

        lSql = new JLabel("Введите SQL запрос (только для отображения данных):");
        lSql.setBounds(30, 10, 430, 30);
        lSql.setFont(new Font("Cambria", Font.PLAIN, 16));

        tSql = new JTextField();
        tSql.setBounds(30, lSql.getY() + 40, 930, 30);
        tSql.setFont(new Font("Verdana", Font.PLAIN, 17));

        lError = new JLabel();
        lError.setBounds(30, tSql.getY() + 40, 480, 30);
        lError.setFont(new Font("Cambria", Font.BOLD, 17));
        lError.setHorizontalAlignment(0);
        lError.setBorder(BorderFactory.createLoweredBevelBorder());

        bClear = new JButton("Очистить запрос");
        bClear.setBounds(width - 400, tSql.getY() + 40, 140, 30);
        bClear.setFocusPainted(false);
        bClear.addActionListener(e -> tSql.setText(""));

        bExecute = new JButton("Выполнить");
        bExecute.setBounds(width - 160, tSql.getY() + 40, 120, 30);
        bExecute.setFocusPainted(false);
        bExecute.addActionListener(e -> {

            lError.setText("");
            if(!repository.isConnect()) return;

            querySql = tSql.getText().trim();
            if(querySql.equals("") || querySql.isEmpty()) return;

            JTable table;
            try(ResultSet resultSet = repository.getDataQuery(querySql)) {

                if(resultSet == null) {
                    lError.setText("Неверный запрос");
                    return;
                }
                table = new JTable(viewTable(resultSet));
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.setRowHeight(30);

            } catch(Exception ex) {
                ex.printStackTrace();
                return;
            }
            JOptionPane.showMessageDialog(null, new JScrollPane(table),
                    "Результат запроса", JOptionPane.PLAIN_MESSAGE);
        });

        bExit = new JButton("Выйти");
        bExit.setBounds(30, lError.getY() + 50, 100, 30);
        bExit.setFocusPainted(false);
        bExit.addActionListener(e -> System.exit(0));

        pSouth = new JPanel(null);
        pSouth.setPreferredSize(new Dimension(getWidth(), 180));
        pSouth.add(lSql);
        pSouth.add(tSql);
        pSouth.add(lError);
        pSouth.add(bClear);
        pSouth.add(bExecute);
        pSouth.add(bExit);

        add(pNorth, BorderLayout.NORTH);
        add(sPane, BorderLayout.CENTER);
        add(pSouth, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
     * Проверка пути к БД
     * @param path путь к БД
     * @return true если путь существует
     */
    private boolean checkPath(String path) {

        File file = new File(path);
        return file.exists();
    }

    /**
     * Создание модели для заполнения таблицы JTable из запроса в БД
     * @param resultSet объект с данными запроса из БД
     * @return модель таблицы, либо null
     */
    private DefaultTableModel viewTable(ResultSet resultSet) {

        if(resultSet == null) return null;

        try {

            DefaultTableModel tableModel = new DefaultTableModel();
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columns = metaData.getColumnCount();
            for(int i = 1; i <= columns; i++) {
                tableModel.addColumn(metaData.getColumnLabel(i));
            }

            Object[] row = new Object[columns];
            while(resultSet.next()) {

                for(int i = 0; i < columns; i++) {
                    row[i] = resultSet.getObject(i + 1);
                }
                tableModel.addRow(row);
            }
            return tableModel;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
