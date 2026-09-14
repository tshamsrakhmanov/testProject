package org.example.CustomCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConcurrentHashDequeService {

  private ConcurrentLinkedDeque<String> deque = new ConcurrentLinkedDeque<>();

  public void insertToHead(String value) {
    deque.add(value);
  }

  public String getFromTail() throws Exception {
    String result = deque.pollFirst();
    if (result != null) {
      return result;
    } else {
      throw new Exception("No elements in deque");
    }
  }

  public long getLength() {
    return deque.size();
  }

  public List<String> getAll() {
    return new ArrayList<String>(deque);
  }

  public void insertBulk(List<String> list) {
    for (String value : list) {
      deque.add(value);
    }
  }

}
