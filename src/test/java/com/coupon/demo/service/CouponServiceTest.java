package com.coupon.demo.service;

import com.coupon.demo.dto.request.CouponRequestDto;
import com.coupon.demo.dto.response.CouponResponseDto;
import com.coupon.demo.entity.CouponEntity;
import com.coupon.demo.exception.ResourceNotFoundException;
import com.coupon.demo.mapper.CouponMapper;
import com.coupon.demo.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponMapper mapper;

    @Test
    @DisplayName("Deve criar um cupom com sucesso")
    void deveCriarCupomComSucesso() {
        CouponRequestDto request = new CouponRequestDto();
        CouponEntity entityAntesSalvar = new CouponEntity();
        CouponEntity entitySalva = new CouponEntity();
        entitySalva.setId(UUID.randomUUID());

        CouponResponseDto expectedResponse = new CouponResponseDto();
        expectedResponse.setId(String.valueOf(entitySalva.getId()));

        when(mapper.toEntity(request)).thenReturn(entityAntesSalvar);
        when(couponRepository.save(entityAntesSalvar)).thenReturn(entitySalva);
        when(mapper.toDto(entitySalva)).thenReturn(expectedResponse);

        CouponResponseDto result = couponService.criarCupom(request);

        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());

        verify(couponRepository, times(1)).save(entityAntesSalvar);
    }

    @Test
    @DisplayName("Deve buscar cupom por ID com sucesso")
    void deveBuscarCupomPorIdComSucesso() {
        UUID id = UUID.randomUUID();
        String idString = id.toString();
        CouponEntity entity = new CouponEntity();
        entity.setId(id);
        CouponResponseDto responseDto = new CouponResponseDto();
        responseDto.setId(String.valueOf(id));

        when(couponRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(responseDto);

        CouponResponseDto result = couponService.buscarCupomPorId(idString);

        assertNotNull(result);
        assertEquals(id.toString(), result.getId());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando ID não existir no banco")
    void deveLancarErroQuandoCupomNaoEncontradoAoBuscar() {
        UUID id = UUID.randomUUID();
        String idString = id.toString();

        when(couponRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> couponService.buscarCupomPorId(idString));

        verify(mapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando ID for nulo na busca")
    void deveLancarErroQuandoIdNuloNaBusca() {
        assertThrows(IllegalArgumentException.class, () -> couponService.buscarCupomPorId(null));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando formato do ID for inválido (não UUID)")
    void deveLancarErroQuandoIdFormatoInvalidoNaBusca() {
        assertThrows(IllegalArgumentException.class, () -> couponService.buscarCupomPorId("123-formato-errado"));
    }

    @Test
    @DisplayName("Deve deletar cupom com sucesso e retornar o DTO")
    void deveDeletarCupomComSucesso() {
        UUID id = UUID.randomUUID();
        String idString = id.toString();
        CouponEntity entity = new CouponEntity();
        entity.setId(id);
        CouponResponseDto responseDto = new CouponResponseDto();
        responseDto.setId(String.valueOf(id));

        when(couponRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(responseDto);

        CouponResponseDto result = couponService.deletarCupom(idString);

        assertEquals(id.toString(), result.getId());

        verify(couponRepository, times(1)).delete(entity);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar cupom inexistente")
    void deveLancarErroAoDeletarCupomInexistente() {
        UUID id = UUID.randomUUID();
        String idString = id.toString();

        when(couponRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> couponService.deletarCupom(idString));

        verify(couponRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando ID for nulo ao deletar")
    void deveLancarErroQuandoIdNuloAoDeletar() {
        assertThrows(IllegalArgumentException.class, () -> couponService.deletarCupom(null));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando formato do ID for inválido ao deletar")
    void deveLancarErroQuandoIdInvalidoAoDeletar() {
        assertThrows(IllegalArgumentException.class, () -> couponService.deletarCupom("id-invalido"));
    }
}