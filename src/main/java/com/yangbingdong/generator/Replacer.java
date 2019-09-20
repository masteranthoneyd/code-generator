package com.yangbingdong.generator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static com.yangbingdong.generator.config.ConstVal.TUPLE_MARK_END;
import static com.yangbingdong.generator.config.ConstVal.TUPLE_MARK_START;

/**
 * @author ybd
 * @date 2019/9/19
 * @contact yangbingdong1994@gmail.com
 */
public class Replacer {

    public static List<String> process(List<String> oldLines, List<String> newLines) {
        List<List<String>> extract = extract(newLines);
        return replace(new LinkedList<>(oldLines), extract);
    }

    private static List<List<String>> extract(List<String> newLines) {
        boolean markStart = false;
        List<String> temp = new ArrayList<>(16);
        List<List<String>> replaceLine = new ArrayList<>(4);
        for (String newLine : newLines) {
            if (newLine.contains(TUPLE_MARK_END)) {
                markStart = false;
                replaceLine.add(new ArrayList<>(temp));
                temp.clear();
                continue;
            }
            if (markStart) {
                temp.add(newLine);
            }
            if (newLine.contains(TUPLE_MARK_START)) {
                markStart = true;
            }
        }
        return replaceLine;
    }

    private static LinkedList<String> replace(LinkedList<String> oldLines, List<List<String>> newLineLists) {
        deleteOldMarkLines(oldLines);
        insertNewLines(oldLines, newLineLists);
        return oldLines;
    }

    private static void insertNewLines(LinkedList<String> oldLines, List<List<String>> newLineLists) {
        ListIterator<String> iterator = oldLines.listIterator();
        int index = 0;
        List<String> newLines = newLineLists.get(index);
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (next.contains(TUPLE_MARK_START)) {
                String end = iterator.next();
                if (!end.contains(TUPLE_MARK_END)) {
                    throw new IllegalArgumentException();
                }
                iterator.previous();
                for (String newLine : newLines) {
                    iterator.add(newLine);
                }
                if (newLineLists.size() > 1) {
                    newLines = newLineLists.get(++index);
                }
            }
        }
    }

    private static void deleteOldMarkLines(LinkedList<String> oldLines) {
        ListIterator<String> iterator = oldLines.listIterator();
        boolean del = false;
        while (iterator.hasNext()) {
            String next = iterator.next();
            if (del) {
                if (next.contains(TUPLE_MARK_END)) {
                    del = false;
                    continue;
                }
                iterator.remove();
            }
            if (next.contains(TUPLE_MARK_START)) {
                del = true;
            }
        }
    }
}
