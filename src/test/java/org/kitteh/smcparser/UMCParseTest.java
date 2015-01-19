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

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Tests the Parser's ability to read and find things in a modified version
 * of the Ultimate Map Chooser plugin's example configuration.
 */
public final class UMCParseTest {
    @Test
    public void umc() throws IOException {
        Map<String, Object> map = SMCParser.parse(this.getClass().getResourceAsStream("/umc.smc"));
        try {
            Map<String, Object> umc = getMap(map, "umc_rotation");
            Map<String, Object> cp5 = getMap(umc, "5-Point CP (Push)");
            Map<String, Object> well = getMap(cp5, "cp_well");
            Assert.assertEquals("12", well.get("min_players"));
        } catch (Exception e) {
            throw new AssertionError("Failed to acquire :" + e.getMessage());
        }
    }

    private Map<String, Object> getMap(Map<String, Object> input, String key) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) input.get(key);
        return map;
    }
}