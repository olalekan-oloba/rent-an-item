package com.omegalambdang.rentanitem.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.omegalambdang.rentanitem.feature.account.rentor.Rentor;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

public class TestUtils {

	public static String asJsonString(final Object obj) {
		try {
			ObjectMapper myObjectMapper = new ObjectMapper().findAndRegisterModules();
			myObjectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			return myObjectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T objectFromResponseStr(String response, String jsonPathStr) {
		return JsonPath.parse(response).read(jsonPathStr);
	}

	public static Map objToMap(Object o) {
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(o, Map.class);
		return map;
	}

	public static MockMultipartHttpServletRequestBuilder multipartBuilder(String path,Object... params) {
		MockMultipartHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.multipart(path ,params);
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});
		return builder;
	}

	public static boolean isValidJson(String json) {
		ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
		try {
			mapper.readTree(json);
		} catch (JacksonException e) {
			return false;
		}
		return true;
	}

	public static ResponseBodyMatchers responseBody() {
		return new ResponseBodyMatchers();
	}

	public static <T> T cloneObject(T object)  {
		ModelMapper mapper=new ModelMapper();
        try {
			Class<T> c= (Class<T>) Class.forName(object.getClass().getName());
			T s=(T)c.newInstance();
			mapper.map(object,s);
			return s;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
	}
}
