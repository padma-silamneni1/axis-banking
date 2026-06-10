package com.axisbanking.common.event;

public final class KafkaTopics {
    private KafkaTopics() {}

    public static final String ACCOUNT_EVENTS = "axis.accounts.events";
    public static final String PAYMENT_EVENTS = "axis.payments.events";
    public static final String LOAN_EVENTS = "axis.loans.events";
    public static final String DEPOSIT_EVENTS = "axis.deposits.events";
    public static final String CARD_EVENTS = "axis.cards.events";
    public static final String INVESTMENT_EVENTS = "axis.investments.events";
    public static final String INSURANCE_EVENTS = "axis.insurance.events";
    public static final String NOTIFICATION_EVENTS = "axis.notifications.events";
    public static final String AUDIT_EVENTS = "axis.audit.events";
    public static final String DLQ_EVENTS = "axis.dlq.events";
}
