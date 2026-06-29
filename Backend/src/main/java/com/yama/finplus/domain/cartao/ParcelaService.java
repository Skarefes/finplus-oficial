package com.yama.finplus.domain.cartao;

import com.yama.finplus.repository.ParcelaRepository;

public class ParcelaService {
    private final ParcelaRepository parcelaRepository;

    public ParcelaService(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }

    public Integer quantidadeParcelas(){
        return null;
    }
}
