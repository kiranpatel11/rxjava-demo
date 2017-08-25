package com.att.idp.dashboard;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	
	public static String toJson(Object obj) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
	
	public static <T> T toPojo(String json, Class<T> clazz) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.readValue(json, clazz);
	}

}
