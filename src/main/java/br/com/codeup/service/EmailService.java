package br.com.codeup.service;

import br.com.codeup.entities.User;

public interface EmailService {

    void notifyDelay(User user);

}