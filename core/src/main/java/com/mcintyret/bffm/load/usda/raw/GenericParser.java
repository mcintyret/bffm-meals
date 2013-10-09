package com.mcintyret.bffm.load.usda.raw;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tommcintyre
 * Date: 9/10/13
 */
public abstract class GenericParser<T> {

    private static final Splitter SPLITTER = Splitter.on('^');

    private final String fileName;

    public GenericParser(String fileName) {
        this.fileName = fileName;
    }

    public List<T> parseList() throws IOException {
        List<String> lines = IOUtils.readLines(getClass().getResourceAsStream(fileName));
        List<T> results = new ArrayList<T>();

        for (String line : lines) {
            try {
                String[] parts = Iterables.toArray(SPLITTER.split(line), String.class);
                Object[] rawData = new Object[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    String stringPart = parts[i];
                    if (stringPart.startsWith("~") && stringPart.endsWith("~")) {
                        // this is a text field
                        rawData[i] = stringPart.substring(1, stringPart.length() - 1);
                    } else {
                        if (stringPart.isEmpty()) {
                            rawData[i] = -1F;
                        } else {
                            try {
                                rawData[i] = Float.parseFloat(stringPart);
                            } catch (Exception e) {
                                if (i == parts.length -1 && "ABCD".indexOf(parts[i]) >= 0) {
                                    // ignore
                                } else {
                                    throw e;
                                }
                            }
                        }
                    }
                }
                results.add(parseItem(rawData));
            } catch (Exception e) {
                System.out.println(line);
                e.printStackTrace();
            }
        }
        return results;
    }

    protected abstract T parseItem(Object[] rawData);
}
