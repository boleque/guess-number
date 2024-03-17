package com.live.guessnumber.service;

import com.live.guessnumber.model.Player;
import com.live.guessnumber.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class PlayerRegistrationServiceImpl implements PlayerRegistrationService {

    private final PlayerRepository playerRepository;

    public PlayerRegistrationServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    @Override
    public Player register(String nickName, BigDecimal balance) {
        Player player = new Player();
        player.setNickname(nickName);
        player.setBalance(balance);
        return playerRepository.save(player);
    }
}
