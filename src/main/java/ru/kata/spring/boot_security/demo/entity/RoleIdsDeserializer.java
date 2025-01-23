package ru.kata.spring.boot_security.demo.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoleIdsDeserializer extends JsonDeserializer<List<Integer>> {

    @Override
    public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        List<Integer> roleIds = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode element: node) {
                roleIds.add(element.asInt());
            }
        } else if (node.isNumber()) {
            roleIds.add(node.asInt());
        }
        return roleIds;
    }
}
