package com.mizilin.firstbot.repository;

import com.mizilin.firstbot.entity.UniqueUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UniqueUserRepository extends JpaRepository<UniqueUser, Long> {

}
