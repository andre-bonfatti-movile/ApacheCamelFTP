package com.movile.ApacheCamelFTP.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.SimpleBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProcessFileName implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {

		String fullFilename = exchange.getIn().getHeader(Exchange.FILE_NAME).toString();

		String filename = fullFilename.substring(0, fullFilename.lastIndexOf("."));
		String customer_id = fullFilename.split("_", 2)[1].split("/")[0];

		filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
		filename = customer_id + "." + filename;

		exchange.getIn().setHeader(Exchange.FILE_NAME, new SimpleBuilder(filename));
	}

	
	
}
