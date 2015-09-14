package com.payneteasy.webstart.client;

import io.airlift.airline.Command;
import io.airlift.airline.Option;

import java.math.BigDecimal;

@Command(name = "main", description = "payment client")
public class StartParameters {

    @Option(name="-amount", description = "Amount", required = true)
    public BigDecimal amount;

    @Option(name="-currency", description = "Currency, ex: RUB", required = true)
    public String     currency;

    @Option(name="-invoice", description = "Merchant's order number", required = true)
    public String     invoice;

    @Option(name="-description", description = "Order description", required = true)
    public String     description;

}
