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

public abstract class SMCElement<Type> {
    protected static final String INDENT = "    ";

    private String key;
    private Type value;

    protected SMCElement(String key, Type value) {
        this.setKey(key);
        this.setValue(value);
    }

    protected final void checkString(Object object) {
        if (object instanceof String) {
            this.checkString((String) object);
        }
    }

    protected final void checkString(String string) {
        if (string.contains("\"")) {
            throw new IllegalArgumentException("Quote character may not be used");
        }
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Type getValue() {
        return this.value;
    }

    /**
     * Sets the key of this element.
     *
     * @param key new key for the element
     * @throws IllegalArgumentException if a " character is present
     */
    public final void setKey(String key) {
        this.checkString(key);
        this.key = key;
    }

    /**
     * Sets the value of this element.
     *
     * @param value new value for the element
     * @throws IllegalArgumentException if a " character is present
     */
    public final void setValue(Type value) {
        this.checkString(value);
        this.value = value;
    }

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        toString(builder, "");
        return builder.toString();
    }

    protected void toString(StringBuilder builder, String indent) {
        builder.append(indent).append('"').append(this.key).append('"');
        this.outputValue(builder, indent);
    }

    protected abstract void outputValue(StringBuilder builder, String indent);
}