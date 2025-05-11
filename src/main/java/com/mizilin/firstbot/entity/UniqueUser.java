package com.mizilin.firstbot.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

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
