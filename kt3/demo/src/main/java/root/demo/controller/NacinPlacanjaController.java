package root.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import root.demo.model.NaucnaOblast;
import root.demo.repository.NacinPlacanjaRepository;
import java.util.List;

@Controller
@RequestMapping("/nacinplacanja")
public class NacinPlacanjaController {
    @Autowired
    private NacinPlacanjaRepository nacinPlacanjaRepository;

    @GetMapping(path = "/get/all", produces = "application/json")
    public @ResponseBody ResponseEntity<List<NaucnaOblast>> getAll() {
        return new ResponseEntity(nacinPlacanjaRepository.findAll(),  HttpStatus.OK);
    }

}