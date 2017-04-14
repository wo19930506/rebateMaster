package com.edm.utils;


public class MailBox {
    
    private static char[] chars = { 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 000 - 015 0
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 016 - 031 1
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, // 032 - 047 2
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, // 048 - 063 3
        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // 064 - 079 4
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, // 080 - 095 5
        0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, // 096 - 111 6
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, // 112 - 127 7
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    
    private static boolean index(char c) {
        int idx = c;
        return (idx < 256 && chars[idx] != 0);
    }

    public static boolean validate(String email) {
        char[] chars = email.toCharArray();
        int begin = 0;
        int end = chars.length;
        boolean hasAt = false;
        boolean hasDomain = false;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '@') {
                if (hasAt || (i == begin) || (i + 1 >= end)) {
                    return false;
                }
                hasAt = true;
            } else if (hasAt && c == '.') {
                if (chars[i - 1] == '@' || i + 1 >= end) {
                    return false;
                }
                hasDomain = true;
            } else if (!index(c)) {
                return false;
            }
        }
        return hasAt && hasDomain;
    }
}
