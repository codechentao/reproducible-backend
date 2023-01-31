package com.huawei.openEuler.utils;

/**
 * 常量
 *
 * @author zhangshengjie
 * @since 2022-5-27
 */
public class ConstantUtils {

    public static final String LOGGER_CONNECT_TRY = "Tring to connect elasticsearch";

    public static final String LOGGER_CONNECT_SUCCESS = "Connect elasticsearch successfully";

    public static final String LOGGER_CONNECT_FAILED = "Connect elasticsearch failed";

//    public static final String HOST_IP = "123.60.67.76";
//
//    public static final int HOST_PORT = 80;
//
//    public static final String HOST_SCHEME = "http";
//
//    public static final String HOST_USER = "root";
//
//    public static final String HOST_PASSWORD = "Helloman@12#$";

    public static final String HOST_IP = "123.60.15.242";

    public static final int HOST_PORT = 80;

    public static final String HOST_SCHEME = "http";

    public static final String HOST_USER = System.getenv("HOST_USER");

    public static final String HOST_PASSWORD = System.getenv("HOST_PASSWORD");

    public static final String TEST_STATUS = "test_status";

    public static final String CATEGORY_LEVEL = "category_level";

    public static final String ARCH = "arch";

    public static final String UPSTREAM_BRANCH = "upstream_branch";

    public static final String REPRODUCIBLE = "reproducible";

    public static final String UNREPRODUCIBLE = "unreproducible";

    public static final String FAILING_TO_BUILD = "failing to build";

    public static final String IN_DEPWAIT_STATE = "in depwait state";

    public static final String DOWNLOAD_PROBLEMS = "download problems";

    public static final String BLACKLISTED = "blacklisted";

    public static final String UNKOWN_STATE = "unknown state";

    public static final String TASK_START_TIME = "task_start_time";

    public static final String TASK_END_TIME = "task_end_time";

    public static final String ID = "id";

    public static final String PKG_NAME = "pkg_name";

    public static final String VERSION = "version";

//    public static final String RPM_NAME = "rpm_name";

//    public static final String TEST_RESULT = "result";

    public static final String BUILD_LOGS = "build_logs";

    public static final String BUILD_LOG = "buildlog";

    public static final String FIRST = "first";

    public static final String SECOND = "second";

    public static final String SIZE = "size";

    public static final String BUILD_INFOS = "build_infos";

    public static final String BUILD_INFO = "buildinfo";

    public static final String HASHKEY = "hashkey";

    public static final String DIFFOSCOPE_LOGS = "diffoscope_logs";

    public static final int TYPE_VALUE_1 = 1;

    public static final int TYPE_VALUE_2 = 2;

    public static final int TYPE_VALUE_3 = 3;

    public static final int TYPE_VALUE_4 = 4;
}