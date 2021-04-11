package com.ffdev.diff.controllers;

import com.ffdev.diff.services.DiffCommandService;
import com.ffdev.diff.services.DiffQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/diff")
public class DiffController {

    private final DiffCommandService commandService;
    private final DiffQueryService queryService;

    public DiffController(DiffCommandService service, DiffQueryService queryService) {
        this.commandService = service;
        this.queryService = queryService;
    }

    @PostMapping("/{id}/left")
    public ResponseEntity<Void> saveLeft(@PathVariable String id, @RequestBody byte[] data) {
        commandService.saveLeft(id, data);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{id}/right")
    public ResponseEntity<Void> saveRight(@PathVariable String id, @RequestBody byte[] data) {
        commandService.saveRight(id, data);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDiff(@PathVariable String id) {
        return ResponseEntity.ok(queryService.getById(id));
    }
}
