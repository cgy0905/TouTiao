package com.cgy.news.utils;

import java.util.List;

/**
 * @author cgy
 * @description 和List相关的工具方法
 * @date 2019/5/10 10:36
 */
public class ListUtils {

    public static boolean isEmpty(List list) {
        if (list == null) {
            return true;
        }
        return list.size() == 0;
    }
}
