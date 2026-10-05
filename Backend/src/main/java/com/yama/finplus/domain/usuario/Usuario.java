package com.yama.finplus.domain.usuario;

import com.yama.finplus.domain.financeiro.Financeiro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Financeiro> financeiros;

    public Usuario(DadosCadastroUsuario dadosCadastroUsuario) {
        this.username = dadosCadastroUsuario.username();
        this.email = dadosCadastroUsuario.email();
        this.senha = dadosCadastroUsuario.senha();
    }


}
