package com.bendezu.yandexphotos;

import android.net.Uri;

public class UriUtils {

    //returns value related to key of URI fragment
    public static String getFragmentParameter(Uri uri, String key){
        String fragment = uri.getFragment();
        String[] parts = fragment.split("&");
        for (String part : parts){
            if (part.startsWith(key)){
                int beginIndex = part.indexOf('=') + 1;
                return part.substring(beginIndex);
            }
        }
        return null;
    }

}
