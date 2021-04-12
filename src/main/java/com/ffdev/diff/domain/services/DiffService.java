package com.ffdev.diff.domain.services;

import com.ffdev.diff.domain.enums.DiffSide;
import com.ffdev.diff.domain.exceptions.DiffSideNotFoundException;
import com.ffdev.diff.domain.exceptions.InvalidBase64Exception;
import com.ffdev.diff.domain.models.Diff;
import com.ffdev.diff.domain.repositories.DiffSideRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Base64;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class DiffService {
    private static final Logger logger = getLogger(DiffService.class);

    private final DiffSideRepository sideRepository;
    private final DiffCheckService checkService;

    public DiffService(DiffSideRepository sideRepository, DiffCheckService checkService) {
        this.sideRepository = sideRepository;
        this.checkService = checkService;
    }

    public void saveLeft(@NotNull String id, @NotNull String data) {
        save(DiffSide.LEFT, id, data);
    }

    public void saveRight(@NotNull String id, @NotNull String data) {
        save(DiffSide.RIGHT, id, data);
    }

    private void save(DiffSide side, String id, String data) {
        String decodedData = decodeB64(data);
        sideRepository.save(side, id, decodedData);
    }

    private String decodeB64(String encoded) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            return new String(decoded);
        } catch (IllegalArgumentException ex) {
            logger.warn("Error while decoding base64: {}", encoded);
            throw new InvalidBase64Exception();
        }
    }

    public Diff getById(@NotNull String id) {
        String left = getSide(DiffSide.LEFT, id);
        String right = getSide(DiffSide.RIGHT, id);

        return checkService.getDiff(left, right);
    }

    private String getSide(DiffSide side, String id) {
        return sideRepository.getById(side, id)
                .orElseThrow(() -> new DiffSideNotFoundException(side));
    }
}
