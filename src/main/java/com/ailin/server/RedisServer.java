package com.ailin.server;

import java.util.List;
import java.util.Map;

public interface RedisServer {

    /**
     * 通过key 查看redis数据
     * @param hkey
     * @param key
     * @return
     */
    Object findDataByKey(String hkey, String key);

    /**
     * 添加缓存
     * @param hkey
     * @param key
     * @param value
     * @return
     */
    int add(String hkey, String key, String value);

    Long delete(String hkey, Object... keys);


    List<String> values(String hkey);

    Map<String, Object> findAllByKey(String hkey);
}
