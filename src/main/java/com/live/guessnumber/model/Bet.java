package com.live.guessnumber.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bets")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "bet_dt", nullable = false)
    private Timestamp betDt;

    @Column(name = "guessed_number", nullable = false)
    private int guess;

    @Column(name = "stake")
    private BigDecimal stake;

    @Column(name = "is_won", columnDefinition = "boolean default false")
    private boolean isWon;

    @ManyToOne(fetch = FetchType.LAZY)
    private Round round;

    @ManyToOne(fetch= FetchType.LAZY)
    private Player player;
}
