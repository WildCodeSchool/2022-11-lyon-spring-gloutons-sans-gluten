package com.wcs.project3.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentSerializer extends JsonSerializer<List<Comment>> {

        @Override
        public void serialize(List<Comment> comments, JsonGenerator jsonGenerator, SerializerProvider
        serializerProvider) throws IOException {
        List<Map<String, Object>> serializedComments = new ArrayList<>();

        for (Comment comment : comments) {
            Map<String, Object> serializedComment = new HashMap<>();
//            serializedComment.put("id", comment.getId());
            serializedComment.put("title", comment.getComment());
            serializedComments.add(serializedComment);
        }

        jsonGenerator.writeObject(serializedComments);
    }
}
