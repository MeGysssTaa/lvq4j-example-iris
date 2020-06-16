/*
 * Copyright 2020 DarksideCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.darksidecode.lvq4j.examples.iris;

import lombok.NonNull;
import me.darksidecode.lvq4j.BasicDataRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * @see me.darksidecode.lvq4j.DataRecord
 */
public class IrisDataRecord extends BasicDataRecord {

    private static final Map<Integer, String> labelMapping = new HashMap<>();

    static {
        labelMapping.put(0, "Iris-setosa"    );
        labelMapping.put(1, "Iris-versicolor");
        labelMapping.put(2, "Iris-virginica" );
    }

    /**
     * @see BasicDataRecord#BasicDataRecord()
     */
    public IrisDataRecord() {}

    /**
     * @see me.darksidecode.lvq4j.DataRecord#labelIdToLabelText(int)
     */
    @Override
    public String labelIdToLabelText(int labelId) {
        String labelText = labelMapping.get(labelId);

        if (labelText == null)
            throw new IllegalArgumentException(
                    "unrecognized iris species id: " + labelId);

        return labelText;
    }

    /**
     * @see me.darksidecode.lvq4j.DataRecord#labelTextToLabelId(String)
     */
    @Override
    public int labelTextToLabelId(@NonNull String labelText) {
        return labelMapping
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(labelText))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "unrecognized iris species name: " + labelText))
                .getKey();
    }

}
