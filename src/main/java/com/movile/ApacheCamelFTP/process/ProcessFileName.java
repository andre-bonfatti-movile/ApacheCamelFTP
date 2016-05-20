package com.movile.ApacheCamelFTP.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.SimpleBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProcessFileName implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		exchange.getIn().setHeader(Exchange.FILE_NAME, new SimpleBuilder("myfile.txt"));
		
		System.out.println(exchange.toString());
		
	}

	
	
}
