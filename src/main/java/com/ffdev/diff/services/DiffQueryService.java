package com.ffdev.diff.services;

import com.ffdev.diff.dtos.DiffChangeDTO;
import com.ffdev.diff.dtos.DiffResultDTO;
import com.ffdev.diff.exceptions.DiffNotFoundException;
import com.ffdev.diff.repositories.DiffReadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.singletonList;

@Service
public class DiffQueryService {

    private final DiffReadRepository repository;

    public DiffQueryService(DiffReadRepository repository) {
        this.repository = repository;
    }

    public DiffResultDTO getById(String id) {
        String data = repository.getById(id).orElseThrow(DiffNotFoundException::new);
        List<DiffChangeDTO> changes = singletonList(new DiffChangeDTO("some-action", 0L, (long) data.length()));
        return new DiffResultDTO("some-result-status", changes);
    }
}
