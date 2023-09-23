package com.task.inovus.constants;

import java.util.List;

public interface ApiConstants {
    List<Character> NUMBER_CHARACTERS = List.of(
            '\u0410', '\u0412', '\u0415', '\u041A', '\u041C', '\u041D',                 // А, В, Е, К, М, Н
            '\u041E', '\u0420', '\u0421', '\u0422', '\u0423', '\u0425');                        // О, Р, С, Т, У, Х

    int REPEAT_COUNT = 10;
    int MAX_NUMBER_VALUE = 1000;
    String LOCATION = "116 RUS";
}