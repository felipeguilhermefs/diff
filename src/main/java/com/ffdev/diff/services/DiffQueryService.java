package com.ffdev.diff.services;

import com.ffdev.diff.exceptions.DiffNotFoundException;
import com.ffdev.diff.repositories.DiffReadRepository;
import org.springframework.stereotype.Service;

@Service
public class DiffQueryService {

    private final DiffReadRepository repository;

    public DiffQueryService(DiffReadRepository repository) {
        this.repository = repository;
    }

    public String getById(String id) {
        return repository.getById(id).orElseThrow(DiffNotFoundException::new);
    }
}
