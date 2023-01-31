package com.huawei.openEuler.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huawei.openEuler.entity.ArgsModel;
import com.huawei.openEuler.entity.BuildInfo;
import com.huawei.openEuler.entity.DetailTable;
import com.huawei.openEuler.entity.StatisticalTable;
import org.elasticsearch.search.SearchHit;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具
 *
 * @author zhangshengjie
 * @since 2022-5-27
 */
public class ProjectUtils {

    static String valueNull = "null";

    public static String timeCut(String startTime, String endTime) {
        if (valueNull.equals(startTime) || valueNull.equals(endTime)) {
            return "null";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        String result = "";
        try {
            if (!startTime.equals("") && !endTime.equals("")) {
                Date c = simpleDateFormat.parse(startTime);
                Date d = simpleDateFormat.parse(endTime);
                long e = c.getTime();
                long f = d.getTime();
                long day = (f - e) / 1000;
                long hh = day / 3600;
                long mm = (day % 3600) / 60;
                long ss = (day % 3600) % 60;
                result = hh + "h:" + mm + "m:" + ss + "s";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String timeTurn(String startTime) {
        if (valueNull.equals(startTime)) {
            return "null";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String result = "";
        try {
            if (!startTime.equals("")) {
                Date c = simpleDateFormat.parse(startTime);
                result = simpleDateFormat2.format(c);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String testResult(String rpmName, String testResult) {
        String result = null;
        switch (testResult) {
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

    public static void statisticalResult(LinkedHashMap<String, LinkedHashMap<String, Long>> map, List<StatisticalTable> list) {
        for (String key1 : map.keySet()) {
            StatisticalTable statisticalTable = new StatisticalTable();
            statisticalTable.setCategoryLevel(key1);
            int sum = 0;
            for (String key2 : map.get(key1).keySet()) {
                sum += map.get(key1).get(key2);
            }
            statisticalTable.setAllPackage(String.valueOf(sum));
//            Long ftb = 0L;
//            if (map.get(key1).get("failing to build")!=null){
//                ftb = map.get(key1).get("failing to build");
//            }
            for (String key2 : map.get(key1).keySet()) {
                int value = map.get(key1).get(key2).intValue();
//                float value2 = (float) (value * 100) / (sum - ftb);
                float value2 = (float) (value * 100) / sum;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String s = decimalFormat.format(value2);
                switch (key2) {
                    case (ConstantUtils.REPRODUCIBLE):
                        statisticalTable.setReproducible(value + "(" + s + "%)");
                        break;
                    case (ConstantUtils.UNREPRODUCIBLE):
                        statisticalTable.setUnreproducible(value + "(" + s + "%)");
                        break;
                    case (ConstantUtils.FAILING_TO_BUILD):
                        statisticalTable.setFailingToBuild(value + "(" + s + "%)");
//                        statisticalTable.setFailingToBuild(String.valueOf(value));
                        break;
                    case (ConstantUtils.IN_DEPWAIT_STATE):
                        statisticalTable.setInDepwaitState(value + "(" + s + "%)");
                        break;
                    case (ConstantUtils.DOWNLOAD_PROBLEMS):
                        statisticalTable.setDownloadProblems(value + "(" + s + "%)");
                        break;
                    case (ConstantUtils.BLACKLISTED):
                        statisticalTable.setBlacklisted(value + "(" + s + "%)");
                        break;
                    case (ConstantUtils.UNKOWN_STATE):
                        statisticalTable.setUnknownState(value + "(" + s + "%)");
                        break;
                    default:
                }
            }
            list.add(statisticalTable);
        }
    }

    public static JSONObject chartingDate(LinkedHashMap<String, LinkedHashMap<String, Long>> map) {
        List<String> date = new ArrayList<>();
        List<Long> reproducible = new ArrayList<>();
        List<Long> unReproducible = new ArrayList<>();
        List<Long> failingToBuild = new ArrayList<>();
        List<Long> inDepwaitState = new ArrayList<>();
        List<Long> downloadProblems = new ArrayList<>();
        List<Long> blacklisted = new ArrayList<>();
        List<Long> unknownState = new ArrayList<>();
        for (String key : map.keySet()) {
            LinkedHashMap<String, Long> value = map.get(key);
            date.add(key);
            reproducible.add(value.getOrDefault(ConstantUtils.REPRODUCIBLE, 0L));
            unReproducible.add(value.getOrDefault(ConstantUtils.UNREPRODUCIBLE, 0L));
            failingToBuild.add(value.getOrDefault(ConstantUtils.FAILING_TO_BUILD, 0L));
            inDepwaitState.add(value.getOrDefault(ConstantUtils.IN_DEPWAIT_STATE, 0L));
            downloadProblems.add(value.getOrDefault(ConstantUtils.DOWNLOAD_PROBLEMS, 0L));
            blacklisted.add(value.getOrDefault(ConstantUtils.BLACKLISTED, 0L));
            unknownState.add(value.getOrDefault(ConstantUtils.UNKOWN_STATE, 0L));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", date);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", ConstantUtils.REPRODUCIBLE);
        jsonObject1.put("data", reproducible);
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name", ConstantUtils.UNREPRODUCIBLE);
        jsonObject2.put("data", unReproducible);
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("name", ConstantUtils.FAILING_TO_BUILD);
        jsonObject3.put("data", failingToBuild);
        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("name", ConstantUtils.IN_DEPWAIT_STATE);
        jsonObject4.put("data", inDepwaitState);
        JSONObject jsonObject5 = new JSONObject();
        jsonObject5.put("name", ConstantUtils.DOWNLOAD_PROBLEMS);
        jsonObject5.put("data", downloadProblems);
        JSONObject jsonObject6 = new JSONObject();
        jsonObject6.put("name", ConstantUtils.BLACKLISTED);
        jsonObject6.put("data", blacklisted);
        JSONObject jsonObject7 = new JSONObject();
        jsonObject7.put("name", ConstantUtils.UNKOWN_STATE);
        jsonObject7.put("data", unknownState);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(jsonObject1);
        jsonObjectList.add(jsonObject2);
        jsonObjectList.add(jsonObject3);
        jsonObjectList.add(jsonObject4);
        jsonObjectList.add(jsonObject5);
        jsonObjectList.add(jsonObject6);
        jsonObjectList.add(jsonObject7);
        JSONArray jsonArray = (JSONArray) JSONArray.toJSON(jsonObjectList);
        jsonObject.put("series", jsonArray);
        return jsonObject;
    }

    public static void parseHits(SearchHit[] hits, List<DetailTable> list, String prefix) {
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            DetailTable detailTable = new DetailTable();
            detailTable.setTableId(String.valueOf(sourceAsMap.get(ConstantUtils.ID)));
            detailTable.setCategoryLevel(String.valueOf(sourceAsMap.get(ConstantUtils.CATEGORY_LEVEL)));
            detailTable.setAllPackage(String.valueOf(sourceAsMap.get(ConstantUtils.PKG_NAME)));
            detailTable.setVersion(String.valueOf(sourceAsMap.get(ConstantUtils.VERSION)));
            detailTable.setTestDate(String.valueOf(
                ProjectUtils.timeTurn(String.valueOf(sourceAsMap.get(ConstantUtils.TASK_START_TIME)))));
            String duration = ProjectUtils.timeCut(String.valueOf(sourceAsMap.get(ConstantUtils.TASK_START_TIME)),
                String.valueOf(sourceAsMap.get(ConstantUtils.TASK_END_TIME)));
            detailTable.setTestDuration(duration);
            detailTable.setTestStatus(String.valueOf(sourceAsMap.get(ConstantUtils.TEST_STATUS)));
            if (sourceAsMap.get(ConstantUtils.BUILD_LOGS) != null) {
                JSONObject json = (JSONObject) JSONObject.toJSON(sourceAsMap.get(ConstantUtils.BUILD_LOGS));
                JSONObject first = (JSONObject) JSONObject.toJSON(json.get(ConstantUtils.FIRST));
                JSONObject second = (JSONObject) JSONObject.toJSON(json.get(ConstantUtils.SECOND));
                detailTable.setFirstBuildLog(prefix + first.get(ConstantUtils.BUILD_LOG));
                detailTable.setSecondBuildLog(prefix + second.get(ConstantUtils.BUILD_LOG));
                if (first.get(ConstantUtils.SIZE) != null) {
                    String firstSize = first.get(ConstantUtils.SIZE).toString();
                    long l;
                    if (firstSize.contains(",")) {
                        l = Long.parseLong(firstSize.replaceAll(",", ""));
                    } else {
                        l = Long.parseLong(firstSize);
                    }
                    detailTable.setFirstLogSize(ProjectUtils.readableFileSize(l));
                }
                if (second.get(ConstantUtils.SIZE) != null) {
                    String firstSize = second.get(ConstantUtils.SIZE).toString();
                    long l2;
                    if (firstSize.contains(",")) {
                        l2 = Long.parseLong(firstSize.replaceAll(",", ""));
                    } else {
                        l2 = Long.parseLong(firstSize);
                    }
                    detailTable.setSecondLogSize(ProjectUtils.readableFileSize(l2));
                }
            }
            if (sourceAsMap.get("rpms") != null) {
                JSONObject json = (JSONObject) JSONObject.toJSON(sourceAsMap.get("rpms"));
                for (String key : json.keySet()) {
                    JSONObject rpm = (JSONObject) JSONObject.toJSON(json.get(key));
                    JSONObject buildInfos = (JSONObject) JSONObject.toJSON(rpm.get(ConstantUtils.BUILD_INFOS));
                    JSONObject first = (JSONObject) JSONObject.toJSON(buildInfos.get(ConstantUtils.FIRST));
                    String firstBuildInfo = first.get(ConstantUtils.BUILD_INFO).toString();
                    String firstHashKey = first.get(ConstantUtils.HASHKEY).toString();
                    JSONObject second = (JSONObject) JSONObject.toJSON(buildInfos.get(ConstantUtils.SECOND));
                    String secondBuildInfo = second.get(ConstantUtils.BUILD_INFO).toString();
                    String secondHashKey = second.get(ConstantUtils.HASHKEY).toString();
                    BuildInfo buildInfo = new BuildInfo();
                    buildInfo.setFirstBuildInfo(prefix + firstBuildInfo);
                    buildInfo.setFirstHashkey(firstHashKey);
                    buildInfo.setSecondBuildInfo(prefix + secondBuildInfo);
                    buildInfo.setSecondHashkey(secondHashKey);
                    detailTable.getBuildInfos().add(buildInfo);
                    String status = rpm.get(ConstantUtils.TEST_STATUS).toString();
                    String result = ProjectUtils.testResult(key, status);
                    detailTable.getTestResult().add(result);
                    List<String> list1 = JSONObject.parseArray(rpm.getJSONArray(ConstantUtils.DIFFOSCOPE_LOGS).toJSONString(), String.class);
                    list1.replaceAll(s -> prefix + s);
                    detailTable.getDiffoscopeLogs().add(list1);
                }
            } else {
                detailTable.getTestResult().add(ConstantUtils.FAILING_TO_BUILD);
            }
            list.add(detailTable);
        }
    }
}
