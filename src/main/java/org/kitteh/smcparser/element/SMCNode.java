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
package org.kitteh.smcparser.element;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class SMCNode extends SMCElement<List<SMCElement>> {
    public SMCNode(String key) {
        super(key, new CopyOnWriteArrayList<>());
    }

    /**
     * Gets an element by key.
     *
     * @param key key
     * @return the value associated with the key or null if not found
     */
    public SMCElement<?> getElement(String key) {
        return getType(key, SMCElement.class);
    }

    /**
     * Gets a node value by key.
     *
     * @param key key
     * @return the value associated with the key or null if not found
     */
    public SMCNode getNode(String key) {
        return getType(key, SMCNode.class);
    }

    /**
     * Gets a string value by key.
     *
     * @param key key
     * @return the value associated with the key or null if not found
     */
    public SMCString getString(String key) {
        return getType(key, SMCString.class);
    }

    private <Type> Type getType(String key, Class<Type> clazz) {
        for (SMCElement element : this.getValue()) {
            if (clazz.isAssignableFrom(element.getClass()) && element.getKey().equals(key)) {
                @SuppressWarnings("unchecked")
                Type type = (Type) element;
                return type;
            }
        }
        return null;
    }

    @Override
    protected void outputValue(StringBuilder builder, String indent) {
        if (this.getValue().isEmpty()) {
            builder.append(" { }\n");
        } else {
            builder.append('\n');
            builder.append(indent).append("{\n");
            final String newIndent = indent + SMCElement.INDENT;
            this.getValue().forEach(e -> e.toString(builder, newIndent));
            builder.append(indent).append("}\n");
        }
    }
}