package com.evilcorp;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static final String exitMessage = " Нажмите Enter для прекращения программы.";
    public static void main(String[] args) throws Exception {
        String TypeSort = "-a";
        String Type = "";
        String outputFileName;
        String text;
        String[] temp = null;
        System.out.println("Введите параметры сортировки: -тип сортировки(-a/-d) -тип данных(-i/-s)"
                + " имя выходного файла(out.txt)  имена входных файлов(in1.txt)");

        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        text = read.readLine();
        temp = text.split(" ");
        if (temp.length < 3) {
            throw new Exception("Отсутствуют обязательные параметры:" + " тип данных, имя выходного файла или имя входного файла.\n");
        }
        read.close();

        int i = 0;
        //Проверяем введенный тип сортировки
        if (temp[i].equals("-d")) {
            TypeSort = "-d";
            i++;
        } else if (temp[i].equals("-a")) {
            TypeSort = "-a";
            i++;
        } else if (!temp[i].equals("-i") || !temp[i].equals("-s")) {
            System.out.println("Вы неверно указали тип сортировки."
                    + " Элементы будут отсортированы по возрастанию по умолчанию.");
            TypeSort = "-a";
            i++;
        } else {
            System.out.println("Вы неверно указали тип сортировки."
                    + " Элементы будут отсортированы по возрастанию по умолчанию.");
            TypeSort = "-a";
        }
        //Проверяем введеный тип данных
        if (temp[i].equals("-i") || temp[i].equals("-s")) {
            if (temp[i].equals("-i")) Type = "-i";
            else Type = "-s";
            i++;
        } else throw new Exception("Вы ввели неверный тип данных, попробуйте снова.");
        //Выводимый файл
        outputFileName = temp[i];
        i++;

        String data[]; //массив строк
        int intArr[]; //массив целых чисел
        List<String> rawData = new ArrayList<>();

        for (int j = i; j < temp.length; j++) {
            try (BufferedReader reader = new BufferedReader(new java.io.FileReader(temp[j]))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.indexOf(' ') != -1)
                        throw new Exception("У вас присутствует пробел в строке, уберите и попробуйте снова");
                    rawData.add(line);
                }

            } catch (FileNotFoundException e) {
                System.out.println("Входной файл с заданым именем не найден." + exitMessage);
                exitByEnterPressed();
            } catch (IOException e) {
                System.out.println("Ошибка чтения входных данных." + exitMessage);
                exitByEnterPressed();
            }
        }

        data = new String[rawData.size()];
        rawData.toArray(data);
        Type = checkArrayType(data, Type);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            if (Type.equals("-i")) {
                intArr = Arrays.stream(data).mapToInt(value -> Integer.parseInt(value)).toArray();
                Comparator<Integer> com = (a, b) -> a.compareTo(b);
                mergeStartInt(intArr, TypeSort);
                for (int k = 0; k < intArr.length; k++)
                    writer.write(intArr[k] + "\n");
            } else {
                Comparator<String> comp = (a, b) -> {
                    if (a.length() != b.length()) {
                        return a.length() - b.length();
                    }
                    return a.compareTo(b);
                };//сортировка по алфавиту и длинне
                mergeStart(data, comp, TypeSort);

                for (int k = 0; k < data.length; k++)
                    writer.write(data[k] + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи выходных данных." + exitMessage);
        }

    }

    public static void exitByEnterPressed() {
        try {
            System.in.read();
            System.exit(1);
        } catch (IOException ignored) {
            System.out.println(ignored.getMessage());
        }
    }

    // Проверка соответствия выбранного типа данных типу данных в файле
    public static String checkArrayType(String[] arr, String expectedType) {
        if (expectedType.equals("-i")) {
            try {
                int intArr[] = Arrays.stream(arr).mapToInt(value -> Integer.parseInt(value)).toArray();
            } catch (NumberFormatException e) {
                System.out.println("Вы пытаетесь отсортировать массив, содержащий строки, как массив целых чисел." + " Тип сортируемых данных будет изменен.");
                return "-s";
            }
        } else if (expectedType.equals("-s")) {
            try {
                int intArr[] = Arrays.stream(arr).mapToInt(value -> Integer.parseInt(value)).toArray();
                System.out.println("Вы пытаетесь отсортировать массив целых чисел как массив строк." + " Тип сортируемых данных будет изменен.");
                return "-i";
            } catch (NumberFormatException e) {
                return expectedType;
            }
        } else {
            System.out.println("Вы указали неверный тип входных данных." + " Данные будут отсортированы по умолчанию как массив строк.");
            return "-s";
        }
        return expectedType;
    }
