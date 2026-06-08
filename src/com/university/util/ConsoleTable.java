package com.university.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleTable {
    private List<String> headers;
    private List<List<String>> rows;
    private List<Integer> columnWidths;

    public ConsoleTable() {
        this.headers = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.columnWidths = new ArrayList<>();
    }

    public void setHeaders(String... headers) {
        this.headers = Arrays.asList(headers);
        updateWidths(this.headers);
    }

    public void addRow(String... rowValues) {
        List<String> row = Arrays.asList(rowValues);
        rows.add(row);
        updateWidths(row);
    }

    private void updateWidths(List<String> row) {
        for (int i = 0; i < row.size(); i++) {
            String value = row.get(i);
            int length = value != null ? value.length() : 0;
            if (i >= columnWidths.size()) {
                columnWidths.add(length);
            } else {
                if (length > columnWidths.get(i)) {
                    columnWidths.set(i, length);
                }
            }
        }
    }

    public void print() {
        if (headers.isEmpty() && rows.isEmpty()) {
            System.out.println("(Empty Table)");
            return;
        }

        // Add padding to widths
        List<Integer> paddedWidths = new ArrayList<>();
        for (int w : columnWidths) {
            paddedWidths.add(w + 2); // 1 space padding on left and right
        }

        // 1. Draw Top Border
        // ┌───────────┬──────────────┬─────────────┐
        printBorder(paddedWidths, '┌', '┬', '┐', '─');

        // 2. Draw Header
        if (!headers.isEmpty()) {
            printRow(headers, paddedWidths, true);
            // ├───────────┼──────────────┼─────────────┤
            printBorder(paddedWidths, '├', '┼', '┤', '─');
        }

        // 3. Draw Rows
        for (List<String> row : rows) {
            printRow(row, paddedWidths, false);
        }

        // 4. Draw Bottom Border
        // └───────────┴──────────────┴─────────────┘
        printBorder(paddedWidths, '└', '┴', '┘', '─');
    }

    private void printBorder(List<Integer> widths, char left, char joint, char right, char line) {
        StringBuilder sb = new StringBuilder();
        sb.append(ConsoleColor.CYAN);
        sb.append(left);
        for (int i = 0; i < widths.size(); i++) {
            for (int j = 0; j < widths.get(i); j++) {
                sb.append(line);
            }
            if (i < widths.size() - 1) {
                sb.append(joint);
            }
        }
        sb.append(right);
        sb.append(ConsoleColor.RESET);
        System.out.println(sb.toString());
    }

    private void printRow(List<String> row, List<Integer> widths, boolean isHeader) {
        StringBuilder sb = new StringBuilder();
        sb.append(ConsoleColor.CYAN).append("│").append(ConsoleColor.RESET);
        
        for (int i = 0; i < row.size(); i++) {
            String val = i < row.size() ? row.get(i) : "";
            if (val == null) val = "";
            int targetWidth = widths.get(i);
            
            String formattedVal = " " + val + " ";
            int spacing = targetWidth - formattedVal.length();
            
            sb.append(isHeader ? ConsoleColor.WHITE_BOLD : ConsoleColor.RESET);
            sb.append(" ");
            sb.append(val);
            for (int j = 0; j < spacing; j++) {
                sb.append(" ");
            }
            sb.append(" ");
            sb.append(ConsoleColor.CYAN).append("│").append(ConsoleColor.RESET);
        }
        System.out.println(sb.toString());
    }
}
