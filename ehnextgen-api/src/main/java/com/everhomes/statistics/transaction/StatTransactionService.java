package com.everhomes.statistics.transaction;

public interface StatTransactionService {

	StatTaskLog excuteSettlementTask(Long startDate, Long endDate);
}
