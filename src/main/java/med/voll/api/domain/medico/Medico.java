package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.endereco.Endereco;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;

    public Medico(DadosCadastroMedico data) {
        this.ativo = true;
        this.nome = data.nome();
        this.email = data.email();
        this.crm = data.crm();
        this.telefone = data.telefone();
        this.especialidade = data.especialidade();
        this.endereco = new Endereco(data.endereco());
    }

    public void atualizarInformacoes(DadosAtualizarMedico data) {
        if (data.nome() != null) {
            this.nome = data.nome();
        }

        if (data.telefone() != null) {
            this.telefone = data.telefone();
        }

        if (data.endereco() != null) {
            this.endereco.atualizarEndereco(data.endereco());
        }
    }

    public void inativarMedico() {
        this.ativo = false;
    }
}
