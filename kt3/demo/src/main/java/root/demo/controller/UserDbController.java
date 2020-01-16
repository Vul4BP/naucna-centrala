package root.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import root.demo.Dto.UserDto;
import root.demo.config.MyConfig;
import root.demo.model.UserDb;
import root.demo.services.others.UserDbService;
import root.demo.utils.ObjectMapperUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/userDb")
public class UserDbController {

    @Autowired
    private UserDbService userDbService;

    @GetMapping(path = "/get/{role}/all", produces = "application/json")
    public @ResponseBody ResponseEntity<List<UserDto>> getAllByRole(@PathVariable String role) {
        if(MyConfig.checkIfInRole(role) == false){
            return new ResponseEntity(new ArrayList<>(),  HttpStatus.BAD_REQUEST);
        }

        List<UserDto> dtos = new ArrayList<>();
        List<UserDb> users = userDbService.findByRole(role);

        for(UserDb user : users){
            UserDto userDto= ObjectMapperUtils.map(user, UserDto.class);
            userDto.setPassword("");
            userDto.setToken("");
            dtos.add(userDto);
        }

        return new ResponseEntity(dtos, HttpStatus.OK);
    }

}
