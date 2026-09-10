package org.example.CustomCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class CacheService {

  private ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

  public String put(String value) {
    String id = UUID.randomUUID().toString();
    cache.put(id, value);
    return id;
  }

  public String get(String id) {
    return cache.get(id);
  }

  public Map<String, String> getAll() {
    Map<String, String> result = new HashMap<String, String>();
    for (Entry<String, String> entry : cache.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      result.put(key, value);

    }
    return result;
  }

}
