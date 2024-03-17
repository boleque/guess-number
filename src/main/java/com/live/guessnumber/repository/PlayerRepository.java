package com.live.guessnumber.repository;

import com.live.guessnumber.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByNickname(String nickName);
}
