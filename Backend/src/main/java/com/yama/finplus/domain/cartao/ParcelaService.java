package com.yama.finplus.domain.cartao;

import com.yama.finplus.repository.ParcelaRepository;
import org.springframework.stereotype.Service;

@Service
public class ParcelaService {
    private final ParcelaRepository parcelaRepository;

    public ParcelaService(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }
}
