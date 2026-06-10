package com.axisbanking.common.event;

public final class EventType {
    private EventType() {}

    // Account Events
    public static final String ACCOUNT_CREATED = "ACCOUNT_CREATED";
    public static final String ACCOUNT_UPDATED = "ACCOUNT_UPDATED";
    public static final String ACCOUNT_CLOSED = "ACCOUNT_CLOSED";
    public static final String CUSTOMER_CREATED = "CUSTOMER_CREATED";

    // Payment Events
    public static final String PAYMENT_INITIATED = "PAYMENT_INITIATED";
    public static final String PAYMENT_COMPLETED = "PAYMENT_COMPLETED";
    public static final String PAYMENT_FAILED = "PAYMENT_FAILED";

    // Loan Events
    public static final String LOAN_APPLIED = "LOAN_APPLIED";
    public static final String LOAN_APPROVED = "LOAN_APPROVED";
    public static final String LOAN_DISBURSED = "LOAN_DISBURSED";
    public static final String LOAN_CLOSED = "LOAN_CLOSED";

    // Deposit Events
    public static final String DEPOSIT_CREATED = "DEPOSIT_CREATED";
    public static final String DEPOSIT_MATURED = "DEPOSIT_MATURED";
    public static final String DEPOSIT_CLOSED = "DEPOSIT_CLOSED";

    // Card Events
    public static final String CARD_ISSUED = "CARD_ISSUED";
    public static final String CARD_BLOCKED = "CARD_BLOCKED";
    public static final String CARD_ACTIVATED = "CARD_ACTIVATED";

    // Investment Events
    public static final String INVESTMENT_CREATED = "INVESTMENT_CREATED";
    public static final String INVESTMENT_REDEEMED = "INVESTMENT_REDEEMED";

    // Insurance Events
    public static final String POLICY_CREATED = "POLICY_CREATED";
    public static final String POLICY_CANCELLED = "POLICY_CANCELLED";
    public static final String CLAIM_SUBMITTED = "CLAIM_SUBMITTED";
}
