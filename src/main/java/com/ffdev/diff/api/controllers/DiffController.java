package com.ffdev.diff.api.controllers;

import com.ffdev.diff.api.dtos.ResponseDTO;
import com.ffdev.diff.domain.services.DiffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/diff")
public class DiffController {

    private final DiffService service;

    public DiffController(DiffService service) {
        this.service = service;
    }

    @PostMapping("/{id}/left")
    public ResponseEntity<Void> saveLeft(@PathVariable String id, @RequestBody String data) {
        service.saveLeft(id, data);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{id}/right")
    public ResponseEntity<Void> saveRight(@PathVariable String id, @RequestBody String data) {
        service.saveRight(id, data);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getDiff(@PathVariable String id) {
        ResponseDTO reponse = ResponseDTO.from(service.getById(id));
        return ResponseEntity.ok(reponse);
    }
}
