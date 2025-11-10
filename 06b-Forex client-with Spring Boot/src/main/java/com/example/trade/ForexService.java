package com.example.trade;

import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.primitives.InstrumentName;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.trade.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForexService {

    private final Context ctx;

    public ForexService() {
        this.ctx = new ContextBuilder(Config.URL).setToken(Config.TOKEN).setApplication("ForexService").build();
    }

    public AccountSummary getAccountSummary() throws Exception {
        return ctx.account.summary(Config.ACCOUNTID).getAccount();
    }

    public PricingGetResponse getPricing(List<String> instruments) throws Exception {
        PricingGetRequest request = new PricingGetRequest(Config.ACCOUNTID, instruments);
        return ctx.pricing.get(request);
    }

    public InstrumentCandlesResponse getCandles(String instrument, String granularity, long count) throws Exception {
        InstrumentCandlesRequest request = new InstrumentCandlesRequest(new InstrumentName(instrument));
        switch (granularity) {
            case "M1": request.setGranularity(com.oanda.v20.instrument.CandlestickGranularity.M1); break;
            case "H1": request.setGranularity(com.oanda.v20.instrument.CandlestickGranularity.H1); break;
            case "D": request.setGranularity(com.oanda.v20.instrument.CandlestickGranularity.D); break;
            case "W": request.setGranularity(com.oanda.v20.instrument.CandlestickGranularity.W); break;
            case "M": request.setGranularity(com.oanda.v20.instrument.CandlestickGranularity.M); break;
            default: request.setGranularity(com.oanda.v20.instrument.CandlestickGranularity.D); break;
        }
        request.setCount(count);
        return ctx.instrument.candles(request);
    }

    public String openMarketOrder(String instrument, int units) throws Exception {
        InstrumentName instr = new InstrumentName(instrument);
        OrderCreateRequest request = new OrderCreateRequest(Config.ACCOUNTID);
        MarketOrderRequest marketorderrequest = new MarketOrderRequest();
        marketorderrequest.setInstrument(instr);
        marketorderrequest.setUnits(units);
        request.setOrder(marketorderrequest);
        OrderCreateResponse response = ctx.order.create(request);
        // return trade id from filled order
        return response.getOrderFillTransaction().getId().toString();
    }

    public List<Trade> listOpenTrades() throws Exception {
        return ctx.trade.listOpen(Config.ACCOUNTID).getTrades();
    }

    public void closeTrade(String tradeId) throws Exception {
        ctx.trade.close(new TradeCloseRequest(Config.ACCOUNTID, new TradeSpecifier(tradeId)));
    }
}
