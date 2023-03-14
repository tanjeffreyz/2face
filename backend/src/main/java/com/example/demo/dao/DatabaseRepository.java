package com.example.demo.dao;

import com.example.demo.entity.Image;
import com.example.demo.entity.Person;

public interface DatabaseRepository {

    int savePerson(Person person);

    int saveImage(Image image);

    Person getPerson(String userId);

    Image getImage(String userId);

}
