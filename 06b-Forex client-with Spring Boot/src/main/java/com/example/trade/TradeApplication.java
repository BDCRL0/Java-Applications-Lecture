package com.example.trade;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.List;
import com.oanda.v20.trade.*;
import soapclient.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Controller
public class TradeApplication {
	public static void main(String[] args) {
		SpringApplication.run(TradeApplication.class, args);
	}

	private final ForexService forexService;

	public TradeApplication(ForexService forexService) {
		this.forexService = forexService;
	}
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/account_info")
	@ResponseBody
	public AccountSummary account_info()  {
		try {
			return forexService.getAccountSummary();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/account_info_page")
	public String account_info_page(Model model) {
		try {
			AccountSummary summary = forexService.getAccountSummary();
			model.addAttribute("account", summary);
		} catch (Exception e) {
			model.addAttribute("error", "Failed to load account information: " + e.getMessage());
		}
		return "account_info";
	}

	@GetMapping("/actual_prices")
	public String actual_prices(Model model) {
		model.addAttribute("par", new MessageActPrice());
		return "form_actual_prices";
	}

	@PostMapping("/actual_prices")
	public String actual_prices2(@ModelAttribute MessageActPrice messageActPrice, Model model) {
		String strOut="";
		List<String> instruments = new ArrayList<>( );
		instruments.add(messageActPrice.getInstrument());
		try {
			PricingGetResponse resp = forexService.getPricing(instruments);
			for (ClientPrice price : resp.getPrices())
				strOut+=price+"<br>";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		model.addAttribute("instr", messageActPrice.getInstrument());
		model.addAttribute("price", strOut);
		return "result_actual_prices";
	}

	@GetMapping("/hist_prices")
	public String hist_prices(Model model) {
		model.addAttribute("param", new MessageHistPrice());
		return "form_hist_prices";
	}

	@PostMapping("/hist_prices")
	public String hist_prices2(@ModelAttribute MessageHistPrice messageHistPrice, Model model) {
		String strOut;
		try {
			InstrumentCandlesResponse resp = forexService.getCandles(messageHistPrice.getInstrument(), messageHistPrice.getGranularity(), 10);
			strOut = "";
			for (Candlestick candle : resp.getCandles())
				strOut += candle.getTime() + "\t" + candle.getMid().getC() + ";";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		model.addAttribute("instr", messageHistPrice.getInstrument());
		model.addAttribute("granularity", messageHistPrice.getGranularity());
		model.addAttribute("price", strOut);
		return "result_hist_prices";
	}

	@GetMapping("/open_position")
	public String open_position(Model model) {
		model.addAttribute("param", new MessageOpenPosition());
		return "form_open_position";
	}

	@PostMapping("/open_position")
	public String open_position2(@ModelAttribute MessageOpenPosition messageOpenPosition, Model model) {
		String strOut;
		try {
			String tradeId = forexService.openMarketOrder(messageOpenPosition.getInstrument(), messageOpenPosition.getUnits());
			strOut="tradeId: "+tradeId;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		model.addAttribute("instr", messageOpenPosition.getInstrument());
		model.addAttribute("units", messageOpenPosition.getUnits());
		model.addAttribute("id", strOut);
		return "result_open_position";
	}

	@GetMapping("/positions")
	@ResponseBody
	public String positions()  {
		String strOut="Open positions:<br>";
		try {
			List<Trade> trades = forexService.listOpenTrades();
			for(Trade trade: trades)
				strOut+=trade.getId()+","+trade.getInstrument()+", "+trade.getOpenTime()+", "+trade.getCurrentUnits()+", "+trade.getPrice()+", "+trade.getUnrealizedPL()+"<br>";
			return strOut;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/positions_page")
	public String positions_page(Model model) {
		try {
			List<Trade> trades = forexService.listOpenTrades();
			model.addAttribute("trades", trades);
		} catch (Exception e) {
			model.addAttribute("error", "Failed to load positions: " + e.getMessage());
		}
		return "positions";
	}

	@GetMapping("/close_position")
	public String close_position(Model model) {
		model.addAttribute("param", new MessageClosePosition());
		return "form_close_position";
	}

	@PostMapping("/close_position")
	public String close_position2(@ModelAttribute MessageClosePosition messageClosePosition, Model model) {
		String tradeId= messageClosePosition.getTradeId()+"";
		String strOut="Closed tradeId= "+tradeId;
		try {
			forexService.closeTrade(tradeId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		model.addAttribute("tradeId", strOut);
		return "result_close_position";
	}

	// SOAP MNB Exchange Rates
	@GetMapping("/mnb-rates")
	public String mnbRates(Model model) {
		model.addAttribute("mnbRequest", new MNBExchangeRateRequest());
		return "mnb_rates_form";
	}

	@PostMapping("/mnb-rates")
	public String mnbRatesResult(@ModelAttribute MNBExchangeRateRequest request, Model model) {
		try {
			MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
			MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();
			
			String xmlResult = service.getExchangeRates(request.getStartDate(), request.getEndDate(), request.getCurrency());
			
			// Parse XML and extract rates for chart
			Map<String, String> rates = parseExchangeRatesXML(xmlResult);
			
			model.addAttribute("currency", request.getCurrency());
			model.addAttribute("startDate", request.getStartDate());
			model.addAttribute("endDate", request.getEndDate());
			model.addAttribute("rates", rates);
			model.addAttribute("xmlResult", xmlResult);
			
		} catch (Exception e) {
			model.addAttribute("error", "Error fetching exchange rates: " + e.getMessage());
		}
		return "mnb_rates_result";
	}

	private Map<String, String> parseExchangeRatesXML(String xmlString) {
		Map<String, String> rates = new HashMap<>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			
			NodeList dayNodes = doc.getElementsByTagName("Day");
			for (int i = 0; i < dayNodes.getLength(); i++) {
				Element dayElement = (Element) dayNodes.item(i);
				String date = dayElement.getAttribute("date");
				
				NodeList rateNodes = dayElement.getElementsByTagName("Rate");
				if (rateNodes.getLength() > 0) {
					Element rateElement = (Element) rateNodes.item(0);
					String rate = rateElement.getTextContent();
					rates.put(date, rate);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rates;
	}
}
