package com.cgy.news.api;

import android.text.TextUtils;

import com.cgy.news.model.response.ResultResponse;
import com.cgy.news.utils.UIUtils;
import com.socks.library.KLog;

import rx.Subscriber;

/**
 * @author cgy
 * @description
 * @date 2019/5/8 17:22
 */
public abstract class SubscriberCallBack<T> extends Subscriber<ResultResponse<T>> {

    @Override
    public void onNext(ResultResponse response) {
        boolean isSuccess = (!TextUtils.isEmpty(response.message) && response.message.equals("success"))
                || !TextUtils.isEmpty(response.success) && response.success.equals("true");
        if (isSuccess) {
            onSuccess((T) response.data);
        } else {
            UIUtils.showToast(response.message);
            onFailure(response);
        }
    }

    @Override
    public void onError(Throwable e) {
        KLog.e(e.getLocalizedMessage());
        onError();
    }


    @Override
    public void onCompleted() {

    }

    protected abstract void onSuccess(T response);
    protected abstract void onError();

    protected void onFailure(ResultResponse response) {}


}
