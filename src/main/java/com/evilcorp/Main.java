package com.evilcorp;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static final String exitMessage = " ������� Enter ��� ����������� ���������.";

    public static void main(String[] args) throws Exception {
        String TypeSort = "-a";
        String Type = "";
        String outputFileName;
        String text;
        String[] temp = null;
        System.out.println("������� ��������� ����������: -��� ����������(-a/-d) -��� ������(-i/-s)"
                + " ��� ��������� �����(out.txt)  ����� ������� ������(in1.txt)");

        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        text = read.readLine();
        temp = text.split(" ");
        if (temp.length < 3) {
            throw new Exception("����������� ������������ ���������:" + " ��� ������, ��� ��������� ����� ��� ��� �������� �����.\n");
        }
        read.close();

        int i = 0;
        //��������� ��������� ��� ����������
        if (temp[i].equals("-d")) {
            TypeSort = "-d";
            i++;
        } else if (temp[i].equals("-a")) {
            TypeSort = "-a";
            i++;
        } else if (!temp[i].equals("-i") || !temp[i].equals("-s")) {
            System.out.println("�� ������� ������� ��� ����������."
                    + " �������� ����� ������������� �� ����������� �� ���������.");
            TypeSort = "-a";
            i++;
        } else {
            System.out.println("�� ������� ������� ��� ����������."
                    + " �������� ����� ������������� �� ����������� �� ���������.");
            TypeSort = "-a";
        }
        //��������� �������� ��� ������
        if (temp[i].equals("-i") || temp[i].equals("-s")) {
            if (temp[i].equals("-i")) Type = "-i";
            else Type = "-s";
            i++;
        } else throw new Exception("�� ����� �������� ��� ������, ���������� �����.");
        //��������� ����
        outputFileName = temp[i];
        i++;

        String data[]; //������ �����
        int intArr[]; //������ ����� �����
        List<String> rawData = new ArrayList<>();

        for (int j = i; j < temp.length; j++) {
            try (BufferedReader reader = new BufferedReader(new java.io.FileReader(temp[j]))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.indexOf(' ') != -1)
                        throw new Exception("� ��� ������������ ������ � ������, ������� � ���������� �����");
                    rawData.add(line);
                }

            } catch (FileNotFoundException e) {
                System.out.println("������� ���� � ������� ������ �� ������." + exitMessage);
                exitByEnterPressed();
            } catch (IOException e) {
                System.out.println("������ ������ ������� ������." + exitMessage);
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
                };//���������� �� �������� � ������
                mergeStart(data, comp, TypeSort);

                for (int k = 0; k < data.length; k++)
                    writer.write(data[k] + "\n");
            }
        } catch (IOException e) {
            System.out.println("������ ������ �������� ������." + exitMessage);
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

    // �������� ������������ ���������� ���� ������ ���� ������ � �����
    public static String checkArrayType(String[] arr, String expectedType) {
        if (expectedType.equals("-i")) {
            try {
                int intArr[] = Arrays.stream(arr).mapToInt(value -> Integer.parseInt(value)).toArray();
            } catch (NumberFormatException e) {
                System.out.println("�� ��������� ������������� ������, ���������� ������, ��� ������ ����� �����." + " ��� ����������� ������ ����� �������.");
                return "-s";
            }
        } else if (expectedType.equals("-s")) {
            try {
                int intArr[] = Arrays.stream(arr).mapToInt(value -> Integer.parseInt(value)).toArray();
                System.out.println("�� ��������� ������������� ������ ����� ����� ��� ������ �����." + " ��� ����������� ������ ����� �������.");
                return "-i";
            } catch (NumberFormatException e) {
                return expectedType;
            }
        } else {
            System.out.println("�� ������� �������� ��� ������� ������." + " ������ ����� ������������� �� ��������� ��� ������ �����.");
            return "-s";
        }
        return expectedType;
    }

    public static void mergeStartInt(int[] array, String TypeSort) {
        int[] support = Arrays.copyOf(array, array.length);
        int startIndex = 0;
        int stopIndex = support.length - 1;
        mergeSort(array, support, startIndex, stopIndex, TypeSort);
    }

    public static void mergeSort(int[] array, int[] support, int startIndex, int endIndex, String TypeSort) {
        if (startIndex >= endIndex) { // ������� ��������� ����� ��������
            return;
        }
        int h = startIndex + (endIndex - startIndex) / 2;
        mergeSort(array, support, startIndex, h, TypeSort); //���������� ����� ���������������������
        mergeSort(array, support, h + 1, endIndex, TypeSort); // ���������� ������ ��������������������
        merge(array, support, startIndex, h, h + 1, endIndex, TypeSort); // ������� ����������������������
    }
    public static void merge(int[] array, int[] supportArray, int ls, int le, int rs, int re, String TypeSort) {
        for (int i = ls; i <= re; i++) {
            supportArray[i] = array[i];
        }
        int l = ls;
        int r = rs;
        for (int i = ls; i <= re; i++) {
            if (l > le) {
                array[i] = supportArray[r];
                r += 1;
            } else if (r > re) {
                array[i] = supportArray[l];
                l += 1;
            } else if (TypeSort.equals("-a") && supportArray[l] < supportArray[r]) {//��������� �� �����������
                array[i] = supportArray[l];
                l += 1;
            } else if (TypeSort.equals("-d") && supportArray[l] > supportArray[r]) { // ���������� �� ��������
                array[i] = supportArray[l];
                l += 1;
            } else {
                array[i] = supportArray[r];
                r += 1;
            }
        }
    }
    //���������� �����
    public static <T> void mergeStart(T[] array, Comparator<T> comp, String TypeSort) {
        T[] support = Arrays.copyOf(array, array.length);
        int startIndex = 0;
        int endIndex = support.length - 1;
        mergeSort(array, support, comp, startIndex, endIndex, TypeSort);
    }
    public static <T> void mergeSort(T[] array, T[] support, Comparator<T> comp, int startIndex, int endIndex, String TypeSort) {
        if (startIndex >= endIndex) {
            return;
        }
        int h = startIndex + (endIndex - startIndex) / 2;
        mergeSort(array, support, comp, startIndex, h, TypeSort);//���������� ����� ���������������������
        mergeSort(array, support, comp, h + 1, endIndex, TypeSort);// ���������� ������ ��������������������
        merge(array, support, comp, startIndex, h, h + 1, endIndex, TypeSort);// ������� ����������������������
    }
    public static <T> void merge(T[] array, T[] support, Comparator<T> comp, int ls, int le, int rs, int re, String TypeSort) {
        for (int i = ls; i <= re; i++) {
            support[i] = array[i];
        }
        int l = ls;
        int r = rs;
        for (int i = ls; i <= re; i++) {
            if (l > le) {
                array[i] = support[r];
                r += 1;
            } else if (r > re) {
                array[i] = support[l];
                l += 1;
            } else if (TypeSort.equals("-a") && comp.compare(support[l], support[r]) < 0) {
                array[i] = support[l];
                l += 1;
            } else if (TypeSort.equals("-d") && comp.compare(support[l], support[r]) > 0) {
                array[i] = support[l];
                l += 1;
            } else {
                array[i] = support[r];
                r += 1;
            }
        }
    }
}