package org.example;
import com.oanda.v20.Context;
import com.oanda.v20.ContextBuilder;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.primitives.InstrumentName;
import static com.oanda.v20.instrument.CandlestickGranularity.H1;

public class HistoricalData {
    public static void main(String[] args) {
        Context ctx = new ContextBuilder(Config.URL).setToken(Config.TOKEN).setApplication("HistoricalData").build();

        try {
            InstrumentCandlesRequest request = new InstrumentCandlesRequest(new InstrumentName("EUR_USD"));
            request.setGranularity(H1);
            request.setCount(10L);	// 10 data	L: long datatype
            InstrumentCandlesResponse resp = ctx.instrument.candles(request);
            for(Candlestick candle: resp.getCandles())
                System.out.println(candle);
            for(Candlestick candle: resp.getCandles())
                System.out.println(candle.getTime()+"\t"+candle.getMid().getC());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
