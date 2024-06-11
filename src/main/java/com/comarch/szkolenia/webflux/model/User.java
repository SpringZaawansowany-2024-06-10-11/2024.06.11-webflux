package com.comarch.szkolenia.webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tuser")
public class User {
    @Id
    private int id;
    private String login;
    private String pass;
    private String name;
    private String surname;
}
