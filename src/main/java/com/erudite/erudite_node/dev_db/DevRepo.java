package com.erudite.erudite_node.dev_db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevRepo extends JpaRepository<Pod, Long> {

}
