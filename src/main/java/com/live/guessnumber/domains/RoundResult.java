package com.live.guessnumber.domains;


import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class RoundResult {

    private final long roundId;

    private final boolean win;

    private final String nickname;
}
