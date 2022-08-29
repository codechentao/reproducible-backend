package com.huawei.openEuler.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具
 *
 * @author zhangshengjie
 * @since 2022-5-27
 * */
public class ProjectUtils {

    static String valueNull = "null";

    public static String timeCut(String startTime, String endTime){
        if (valueNull.equals(startTime) || valueNull.equals(endTime)){
            return "null";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        String result = "";
        try {
            if (!startTime.equals("") && !endTime.equals("")){
                Date c = simpleDateFormat.parse(startTime);
                Date d = simpleDateFormat.parse(endTime);
                long e = c.getTime();
                long f = d.getTime();
                long day = (f - e)/1000;
                long hh = day / 3600;
                long mm = (day % 3600) / 60;
                long ss = (day % 3600) % 60;
                result = hh+"h:"+mm+"m:"+ss+"s";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String timeTurn(String startTime){
        if (valueNull.equals(startTime)){
            return "null";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String result = "";
        try {
            if (!startTime.equals("")){
                Date c = simpleDateFormat.parse(startTime);
                result = simpleDateFormat2.format(c);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String testResult(String rpmName, String testResult){
        String result = null;
        switch (testResult){
            case (ConstantUtils.REPRODUCIBLE):
                result = rpmName + " is reproducible in our current test framework";
                break;
            case (ConstantUtils.UNREPRODUCIBLE):
                result = rpmName + " is unreproducible";
                break;
            case (ConstantUtils.FAILING_TO_BUILD):
                result = rpmName + " failed to build";
                break;
            case (ConstantUtils.IN_DEPWAIT_STATE):
                result = rpmName + " could not resolve dependencies";
                break;
            case (ConstantUtils.DOWNLOAD_PROBLEMS):
                result = rpmName + " download failed";
                break;
            case (ConstantUtils.BLACKLISTED):
                result = rpmName + "";
                break;
            case (ConstantUtils.UNKOWN_STATE):
                result = rpmName + "probably failed to build from source, please investigate";
                break;
            default:
        }
        return result;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


}
