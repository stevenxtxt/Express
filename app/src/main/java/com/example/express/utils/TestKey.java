package com.example.express.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 项目名称：Express2015-4-24
 * 类描述：
 * 创建人：xutework
 * 创建时间：2015/8/13 11:57
 * 修改人：xutework
 * 修改时间：2015/8/13 11:57
 * 修改备注：
 */
public class TestKey {
    public static void setKey() {
        String str = "{\"mailNo\":\"1000506131746\"}";
        HashMap localHashMap = new HashMap();
        localHashMap.put("sign_method", "yd_md5");
        localHashMap.put("req_time", "20150813110104");
        try {
            localHashMap.put("data", new JSONObject(str));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        localHashMap.put("action", "member.order.get_mail_info");
        localHashMap.put("appver", "4.00.00");
        localHashMap.put("version", "v1.0");
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("sign_method=yd_md5&").append("req_time=20150813110104&").append("data=").append(new JSONObject(str)).append("&")
                    .append("action=member.order.get_mail_info&").append("appver=4.00.00&").append("version=v1.0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String paramString1 = sb.toString();
        String paramString2 = encodeRequest(paramString1, null, "UTF-8");
        Logger.show("----->>>>>>>>>>>>>>>>>>>>>>>>>>>>>", paramString2);
    }

    public static String map2QueryStr(Map<String, Object> paramMap)
    {
        if ((paramMap == null) || (paramMap.isEmpty()))
        {
            return null;
        }
        String str1 = "";
        Iterator localIterator = paramMap.keySet().iterator();
        for (;;)
        {
            if (!localIterator.hasNext())
            {
                if (str1.length() <= 1) {
                    break;
                }
                if (!"&".equals(str1.substring(str1.length() - 1))) {
                    break;
                }
                str1 = str1.substring(0, str1.length() - 1);
            }
            String str2 = (String)localIterator.next();
            str1 = str1 + str2 + "=" + paramMap.get(str2) + "&";
        }
        return str1;
    }

    public static String encodeRequest(String paramString1, String paramString2, String paramString3)
    {
        Map localMap = splitUrlQuery(paramString1);
        String[] arrayOfString = (String[])localMap.keySet().toArray(new String[0]);
        byte[] paramBytes;
        Arrays.sort(arrayOfString);
        StringBuilder localStringBuilder = new StringBuilder();
        int j = arrayOfString.length;
        int i = 0;
        for (;;)
        {
            if (i >= j)
            {
                if ((paramString2 != null) && (paramString2.length() > 0)) {
                    localStringBuilder.append(paramString2);
                }
                paramBytes = encryptMD5(localStringBuilder.toString(), paramString3);
                return paramString1 + "&sign=" + byte2hex(paramBytes);
            }
            String str1 = arrayOfString[i];
            String str2 = (String)localMap.get(str1);
            try
            {
                str2 = URLDecoder.decode(str2, paramString3);
//                if (StringUtils.areNotEmpty(new String[] { str1, str2 })) {
                    localStringBuilder.append(str1).append(str2);
//                }
                i += 1;
            }
            catch (UnsupportedEncodingException paramString)
            {
                throw new RuntimeException(paramString);
            }
        }
    }

    public static byte[] encryptMD5(String paramString1, String paramString2)
    {
        try
        {
            byte[] paramBytes = MessageDigest.getInstance("MD5").digest(paramString1.getBytes(paramString2));
            return paramBytes;
        }
        catch (GeneralSecurityException paramString)
        {
            throw new RuntimeException(paramString);
        }
        catch (UnsupportedEncodingException paramString)
        {
            throw new RuntimeException(paramString);
        }
    }

    public static String byte2hex(byte[] paramArrayOfByte)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        int i = 0;
        for (;;)
        {
            if (i >= paramArrayOfByte.length) {
                return localStringBuilder.toString();
            }
            String str = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
            if (str.length() == 1) {
                localStringBuilder.append("0");
            }
            localStringBuilder.append(str.toUpperCase());
            i += 1;
        }
    }

    public static Map<String, String> splitUrlQuery(String paramString)
    {
        String[] paramStrs;
        HashMap localHashMap = new HashMap();
        paramStrs = paramString.split("&");
        int j = 0;
        int i = 0;
        if ((paramStrs != null) && (paramStrs.length > 0))
        {
            j = paramStrs.length;
            i = 0;
        }
        for (;;)
        {
            if (i >= j) {
                return localHashMap;
            }
            String[] arrayOfString = paramStrs[i].split("=", 2);
            if ((arrayOfString != null) && (arrayOfString.length == 2)) {
                localHashMap.put(arrayOfString[0], arrayOfString[1]);
            }
            i += 1;
        }
    }
}
