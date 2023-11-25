package br.com.codeup.dao;

import java.util.List;

public interface Location {

    void save(br.com.codeup.entities.Location location);

    List<br.com.codeup.entities.Location> obtainPendingLease();

}