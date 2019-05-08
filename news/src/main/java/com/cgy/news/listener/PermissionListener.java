package com.cgy.news.listener;

import java.util.List;

/**
 * @author cgy
 * @description 权限申请回调的接口
 * @date 2019/5/8 15:54
 */
public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissions);
}
