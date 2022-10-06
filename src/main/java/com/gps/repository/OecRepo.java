package com.gps.repository;

import com.gps.model.OecEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OecRepo extends JpaRepository<OecEntity,String> {
}
