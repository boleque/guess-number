package com.live.guessnumber.service.scheduled;

import com.live.guessnumber.integration.FinishedRoundGateway;
import com.live.guessnumber.integration.StartedRoundGateway;
import com.live.guessnumber.service.RoundLifecycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class RoundLifecycleScheduledServiceImpl implements RoundLifecycleScheduledService {

    private static final Logger LOG = LoggerFactory.getLogger(RoundLifecycleScheduledServiceImpl.class);

    private final RoundLifecycleService roundService;

    private long currRoundId = -1;

    private final FinishedRoundGateway finishedRoundGateway;

    private final StartedRoundGateway startedRoundGateway;

    public RoundLifecycleScheduledServiceImpl(RoundLifecycleService roundService, FinishedRoundGateway finishedRoundGateway, StartedRoundGateway startedRoundGateway) {
        this.roundService = roundService;
        this.finishedRoundGateway = finishedRoundGateway;
        this.startedRoundGateway = startedRoundGateway;
    }

    @Scheduled(fixedRateString = "${game.round-rate-ms}")
    public void processRound() {
        long prevRoundId = currRoundId;
        if (currRoundId == -1) {
            currRoundId = roundService.startRound();
        } else {
            roundService.finishRound(currRoundId);
            notifyRoundFinished(currRoundId);

            currRoundId = roundService.startRound();
            notifyRoundStarted(currRoundId);
        }
        LOG.info("Process round lifecycle; Previous: {}; Current: {}", prevRoundId, currRoundId);
    }

    private void notifyRoundStarted(long roundId) {
        startedRoundGateway.process(roundId);
    }

    private void notifyRoundFinished(long roundId) {
        finishedRoundGateway.process(roundId);
    }
}
