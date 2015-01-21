/*
 * Copyright 2015 Matt Baxter
 *
 * This file is part of Kitteh SMC Parser.
 *
 * Kitteh SMC Parser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kitteh SMC Parser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Kitteh SMC Parser.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kitteh.smcparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses SourceMod Configuration content to a Map.
 */
public final class SMCParser {
    private static final class Pair<Left, Right> {
        private final Left left;
        private final Right right;

        Pair(Left left, Right right) {
            this.left = left;
            this.right = right;
        }

        Left getLeft() {
            return this.left;
        }

        Right getRight() {
            return this.right;
        }
    }

    private static final Pattern PATTERN = Pattern.compile("(\"[^\"]*\")|([^\" \\t\\r\\n\\{\\}]+)|\\{|\\}|(//[^\\n]*\\r?\\n)");

    /**
     * Parses a given InputStream of SMC content.
     *
     * @param stream an input stream providing SMC content
     * @return a map of the SMC content
     * @throws InvalidSMCException for IO errors or invalid SMC content
     */
    public static Map<String, Object> parse(InputStream stream) {
        final char[] buffer = new char[8192];
        final StringBuilder out = new StringBuilder();
        try (final InputStream input = stream; final Reader reader = new InputStreamReader(input, "UTF-8")) {
            int read;
            while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                out.append(buffer, 0, read);
            }
        } catch (IOException e) {
            throw new InvalidSMCException("IO error reading stream", e);
        }
        return parse(out.toString());
    }

    /**
     * Parses a given String of SMC content.
     *
     * @param input SMC content
     * @return a map of the SMC content
     * @throws InvalidSMCException for IO errors or invalid SMC content
     */
    public static Map<String, Object> parse(String input) {
        Matcher matcher = PATTERN.matcher(input);
        Queue<String> sequence = new LinkedList<>();
        while (matcher.find()) {
            String found = matcher.group();
            if (found.startsWith("//")) {
                continue;
            }
            sequence.add(found);
        }

        return getMap(sequence);
    }

    private static Pair<String, Object> getPair(Queue<String> queue) {
        String first = queue.poll();
        if (first.equals("}")) {
            return null;
        }
        if (first.equals("{")) {
            throw new InvalidSMCException("Encountered unexpected \"{\" as a key");
        }

        // All that's left is Strings for the key
        first = stripQuote(first);

        String second = queue.poll();
        if (second == null) {
            throw new InvalidSMCException("Encountered unexpected end of file. Ended on a key!");
        }
        if (second.equals("{")) {
            return new Pair<>(first, getMap(queue));
        }
        if (second.equals("}")) {
            throw new InvalidSMCException("Encountered unexpected \"}\" as a value for key \"" + first + "\"");
        }

        // All that's left is Strings for the value
        second = stripQuote(second);

        return new Pair<>(first, second);

    }

    private static Map<String, Object> getMap(Queue<String> queue) {
        Map<String, Object> map = new LinkedHashMap<>();
        Pair<String, Object> pair;
        while (!queue.isEmpty() && (pair = getPair(queue)) != null) {
            map.put(pair.getLeft(), pair.getRight());
        }
        return map;
    }

    private static String stripQuote(String input) {
        return input.startsWith("\"") ? input.substring(1, input.length() - 1) : input;
    }
}