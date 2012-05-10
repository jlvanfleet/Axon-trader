/*
 * Copyright (c) 2010-2012. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.tradeengine.command;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.AbstractEventSourcedEntity;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedEntity;
import org.axonframework.samples.trader.tradeengine.api.order.OrderId;
import org.axonframework.samples.trader.tradeengine.api.order.PortfolioId;
import org.axonframework.samples.trader.tradeengine.api.order.TradeExecutedEvent;
import org.axonframework.samples.trader.tradeengine.api.order.TransactionId;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Allard Buijze
 */
class Order extends AbstractAnnotatedEntity {

    private TransactionId transactionId;
    private final long itemPrice;
    private final long tradeCount;
    private final PortfolioId portfolioId;
    private long itemsRemaining;
    private OrderId orderId;

    public Order(OrderId orderId, TransactionId transactionId, long itemPrice, long tradeCount, PortfolioId portfolioId) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.itemPrice = itemPrice;
        this.tradeCount = tradeCount;
        this.itemsRemaining = tradeCount;
        this.portfolioId = portfolioId;
    }

    public long getItemPrice() {
        return itemPrice;
    }

    public long getTradeCount() {
        return tradeCount;
    }

    public PortfolioId getPortfolioId() {
        return portfolioId;
    }

    public long getItemsRemaining() {
        return itemsRemaining;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    private void recordTraded(long tradeCount) {
        this.itemsRemaining -= tradeCount;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    @EventHandler
    protected void onTradeExecuted(TradeExecutedEvent event) {
        if (orderId.equals(event.getBuyOrderId())
                || orderId.equals(event.getSellOrderId())) {
            recordTraded(event.getTradeCount());
        }
    }

    @Override
    protected Collection<AbstractEventSourcedEntity> getChildEntities() {
        return Collections.emptyList();
    }
}
