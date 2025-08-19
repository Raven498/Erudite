package com.erudite.erudite_node.dev_db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DevRepo extends JpaRepository<Pod, Long> {

    @Query(value = "select p from Pod p where p.ip = :ip")
    List<Pod> findByIp(String ip);
}
