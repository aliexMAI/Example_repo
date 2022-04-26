package ru.edu;

import java.util.List;

public class ConsoleRun {
    public static void main(String[] args) {

        ConsoleMethods console = new ConsoleMethods();

        List<String> list1 = console.getList();
        List<String> list2 = console.getList();

        List<String> result = console.getListResult(list1, list2);
        for(String str: result) {
            System.out.println(" " + str);
        }
    }
}



