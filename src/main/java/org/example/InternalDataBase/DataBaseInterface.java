package org.example.InternalDataBase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface DataBaseInterface extends JpaRepository<DataBaseEntity, Long> {

  @Modifying
  @Transactional
  @Query("DELETE FROM DataBaseEntity m WHERE m.createdAt < :threshold")
  int deleteOlderThan(@Param("threshold") LocalDateTime threshold);

}
