package com.example.app.dao;
import com.example.app.entity.Image;
import com.example.app.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlDatabase implements DatabaseRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int savePerson(Person person) {

        return jdbcTemplate.update(
                "insert into Person (userid, fullName, email) values(?,?,?)",
                person.getUserID(), person.getFullName(), person.getEmail());

    }

    @Override
    public int saveImage(Image image) {

        return jdbcTemplate.update(
                "insert into Image (userid, image) values(?,?)",
                image.getUserID(), image.getImage());

    }

    @Override
    public Person getPerson(String userId) {
        String sql = "SELECT * FROM Person WHERE userid = ?";
        Person target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Person(
                        rs.getString("userid"),
                        rs.getString("fullName"),
                        rs.getString("age")
                ), new Object[] { userId });
        return target;
    }

    @Override
    public Image getImage(String userId) {
        String sql = "SELECT * FROM Image WHERE userid = ?";
        Image target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Image(
                        rs.getString("userid"),
                        rs.getBytes("image")
                ),new Object[]{userId});
        return target;

    }
}
