package org.example.CustomCache;

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

}
