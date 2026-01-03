package com.coupon.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponRequestDto {

    @NotBlank(message = "O código é obrigatório")
    @Size(max = 20, message = "O código inserido é muito longo")
    private String code;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @NotNull(message = "O valor do desconto é obrigatório")
    @DecimalMin(value = "0.5", message = "O valor do desconto deve ser de no mínimo 0,5")
    private Double discountValue;

    @NotBlank(message = "A data de expiração é obrigatório")
    private String expirationDate;

    private boolean published;

    @AssertTrue(message = "A data de expiração não pode ser anterior ao momento atual")
    private boolean isDataFutura() {
        if (expirationDate == null || expirationDate.isBlank()) {
            return true;
        }
        try {
            ZonedDateTime dataInserida = ZonedDateTime.parse(expirationDate);

            return dataInserida.isAfter(ZonedDateTime.now());

        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
