package tech.ada.banco.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.ada.banco.model.Conta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PixControllerTest extends BaseContaTest {
    private final String baseUri = "/pix";

    @Test
    void testPixContaOrigemInvalida() throws Exception {
        Conta contaBase = criarConta(BigDecimal.TEN);
        Optional<Conta> contaInexistente = repository.findContaByNumeroConta(9999);
        assertTrue(contaInexistente.isEmpty());


        String response =
                mvc.perform(post(baseUri + "/9999")
                                .param("destino", "" + contaBase.getNumeroConta())
                                .param("valor", "3.7")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andReturn().getResponse().getContentAsString();

        contaBase = obtemContaDoBanco(contaBase);
        assertEquals(BigDecimal.valueOf(10), contaBase.getSaldo());
    }

    @Test
    void testPixContaDestinoInvalida() throws Exception {
        Conta contaBase = criarConta(BigDecimal.TEN);
        Optional<Conta> contaInexistente = repository.findContaByNumeroConta(9999);
        assertTrue(contaInexistente.isEmpty());


        String response =
                mvc.perform(post(baseUri + "/" + contaBase.getNumeroConta())
                                .param("destino", "9999")
                                .param("valor", "3.7")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andReturn().getResponse().getContentAsString();

        contaBase = obtemContaDoBanco(contaBase);
        assertEquals(BigDecimal.valueOf(10), contaBase.getSaldo());
    }
}