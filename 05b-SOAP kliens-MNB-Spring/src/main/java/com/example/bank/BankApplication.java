package com.example.bank;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import soapclient.*;

@SpringBootApplication
@Controller
public class BankApplication {
	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}
	@GetMapping("/exercise1")
	@ResponseBody
	public String kiir1() throws MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage, MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage, MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage {
		MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
		MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();
		return service.getInfo() + "<br>" + service.getCurrentExchangeRates() + "<br>" + service.getExchangeRates("2022-08-14","2022-09-14","EUR");
	}
}
