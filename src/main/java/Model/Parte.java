package Model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_parte")
@PrimaryKeyJoinColumn(name = "id_usuario") // Chave primária que também é FK para Usuario
public class Parte extends Usuario {

    @Column(name = "numero_processo", nullable = false)
    private String numeroProcesso;


    public Parte() {

    }
    public String getNumeroProcesso() {
        return numeroProcesso; }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso; }
}