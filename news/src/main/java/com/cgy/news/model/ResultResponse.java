package com.cgy.news.model;

/**
 * @author cgy
 * @description 访问返回的response
 * @date 2019/5/8 17:23
 */
public class ResultResponse<T> {
    public String has_more;
    public String message;
    public String success;
    public T data;

    public ResultResponse(String more, String _message, T result) {
        has_more = more;
        message = _message;
        data = result;
    }
}
