package com.live.guessnumber.configuration;

import com.live.guessnumber.service.RoundResultEvaluationService;
import com.live.guessnumber.service.notifications.RoundResultNotificationService;
import com.live.guessnumber.service.notifications.StartedRoundNotificationService;
import com.live.guessnumber.service.notifications.WinStatisticsNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableIntegration
public class IntegrationConfiguration {

    private static final String NOTIFY_ABOUT_ROUND_STARTED_METHOD_NAME = "notifyRoundStarted";

    private static final String NOTIFY_ABOUT_ROUND_RESULT_METHOD_NAME = "notifyRoundResult";

    private static final String NOTIFY_ABOUT_WIN_STATS_METHOD_NAME = "notifyWinStatistics";

    private static final String EVALUATE_ROUND_RESULT_METHOD_NAME = "evaluateRoundResult";

    private static final String ROUND_RESULT_CHANNEL_NAME = "roundResultChannel";

    private static final int THREAD_POOL_SIZE = 5;

    @Bean
    public MessageChannelSpec<?, ?> startedRoundChannel() {
        return MessageChannels.publishSubscribe(executor());
    }

    @Bean
    public MessageChannelSpec<?, ?> finishedRoundChannel() {
        return MessageChannels.publishSubscribe(executor());
    }

    @Bean
    public MessageChannelSpec<?, ?> roundResultChannel() {
        return MessageChannels.publishSubscribe(executor());
    }

    @Bean
    public MessageChannelSpec<?, ?> winsStatisticsChannel() {
        return MessageChannels.publishSubscribe(executor());
    }

    @Bean
    public IntegrationFlow startRoundNotificationFlow(StartedRoundNotificationService startedRoundNotificationService) {
        return f -> f.channel(startedRoundChannel())
                .handle(startedRoundNotificationService, NOTIFY_ABOUT_ROUND_STARTED_METHOD_NAME)
                .log();
    }

    @Bean
    public IntegrationFlow roundEvaluationFlow(
            RoundResultEvaluationService roundResultEvaluationService
    ) {
        return f -> f.channel(finishedRoundChannel())
                .handle(roundResultEvaluationService, EVALUATE_ROUND_RESULT_METHOD_NAME)
                .channel(ROUND_RESULT_CHANNEL_NAME)
                .log();
    }

    @Bean
    public IntegrationFlow roundResultNotificationFlow(RoundResultNotificationService roundResultNotificationService) {
        return f -> f.channel(roundResultChannel())
                .split()
                .handle(roundResultNotificationService, NOTIFY_ABOUT_ROUND_RESULT_METHOD_NAME)
                .log();
    }

    @Bean
    public IntegrationFlow winStatisticsNotificationFlow(WinStatisticsNotificationService winStatisticsNotificationService) {
        return f -> f.channel(winsStatisticsChannel())
                .handle(winStatisticsNotificationService, NOTIFY_ABOUT_WIN_STATS_METHOD_NAME)
                .log();
    }

    @Bean
    public Executor executor() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }
}
