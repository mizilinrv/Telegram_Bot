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
    private boolean isAdmin;


    public UniqueUser(Long id, boolean isAdmin) {
        this.id = id;
        this.isAdmin = isAdmin;
    }
    public UniqueUser() {
    }

}
