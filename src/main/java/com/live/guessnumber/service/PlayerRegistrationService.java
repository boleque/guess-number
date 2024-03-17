package com.live.guessnumber.service;

import com.live.guessnumber.model.Player;

import java.math.BigDecimal;

public interface PlayerRegistrationService {
    Player register(String nickName, BigDecimal balance);
}
