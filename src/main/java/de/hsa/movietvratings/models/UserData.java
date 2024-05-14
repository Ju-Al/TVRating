package de.hsa.movietvratings.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserData {
    @Id @GeneratedValue
    private long id;
    @Column(unique = true)
    private String username;
    private String password;

    public UserData(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

