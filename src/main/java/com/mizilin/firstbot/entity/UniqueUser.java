package com.mizilin.firstbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "quiz_schema", name = "unique_users")
public class UniqueUser {

    @Id
    private Long id;

    public UniqueUser(final Long id) {
        this.id = id;
    }
    public UniqueUser() {
    }

}
