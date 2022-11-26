package edu.wustl.circ.DICOMAnalysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class ExtractData {
    private Map<String, Set<String>> extractMap = new HashMap<>();

    public Set<String> getKeys() {
        return extractMap.keySet();
    }
    public Set<String> getValues(String key) {
        return extractMap.get(key);
    }
    public void put(String key, String value) {
        if (extractMap.containsKey(key)) {
            extractMap.get(key).add(value);
        } else {
            Set<String> valueSet = new HashSet<>();
            valueSet.add(value);
            extractMap.put(key, valueSet);
        }
    }
}
