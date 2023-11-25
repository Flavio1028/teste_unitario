package br.com.codeup.builder;

import br.com.codeup.entities.User;

public class UserBuilder {

    private User entity;

    private UserBuilder() {
    }

    public static UserBuilder user() {
        UserBuilder builder = new UserBuilder();
        builder.entity = new User();
        builder.entity.setName("Usuario 1");
        return builder;
    }

    public UserBuilder withName(String nome) {
        entity.setName(nome);
        return this;
    }

    public User builder() {
        return entity;
    }

}