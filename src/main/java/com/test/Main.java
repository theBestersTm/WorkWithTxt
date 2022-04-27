package com.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparing;

public class Main {
    private static final Map<String, List<Goods>> GOODS_MAP = new HashMap<>();
    private static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws IOException {
        InputStream resourceAsStream = new FileInputStream("data/input.txt");
        File file = new File("data/output.txt");
        file.createNewFile();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String line;
            while ((line = br.readLine()) != null && line.length() > 1) {
                String[] lineArr = line.trim().split(",");
                switch (lineArr[0]) {
                    case "u":
                        GOODS_MAP.computeIfAbsent(lineArr[lineArr.length - 1].toLowerCase(), s -> new ArrayList<>())
                                .add(new Goods(convertToLong(lineArr[1]), convertToInt(lineArr[2])));
                        break;
                    case "q":
                        if (lineArr.length < 3) {
                            Optional<Goods> max = GOODS_MAP.get(lineArr[1].substring(lineArr[1].indexOf("_") + 1).toLowerCase()).stream().max(comparing(Goods::getPrice).thenComparing(Goods::getSize));
                            max.ifPresent(goods -> result
                                    .append(goods.getPrice())
                                    .append(",")
                                    .append(goods.getSize())
                                    .append("\n"));
                        } else {
                            Optional<Integer> specificPrice = GOODS_MAP.values().stream()
                                    .flatMap(Collection::stream)
                                    .filter(goods -> goods.getPrice() == convertToLong(lineArr[2]))
                                    .map(Goods::getSize).findFirst();
                            specificPrice.ifPresent(specPrice -> result
                                    .append(specPrice)
                                    .append("\n"));
                        }
                        break;
                    case "o":
                        if (lineArr[1].equalsIgnoreCase("buy")) {
                            GOODS_MAP.get("ask").stream().min(comparing(Goods::getPrice))
                                    .ifPresent(goods -> goods.setSize(goods.getSize() - convertToInt(lineArr[2])));
                        } else if (lineArr[1].equalsIgnoreCase("sell")) {
                            GOODS_MAP.get("bid").stream().max(comparing(Goods::getPrice))
                                    .ifPresent(goods -> goods.setSize(goods.getSize() - convertToInt(lineArr[2])));
                        }
                        break;
                    default:
                }
            }
            if (result.length() > 50) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.append(result);
                writer.close();

                result = new StringBuilder();
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.append(result);
        writer.close();

        result = new StringBuilder();
    }

    private static long convertToLong(String str) {
        return Optional.ofNullable(str).filter(Main::isNumeric).map(Long::valueOf).orElse(0L);
    }

    private static int convertToInt(String str) {
        return Optional.ofNullable(str).filter(Main::isNumeric).map(Integer::valueOf).orElse(0);
    }

    public static boolean isNumeric(CharSequence cs) {
        if (cs != null && cs.length() != 0) {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
