package io.usumu.api.variable;

import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Service
public class VariableStorage {
    private final Map<String, String> variables = new HashMap<>();

    public void store(String key, String value) {
        variables.put(key, value);
    }

    public boolean has(String key) {
        return variables.containsKey(key);
    }

    @Nullable
    public String get(String key) {
        return variables.get(key);
    }

    public String resolve(String value) {
        for (String key : variables.keySet()) {
            value = value.replace("${" + key + "}", variables.get(key));
        }
        return value;
    }
}
