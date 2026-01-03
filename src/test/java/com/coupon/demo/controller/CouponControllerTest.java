package com.coupon.demo.controller;

import com.coupon.demo.dto.request.CouponRequestDto;
import com.coupon.demo.dto.response.CouponResponseDto;
import com.coupon.demo.enums.CouponEnum;
import com.coupon.demo.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CouponService couponService;

    @Test
    @DisplayName("Deve criar um cupom com sucesso e retornar status 201")
    void deveCriarCupomComSucesso() throws Exception {

        CouponRequestDto requestDto = new CouponRequestDto();
        requestDto.setCode("NATAL2");
        requestDto.setDescription("Desconto de Natal");
        requestDto.setDiscountValue(10.0);
        requestDto.setExpirationDate("2026-12-25T23:59:00Z");

        CouponResponseDto responseDto = new CouponResponseDto();
        responseDto.setId(String.valueOf(UUID.randomUUID()));
        responseDto.setCode("NATAL2");
        responseDto.setStatus(CouponEnum.ACTIVE);
        responseDto.setExpirationDate(String.valueOf(ZonedDateTime.now().plusDays(10)));

        when(couponService.criarCupom(any(CouponRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("NATAL2"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Deve buscar um cupom por ID e retornar status 200")
    void deveBuscarCupomPorId() throws Exception {
        String id = UUID.randomUUID().toString();

        CouponResponseDto responseDto = new CouponResponseDto();
        responseDto.setId(String.valueOf(UUID.fromString(id)));
        responseDto.setCode("TEST1");
        responseDto.setDescription("Teste de busca");

        when(couponService.buscarCupomPorId(id)).thenReturn(responseDto);

        mockMvc.perform(get("/coupon/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.code").value("TEST1"));
    }

    @Test
    @DisplayName("Deve deletar cupom e retornar status 204 com corpo")
    void deveDeletarCupom() throws Exception {
        String id = UUID.randomUUID().toString();

        CouponResponseDto deletedDto = new CouponResponseDto();
        deletedDto.setId(String.valueOf(UUID.fromString(id)));
        deletedDto.setCode("ABC123");
        deletedDto.setStatus(CouponEnum.ACTIVE);

        when(couponService.deletarCupom(id)).thenReturn(deletedDto);

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.code").value("ABC123"));
    }
}
