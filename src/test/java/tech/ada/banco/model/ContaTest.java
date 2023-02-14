package tech.ada.banco.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContaTest {
    @Test
    void testCreateConta() {
       Conta contaTeste = new Conta(null,new Pessoa());

       assertEquals(null,contaTeste.getTipo());
       assertEquals(null,contaTeste.getTitular().getCpf());
       assertEquals(null,contaTeste.getTitular().getTelefone());
       assertEquals(null,contaTeste.getTitular().getNome());
    }
}