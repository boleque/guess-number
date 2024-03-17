package com.live.guessnumber.service.notifications;

import com.live.guessnumber.domains.RoundResult;


public interface RoundResultNotificationService {
    void notifyRoundResult(RoundResult roundResult);
}
