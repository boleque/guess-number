package com.live.guessnumber;


import com.live.guessnumber.model.Player;
import com.live.guessnumber.repository.PlayerRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.live.guessnumber.service.PlayerRegistrationService;
import com.live.guessnumber.service.PlayerRegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ComponentScan(basePackages = {"com.live.guessnumber.repository"})
public class PlayerRegistrationServiceIntegrationTest {

    private final static String PLAYER_NICKNAME = "bot_1710488164282";

    private final static BigDecimal BALANCE = BigDecimal.valueOf(100);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    public void shouldCorrectlyRegisterPlayer() {
        PlayerRegistrationService playerRegistrationService = new PlayerRegistrationServiceImpl(playerRepository);
        Player player = playerRegistrationService.register(PLAYER_NICKNAME, BALANCE);

        Player expectedPlayer = em.find(Player.class, player.getId());
        assertThat(expectedPlayer).isNotNull().matches(s -> s.getNickname().equals(PLAYER_NICKNAME))
                .matches(s -> s.getBalance().compareTo(BALANCE) == 0);
    }
}
