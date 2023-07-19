package com.konkuk.core;

import com.konkuk.core.socket.SimpleMessageConvertor;
import com.konkuk.core.socket.SocketMessageConvertor;
import com.konkuk.data.response.ClientResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MessageConvertor Test")
public class MessageConvertorTest {

    SocketMessageConvertor messageConvertor;
    Object object;
    String json;

    public MessageConvertorTest() {
        messageConvertor = new SimpleMessageConvertor();
        ClientResponse obj = new ClientResponse();
        obj.setType("type");
        obj.setScript("script");
        obj.setFinish(true);
        obj.setActive(false);
        obj.setDialog_id(1);
        object = obj;

        json = "{" +
                "\"type\":\"type\"," +
                "\"script\":\"script\"," +
                "\"finish\":true," +
                "\"active\":false," +
                "\"dialog_id\":1," +
                "}";

    }

    @Test
    @DisplayName("json to object")
    void jsonToObj() {
        ClientResponse resObject = messageConvertor.toObject(json.getBytes(StandardCharsets.UTF_8), ClientResponse.class);
        assertTrue(() -> isEqual(json, resObject));
        assertTrue(() -> isEqual(object, resObject, ClientResponse.class));
    }

    @Test
    @DisplayName("object to json")
    void objToJson() {
        byte[] bytes = messageConvertor.toByte(object);
        String resJson = new String(bytes);
        assertTrue(() -> isEqual(resJson, object));
    }


    boolean isEqual(Object obj1, Object obj2, Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        String methodName;
        for (Method method : methods) {
            methodName = method.getName();
            if (methodName.startsWith("is") ||
                    methodName.startsWith("get") && methodName.equals("getClass")) {
                try {
                    Object ret1 = method.invoke(obj1);
                    Object ret2 = method.invoke(obj2);
                    if (!ret1.equals(ret2)) return false;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    boolean isEqual(String json, Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        String fieldName, prefix, methodName;
        for (Field field : fields) {
            fieldName = field.getName();
            prefix = "get";
            if (field.getType() == boolean.class) {
                prefix = "is";
            }

            methodName = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                Method getter = clazz.getMethod(methodName);
                Object val = getter.invoke(object);
                Class<?> type = getter.getReturnType();

                String jsonValStr = getFieldValueFromJson(json, fieldName);

                if (jsonValStr == null) return false;

                Object jsonVal;
                if (type == String.class) {
                    jsonVal = jsonValStr;
                } else if (type == int.class) {
                    jsonVal = Integer.parseInt(jsonValStr);
                } else {
                    // boolean
                    jsonVal = Boolean.parseBoolean(jsonValStr);
                }

                if (!jsonVal.equals(val)) return false;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    /*
    @ filedName have to be NotBlank
     */
    boolean isFieldNameExist(String jsonString, String fieldName) {
        int idx = jsonString.indexOf(fieldName);

        String copy = jsonString.toString()
                .replace('{', ' ')
                .replace('}', ' ')
                .trim();

        String[] ss = copy.split("[:,]");
        for (int i = 0; i < ss.length; i += 2) {
            String curr = ss[i];

            curr.trim();

            if (!curr.contains(fieldName)) continue;
            if (!curr.startsWith("\"") || !curr.endsWith("\"")) {
                i--;
                continue;
            }
            return idx != -1 &&
                    curr.charAt(0) == '\"' &&
                    curr.charAt(curr.length() - 1) == '\"';
        }
        return false;
    }

    String getFieldValueFromJson(String jsonString, String fieldName) {

        if (!isFieldNameExist(jsonString, fieldName)) return null;

        String fName = "\"" + fieldName + "\"";
        int nameIdx = jsonString.indexOf(fName);
        nameIdx += fName.length() + 1;

        String valStr = "";
        for (int i = nameIdx; i < jsonString.length(); i++) {
            char curr = jsonString.charAt(i);
            if (curr == '}' || curr == ',') break;
            valStr = valStr + curr;
        }
        valStr.trim();
        if (valStr.startsWith("\"")) {
            valStr = valStr.replaceAll("\"", "");
            valStr.trim();
        }
        return valStr;
    }

}
