package util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author jwolf
 * @Date 2021-10-29 22:24
 */
@Slf4j
public class GuavaUtils {

    private static LoadingCache<String, Object> localCache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .expireAfterAccess(10, TimeUnit.SECONDS)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .removalListener(removal -> {
                log.info(" [{}]被移除原因是 [{}] ", removal.getKey(), removal.getCause());
            })
            .build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String o) throws Exception {
                    return null;
                }
            });

    public static LoadingCache getLocalCacheInstance() {
        return localCache;
    }

    ;

    public static void set(String key, Object value) {
        localCache.put(key, value);
        log.info("[{}]缓存成功", key);
    }

    public static Object get(String key, Object defultValue) {
        try {
            return localCache.get(key);
        } catch (ExecutionException e) {
            log.info("获取缓存异常", e);
        } catch (CacheLoader.InvalidCacheLoadException e) {
            log.info("没有从缓存中获取到数据，返回为null----");
        }
        return defultValue;
    }

    public static void main(String[] args) throws ExecutionException {
        LoadingCache cache = GuavaUtils.getLocalCacheInstance();
        cache.put("aa", 111);
        cache.put("bb", 222);
        System.out.println(GuavaUtils.get("cc", "333"));
        System.out.println(cache.get("aa"));
        System.out.println(cache.asMap());
        cache.invalidateAll();
        System.out.println(cache.asMap());
    }


}
