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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author Vojtěch Perník <v.pernik@centrum.cz>
 */
public class LTTGetter {
    private File trDir;
    private File translation;
    private String code;
    private String lang;
    private String author;
    private File pattern;
    private ArrayList<Translation> translations = new ArrayList<>();

    /**
     * Constructor of the official LTT library for Java
     * Will be used system language
     * @param trDir Directory, where are translations located
     */
    public LTTGetter(String trDir) throws Exception {
        setup(trDir, System.getProperty("user.language"));
    }

    /**
     * Constructor of the official LTT library for Java
     * You can specify language
     * @param trDir Directory, where are translations located
     * @param code Language code
     */
    public LTTGetter(String trDir, String code) throws Exception {
        setup(trDir, code);
    }

    private void setup(String trDir, String code) throws Exception {
        this.trDir = new File(trDir);
        this.code = code;
        translation = new File(this.trDir.getAbsolutePath() + File.separator + this.code + ".ltt");

        if (!translation.exists()) {
            for (File file : this.trDir.listFiles()) {
                String extension = "";
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = file.getName().substring(i+1);
                }
                if (extension.equals("lttp")) {
                    pattern = new File(this.trDir.getAbsolutePath() + File.separator + file.getName());
                    break;
                }
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pattern), StandardCharsets.UTF_8))) {
                String s;
                while ((s = br.readLine()) != null) {
                    if (s.startsWith("; Author:")) {
                        if (s.split(":").length >= 2) {
                            author = s.split(":")[1];
                        }
                    } else if (!s.startsWith(";")) {
                        String singlecode = s.split("=")[0];
                        String singlepattern = "";
                        if (s.split("=").length >= 2) {
                            singlepattern = s.split("=")[1];
                        }
                        translations.add(new Translation(singlecode, singlepattern, ""));
                    }
                }
            }
        } else {
            ArrayList<String> translations = new ArrayList<>();
            ArrayList<String> patterns = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(translation), StandardCharsets.UTF_8))) {
                String s;
                while ((s = br.readLine()) != null) {
                    if (s.startsWith("; Language:")) {
                        if (s.split(":").length >= 2) {
                            lang = s.split(":")[1];
                        }
                    } else if (s.startsWith("; Author:")) {
                        if (s.split(":").length >= 2) {
                            author = s.split(":")[1];
                        }
                    } else if (s.startsWith("; Pattern:")) {
                        if (s.split(":").length >= 2) {
                            pattern = new File(this.trDir.getAbsolutePath() + File.separator + s.split(":")[1]);
                        }
                    } else if (!s.startsWith(";")) {
                        translations.add(s);
                    }
                }
            }

            if (pattern != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pattern), StandardCharsets.UTF_8))) {
                    String s;
                    while ((s = br.readLine()) != null) {
                        if (!s.startsWith(";")) {
                            patterns.add(s);
                        }
                    }
                }

                for (int i = 0; i < patterns.size(); i++) {
                    String singlecode = patterns.get(i).split("=")[0];

                    String singlepattern = "";
                    if (patterns.get(i).split("=").length >= 2) {
                        singlepattern = patterns.get(i).split("=")[1];
                    }

                    String singletranslation = "";
                    for (int j = 0; j < translations.size(); j++) {
                        if (translations.get(j).split("=")[0].equals(singlecode) && translations.get(j).split("=").length >= 2) {
                            singletranslation = translations.get(j).split("=")[1];
                            break;
                        }
                    }

                    this.translations.add(new Translation(singlecode, singlepattern, singletranslation));
                }
            } else {
                for (int i = 0; i < translations.size(); i++) {
                    String singlecode = translations.get(i).split("=")[0];
                    String singletranslation = "";
                    if (translations.get(i).split("=").length >= 2) {
                        singletranslation = translations.get(i).split("=")[1];
                    }
                    this.translations.add(new Translation(singlecode, "", singletranslation));
                }
            }
        }
    }

    /**
     * Getter of one pattern or translation
     * @param code Translation code
     * @return Pattern or translation
     */
    public String getText(String code) {
        String text = "";
        for (int i = 0; i < translations.size(); i++) {
            Translation t = translations.get(i);
            if (t.getCode().equals(code)) {
                text = t.getTranslation();
                if (text.equals("")) {
                    text = t.getOriginal();
                }
                break;
            }
        }
        return replace(text);
    }

    private String replace(String text) {
        text = text.replace("{[equal sign]}", "=");
        text = text.replace("{[semicolon]}", ";");
        text = text.replace("{[opening curly brace]}", "{");
        text = text.replace("{[closing curly brace]}", "}");
        return text;
    }
}
