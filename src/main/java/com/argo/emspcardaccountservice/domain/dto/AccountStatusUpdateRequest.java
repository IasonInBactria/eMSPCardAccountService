package com.argo.emspcardaccountservice.domain.dto;


import com.argo.emspcardaccountservice.domain.enums.AccountStatus;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class AccountStatusUpdateRequest {
    @NotNull
    AccountStatus newStatus;
}
