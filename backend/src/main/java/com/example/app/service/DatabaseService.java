package com.example.demo.service;

import com.example.demo.dao.DatabaseRepository;
import com.example.demo.entity.Image;
import com.example.demo.entity.Person;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@CrossOrigin(origins = "${FRONTEND_HOST:*}")
@RestController
public class DatabaseService {
    @Autowired
    DatabaseRepository sqlDatabase;

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public ResponseEntity<String> postForm(@RequestBody String data) throws JSONException {
        JSONObject jsonObject= new JSONObject(data);
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String base64 = jsonObject.getString("image").replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);

        UUID uuid = UUID.randomUUID();
        String userId = uuid.toString();

        Person user = new Person(userId, name, email);
        Image image = new Image(userId, decode);

        sqlDatabase.savePerson(user);
        sqlDatabase.saveImage(image);

        ResponseEntity<String> result = new ResponseEntity<String>(HttpStatus.OK);

        return result;

    }

    @GetMapping("/image/{userId}")
    ResponseEntity<Image> getImage(@PathVariable String userId) throws IOException {

        Image image = sqlDatabase.getImage(userId);

        String imageName = image.getUserID() + ".png";

        saveImageToFile(image.getImage(), imageName);

        return new ResponseEntity<Image>(image, HttpStatus.OK);

    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        File dir = new File("/images/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Files.write(new File("/images/" + imageName).toPath(), image);
    }

}

