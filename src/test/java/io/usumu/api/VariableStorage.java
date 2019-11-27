package io.usumu.api;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VariableStorage {
    public final Map<String, String> variables = new HashMap<>();
}