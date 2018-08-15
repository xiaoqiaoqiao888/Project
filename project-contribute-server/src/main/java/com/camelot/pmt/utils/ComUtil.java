package com.camelot.pmt.utils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class ComUtil {

    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) {
            return isEmpty((String) obj);
        } else if (obj instanceof Long) {
            return isEmpty((Long) obj);
        } else if (obj instanceof Date) {
            return isEmpty((Date) obj);
        } else if (obj instanceof Collection) {
            return isEmpty((Collection<?>) obj);
        } else if (obj instanceof Map) {
            return isEmpty((Map<?, ?>) obj);
        } else if (obj != null && obj.getClass().isArray()) {
            return isEmptyArray(obj);
        } else {
            return isNull(obj);
        }
    }

    public static boolean isEmpty(Date date) {
        if (date == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(Long longVal) {
        if (longVal == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null || map.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Collection<?> cool) {
        if (cool == null || cool.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isEmptyArray(Object array) {
        int length = 0;
        if (array instanceof int[]) {
            length = ((int[]) array).length;
        } else if (array instanceof byte[]) {
            length = ((byte[]) array).length;
        } else if (array instanceof short[]) {
            length = ((short[]) array).length;
        } else if (array instanceof char[]) {
            length = ((char[]) array).length;
        } else if (array instanceof float[]) {
            length = ((float[]) array).length;
        } else if (array instanceof double[]) {
            length = ((double[]) array).length;
        } else if (array instanceof long[]) {
            length = ((long[]) array).length;
        } else if (array instanceof boolean[]) {
            length = ((boolean[]) array).length;
        } else {
            length = ((Object[]) array).length;
        }
        if (length == 0) {
            return true;
        }
        return false;
    }

    public static String trim(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean equals(String str1, String str2) {
        return str1 != null ? str1.equals(str2) : str2 == null;
    }

    public static boolean equals(Long long1, Long long2) {
        return long1 != null ? long1.equals(long2) : long2 == null;
    }

    public static boolean equals(Object obj1, Object obj2) {
        boolean result;
        if (obj1 != null) {
            result = obj1.equals(obj2);
        } else {
            result = (obj2 == null);
        }
        return result;
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null ? str1.equalsIgnoreCase(str2) : str2 == null;
    }

}
