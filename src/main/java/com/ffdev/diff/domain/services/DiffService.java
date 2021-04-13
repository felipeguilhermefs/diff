package com.ffdev.diff.domain.services;

import com.ffdev.diff.api.dtos.DiffResponse;
import com.ffdev.diff.domain.enums.Side;
import com.ffdev.diff.domain.exceptions.DiffSideNotFoundException;
import com.ffdev.diff.domain.exceptions.InvalidJsonException;
import com.ffdev.diff.domain.models.DiffSide;
import com.ffdev.diff.domain.repositories.DiffSideRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.ffdev.diff.shared.configs.CacheConfig.DIFF_CACHE;
import static com.ffdev.diff.domain.enums.Side.LEFT;
import static com.ffdev.diff.domain.enums.Side.RIGHT;
import static com.ffdev.diff.shared.helpers.Base64Helper.decodeB64;
import static com.ffdev.diff.shared.helpers.JSONHelper.isValidJSON;

@Service
public class DiffService {

    private final DiffSideRepository sideRepository;
    private final DiffCheckService checkService;

    public DiffService(DiffSideRepository sideRepository, DiffCheckService checkService) {
        this.sideRepository = sideRepository;
        this.checkService = checkService;
    }

    @CacheEvict(value = DIFF_CACHE, key = "#id")
    public void saveLeft(@NotNull String id, @NotNull String data) {
        save(LEFT, id, data);
    }

    @CacheEvict(value = DIFF_CACHE, key = "#id")
    public void saveRight(@NotNull String id, @NotNull String data) {
        save(RIGHT, id, data);
    }

    private void save(Side side, String id, String data) {
        var decodedData = decodeB64(data);

        if (!isValidJSON(decodedData)) {
            throw new InvalidJsonException();
        }

        sideRepository.save(new DiffSide(side, id, decodedData));
    }

    @Cacheable(value = DIFF_CACHE, key = "#id")
    public DiffResponse getById(@NotNull String id) {
        var left = getSide(LEFT, id);
        var right = getSide(RIGHT, id);

        return checkService.getDiff(left, right);
    }

    private String getSide(Side side, String id) {
        return sideRepository.getById(side, id)
                .orElseThrow(() -> new DiffSideNotFoundException(side));
    }
}
