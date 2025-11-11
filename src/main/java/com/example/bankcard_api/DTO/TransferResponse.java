package com.example.bankcard_api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferResponse {
    private CardDTO from;
    private CardDTO to;
}
