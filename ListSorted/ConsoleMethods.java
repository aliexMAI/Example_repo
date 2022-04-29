package ListSorted;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ConsoleMethods {

    public static Scanner in = new Scanner(System.in);

    /**
     * Запрос строк от пользователя
     * @return список введенных строк
     */
    public List<String> getList() {

        int num = 0;

        System.out.println("\nВведите количество строк:");
        num = inputInt();

        List<String> list = new ArrayList<>(num);

        System.out.println("Введите строки:");
        for (int i = 0; i < num; ++i) {

            System.out.print(" " + (i + 1) + "-я строка > ");
            String str = in.nextLine().trim();
            list.add(str);
        }
        System.out.println();
        return list;
    }

    /**
     * Запрос числа и проверка корректности ввода
     * @return введенное число
     */
    private int inputInt() {

        int digit = 0;
        boolean point = true;

        while (point) {
            try {
                System.out.print("> ");
                digit = in.nextInt();
                if(digit < 0) digit = -digit;
                in.nextLine();
                point = false;
            } catch (Exception e) {
                System.out.println("\nНеверный ввод!");
                in.nextLine();
            }
        }
        return digit;
    }

    /**
     * Проверка на совпадение строк в двух списках
     * @param list1 первый список строк
     * @param list2 второй список строк
     * @return список с проверенными строками
     */
    public List<String> getListResult(List<String> list1, List<String> list2) {

        List<String> result = new ArrayList<>();
        List<String> temp = new ArrayList<>(list2);

        for(String str1: list1) {

            boolean point = true;

            for (String str2 : list2) {

                String[] strArr = str1.split(" ");
                for (int i = 0; i < strArr.length; ++i) {

                    if (strArr[i].length() <= 2 || strArr[i].equals("для")) continue;

                    if (str2.contains(strArr[i])) {
                        result.add(str1 + ":" + str2);
                        temp.remove(str2);
                        point = false;
                        break;
                    }
                }
            }
            if(point) {
                result.add(str1 + ":?");
            }
        }
        for(String str: temp) {
            result.add(str + ":?");
        }
        return result;
    }
}