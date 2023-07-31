package com.swave.urnr.util.gpt;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
@ApiIgnore
public class MessageSerializer extends JsonSerializer<Message> {
    @Override
    public void serialize(Message message, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("role", message.getRole());
        jsonGenerator.writeStringField("content", message.getContent());
        jsonGenerator.writeEndObject();
    }
}
