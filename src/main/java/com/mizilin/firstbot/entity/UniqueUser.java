package com.mizilin.firstbot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(schema = "quiz_schema", name = "unique_users")
public class UniqueUser {

    @Id
    private Long id;

    public UniqueUser(Long id) {
        this.id = id;
    }
    public UniqueUser() {
    }

}
