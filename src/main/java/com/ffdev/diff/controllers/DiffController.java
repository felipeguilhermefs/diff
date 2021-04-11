package com.ffdev.diff.controllers;

import com.ffdev.diff.services.DiffCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/diff")
public class DiffController {

    private final DiffCommandService commandService;

    public DiffController(DiffCommandService service) {
        this.commandService = service;
    }

    @PostMapping("/{id}/left")
    public ResponseEntity<Void> receiveLeft(@PathVariable String id, @RequestBody byte[] data) {
        commandService.saveLeft(id, data);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{id}/right")
    public ResponseEntity<Void> receiveRight(@PathVariable String id, @RequestBody byte[] data) {
        commandService.saveRight(id, data);

        return ResponseEntity.accepted().build();
    }
}
