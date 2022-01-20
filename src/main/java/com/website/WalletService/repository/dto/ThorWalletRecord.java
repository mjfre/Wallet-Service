package com.website.WalletService.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThorWalletRecord {

    UUID id;

    String thorWalletAddress;

    String terraWalletAddress;

}
