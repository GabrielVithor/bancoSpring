package tech.ada.banco.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.ada.banco.exceptions.ResourceNotFoundException;
import tech.ada.banco.model.Conta;
import tech.ada.banco.model.ModalidadeConta;
import tech.ada.banco.repository.ContaRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class PixTest {
    private final ContaRepository repository = Mockito.mock(ContaRepository.class);
    private final Pix pix = new Pix(repository);

    @Test
    void testPixContaOrigemNaoEncontrada() {
        Conta contaOrigem = new Conta(ModalidadeConta.CC, null);
        contaOrigem.deposito(BigDecimal.TEN);
        when(repository.findContaByNumeroConta(10)).thenReturn(Optional.of(contaOrigem));
        assertEquals(BigDecimal.valueOf(10), contaOrigem.getSaldo(), "O saldo inicial da conta deve ser alterado para 10");
        Conta contaDestino = new Conta(ModalidadeConta.CC, null);
        contaDestino.deposito(BigDecimal.ZERO);
        when(repository.findContaByNumeroConta(9)).thenReturn(Optional.of(contaDestino));
        assertEquals(BigDecimal.ZERO, contaDestino.getSaldo(), "O saldo inicial da conta deve ser alterado para 0");

        try {
            pix.executar(1, 9 ,BigDecimal.ONE);
            fail("A conta deveria não ter sido encontrada.");
        } catch (ResourceNotFoundException e) {

        }

        verify(repository, times(0)).save(any());
        assertEquals(BigDecimal.valueOf(10), contaOrigem.getSaldo(), "O saldo da contaOrigem não pode ter sido alterado.");
        assertEquals(BigDecimal.ZERO, contaDestino.getSaldo(), "O saldo da contaDestino não pode ter sido alterado.");
    }

    @Test
    void testPixContaDestinoNaoEncontrada() {
        Conta contaOrigem = new Conta(ModalidadeConta.CC, null);
        contaOrigem.deposito(BigDecimal.TEN);
        when(repository.findContaByNumeroConta(10)).thenReturn(Optional.of(contaOrigem));
        assertEquals(BigDecimal.valueOf(10), contaOrigem.getSaldo(), "O saldo inicial da conta deve ser alterado para 10");
        Conta contaDestino = new Conta(ModalidadeConta.CC, null);
        contaDestino.deposito(BigDecimal.ZERO);
        when(repository.findContaByNumeroConta(9)).thenReturn(Optional.of(contaDestino));
        assertEquals(BigDecimal.ZERO, contaDestino.getSaldo(), "O saldo inicial da conta deve ser alterado para 0");

        try {
            pix.executar(10, 1 ,BigDecimal.ONE);
            fail("A conta deveria não ter sido encontrada.");
        } catch (ResourceNotFoundException e) {

        }

        verify(repository, times(0)).save(any());
        assertEquals(BigDecimal.valueOf(10), contaOrigem.getSaldo(), "O saldo da contaOrigem não pode ter sido alterado.");
        assertEquals(BigDecimal.ZERO, contaDestino.getSaldo(), "O saldo da contaDestino não pode ter sido alterado.");
    }

    @Test
    void testPixProblemaDeBancoDeDadosContaOrigem() {
        Conta contaOrigem = new Conta(ModalidadeConta.CC, null);
        contaOrigem.deposito(BigDecimal.TEN);
        when(repository.findContaByNumeroConta(10)).thenThrow(RuntimeException.class);
        assertEquals(BigDecimal.valueOf(10), contaOrigem.getSaldo(), "O saldo inicial da conta deve ser alterado para 10");
        Conta contaDestino = new Conta(ModalidadeConta.CC, null);
        contaDestino.deposito(BigDecimal.ZERO);
        when(repository.findContaByNumeroConta(9)).thenReturn(Optional.of(contaDestino));
        assertEquals(BigDecimal.ZERO, contaDestino.getSaldo(), "O saldo inicial da conta deve ser alterado para 0");

        try {
            pix.executar(10,9, BigDecimal.ONE);
            fail("A conta deveria não ter sido encontrada. Por problema de conexao de banco de dados");
        } catch (RuntimeException e) {

        }

        verify(repository, times(0)).save(any());
        assertEquals(BigDecimal.TEN, contaOrigem.getSaldo(), "O saldo da contaOrigem não pode ter sido alterado.");
        assertEquals(BigDecimal.ZERO, contaDestino.getSaldo(), "O saldo da contaDestino não pode ter sido alterado.");
    }
    @Test
    void testPixProblemaDeBancoDeDadosContaDestino() {
        Conta contaOrigem = new Conta(ModalidadeConta.CC, null);
        contaOrigem.deposito(BigDecimal.TEN);
        when(repository.findContaByNumeroConta(10)).thenReturn(Optional.of(contaOrigem));
        assertEquals(BigDecimal.valueOf(10), contaOrigem.getSaldo(), "O saldo inicial da conta deve ser alterado para 10");
        Conta contaDestino = new Conta(ModalidadeConta.CC, null);
        contaDestino.deposito(BigDecimal.ZERO);
        when(repository.findContaByNumeroConta(9)).thenThrow(RuntimeException.class);
        assertEquals(BigDecimal.ZERO, contaDestino.getSaldo(), "O saldo inicial da conta deve ser alterado para 0");

        try {
            pix.executar(10,9, BigDecimal.ONE);
            fail("A conta deveria não ter sido encontrada. Por problema de conexao de banco de dados");
        } catch (RuntimeException e) {

        }

        verify(repository, times(0)).save(any());
        assertEquals(BigDecimal.TEN, contaOrigem.getSaldo(), "O saldo da contaOrigem não pode ter sido alterado.");
        assertEquals(BigDecimal.ZERO, contaDestino.getSaldo(), "O saldo da contaDestino não pode ter sido alterado.");
    }
}