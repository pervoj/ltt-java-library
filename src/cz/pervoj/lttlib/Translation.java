/*
 * Copyright (C) 2020 Vojtěch Perník <v.pernik@centrum.cz>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.pervoj.lttlib;

/**
 * @author Vojtěch Perník <v.pernik@centrum.cz>
 */
public class Translation {
    private String code;
    private String original;
    private String translation;

    /**
     * Constructor of the translation
     * @param code Translation code
     * @param original Original text
     * @param translation Translation text
     */
    public Translation(String code, String original, String translation) {
        this.code = code;
        this.original = original;
        this.translation = translation;
    }

    /**
     * Translation code getter
     * @return Translation code
     */
    public String getCode() {
        return code;
    }

    /**
     * Original text getter
     * @return Original text
     */
    public String getOriginal() {
        return original;
    }

    /**
     * Translation text getter
     * @return Translation text
     */
    public String getTranslation() {
        return translation;
    }
}
