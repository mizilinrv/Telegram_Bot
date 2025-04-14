package com.mizilin.firstbot.repository;

import com.mizilin.firstbot.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

}
