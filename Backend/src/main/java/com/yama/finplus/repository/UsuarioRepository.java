package com.yama.finplus.repository;

import com.yama.finplus.domain.financeiro.Financeiro;
import com.yama.finplus.domain.financeiro.enums.TipoMovimentacao;
import com.yama.finplus.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //Como Username e email são unicos, tenho que deixar exposto isso

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);
}
