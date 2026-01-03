package com.coupon.demo.mapper;

import com.coupon.demo.dto.request.CouponRequestDto;
import com.coupon.demo.dto.response.CouponResponseDto;
import com.coupon.demo.entity.CouponEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    CouponResponseDto toDto(CouponEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "code", qualifiedByName = "limparCodigo")
    @Mapping(target = "expirationDate", source = "expirationDate", qualifiedByName = "isoParaLocalDateTime")
    @Mapping(target = "status", expression = "java(com.coupon.demo.enums.CouponEnum.ACTIVE)")
    @Mapping(target = "redeemed", constant = "false")
    CouponEntity toEntity(CouponRequestDto request);

    @Named("limparCodigo")
    default String limparCodigo(String code) {
        if (code == null) return null;
        String limpo = code.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        if (limpo.length() > 6) throw new IllegalArgumentException("Código muito longo");
        return limpo;
    }

    @Named("isoParaLocalDateTime")
    default LocalDateTime isoParaLocalDateTime(String isoDate) {
        if (isoDate == null) return null;
        return ZonedDateTime.parse(isoDate).toLocalDateTime();
    }
}