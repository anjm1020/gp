package com.konkuk.core.socket;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SimpleMessageConvertor implements SocketMessageConvertor {

    @Override
    public <T> byte[] toByte(T object) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> fieldMap = new HashMap<>();

        Class<?> clazz = object.getClass();

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("is") || methodName.startsWith("get") && !methodName.equals("getClass")) {
                try {
                    String fieldName = extractFieldNameFromGetSetter(methodName);
                    Object val = method.invoke(object);
                    fieldMap.put(fieldName, val);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        sb.append("{");
        for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                sb.append("\"").append(entry.getValue()).append("\",");
            } else {
                sb.append(entry.getValue()).append(",");
            }

        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T toObject(byte[] bytes, Class<T> clazz) {
        String jsonString = new String(bytes);
        try {
            T object = clazz.getDeclaredConstructor().newInstance();

            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.startsWith("set")) {
                    String fieldName = extractFieldNameFromGetSetter(methodName);

                    // extract value from filedName
                    String fieldValue = getFieldFromJson(jsonString, fieldName);
                    if (fieldValue == null)
                        throw new Exception("Field not found");

                    // convert value from extracted value
                    Class<?> fieldType = method.getParameterTypes()[0];
                    Object val = convertToFieldType(fieldValue, fieldType);
                    if (val == null)
                        throw new Exception("Value Conversion Exception");

                    method.invoke(object, val);
                }
            }

            return object;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFieldFromJson(String jsonString, String fieldName) {
        int startIndex = jsonString.indexOf("\"" + fieldName + "\"");
        if (startIndex == -1) {
            return null;
        }

        int colonIndex = jsonString.indexOf(":", startIndex);
        int commaIndex = jsonString.indexOf(",", colonIndex);

        if (commaIndex == -1 && colonIndex == jsonString.lastIndexOf(':')) {
            commaIndex = jsonString.indexOf("}", colonIndex);
        }

        if (colonIndex == -1 || commaIndex == -1) {
            return null;
        }

        int valueStartIndex, valueEndIndex;
        String candidateString = jsonString.substring(colonIndex + 1, commaIndex);
        valueStartIndex = colonIndex + 1;
        valueEndIndex = commaIndex;
        if (candidateString.indexOf('\"') != -1) {
            valueStartIndex++;
            valueEndIndex--;
        }

        return jsonString.substring(valueStartIndex, valueEndIndex);
    }

    private Object convertToFieldType(String val, Class<?> fieldType) {
        if (fieldType == String.class) {
            return val;
        } else if (fieldType == int.class) {
            return Integer.parseInt(val);
        } else if (fieldType == boolean.class) {
            return Boolean.parseBoolean(val);
        }
        return null;
    }

    private String extractFieldNameFromGetSetter(String methodName) {
        int startIndexOfFieldName = methodName.startsWith("is") ? 2 : 3;
        String fieldName = methodName.substring(startIndexOfFieldName);
        return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
    }
}
