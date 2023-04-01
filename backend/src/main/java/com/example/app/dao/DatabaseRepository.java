package com.example.app.dao;

import com.example.app.entity.Image;
import com.example.app.entity.Person;

public interface DatabaseRepository {

    int savePerson(Person person);

    int saveImage(Image image);

    Person getPerson(String userId);

    Image getImage(String userId);

}
