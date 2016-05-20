package com.movile.ApacheCamelFTP.bin;

import com.movile.ApacheCamelFTP.filter.PictureRSSFileFilter;
import com.movile.ApacheCamelFTP.process.ProcessFileName;

import org.apache.camel.BeanInject;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.BeanComponent;
import org.apache.camel.component.file.AntPathMatcherGenericFileFilter;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Raphael_Gatti on 3/13/16.
 */
@Component
public class ApacheCamelRouter extends RouteBuilder {

    @Autowired
    private PictureRSSFileFilter pictureRSSFileFilter;

    @Autowired
    private ProcessFileName processFileName;
    
    
    @Override
    public void configure() throws Exception {

        /*
        oi.ftp.host.uri=${recharge_producer_oi_ftp_host}
oi.ftp.username=${recharge_producer_oi_ftp_user}
oi.ftp.password=${recharge_producer_oi_ftp_pass}
oi.ftp.cron.schedule=0 0 4 1/1 * ? *
oi.ftp.file.download.minutes.delay=1
oi.ftp.files.inbox.in.pattern=RECARGA_*.txt,BASE_OPTOUT_*.txt
oi.ftp.recharge.file.in.pattern=RECARGA_*.txt
oi.ftp.optout.zipped.file.in.pattern=BASE_OPTOUT_*.zip
oi.ftp.optout.file.in.pattern=BASE_OPTOUT_*.txt


    private static final String AMERICA_SAO_PAULO_TIMEZONE = "America/Sao_Paulo";
    private static final String FTP_FILE_DONE_PATTERN = "${file:onlyname}.lido";
    private static final String FTP_FILE_ERROR_PATTERN = "${file:onlyname}-${date:now:yyyyMMdd-HHmmssSSS}.error";
         */

        StringBuilder fromUriOptout = new StringBuilder();
        fromUriOptout.append("ftp://localhost:21");
        fromUriOptout.append("?username=").append("ftpadm");
        fromUriOptout.append("&password=").append("admin");
        fromUriOptout.append("&autoCreate=false");
        fromUriOptout.append("&startingDirectoryMustExist=true");
        fromUriOptout.append("&noop=false");
        fromUriOptout.append("&readLock=rename");
        fromUriOptout.append("&antInclude=").append("**/upload/*.txt");
        fromUriOptout.append("&localWorkDirectory=").append("file://target/working");
        fromUriOptout.append("&move=").append(".done");
        fromUriOptout.append("&moveFailed=").append("${file:onlyname}-${date:now:yyyyMMdd-HHmmssSSS}.error");
        fromUriOptout.append("&disconnect=true");
        fromUriOptout.append("&passiveMode=true");
        fromUriOptout.append("&recursive=true");
        fromUriOptout.append("&delay=").append(TimeUnit.MILLISECONDS.convert(Long.valueOf(1L), TimeUnit.MINUTES));

        from(fromUriOptout.toString())
                .log("file:  ${in.header.CamelFileName}")
                .setHeader(Exchange.FILE_NAME,simple("myfile.txt")) 
                .process(processFileName)
                .to("file://target/input");


        StringBuilder workingDirUri = new StringBuilder();
        workingDirUri.append("file://target/input/");
        workingDirUri.append("?include=").append("*.txt");

       // Route from Working Directory to Done Directory for processing files.
        from(workingDirUri.toString()).id("workingOriginGold")
            .routeId("inboxToDoneGold")
            .log(LoggingLevel.INFO, "system", "Splitting file for processing: ${header.CamelFileName}")
            .split(body().tokenize("\n")).streaming()
                .doTry()
                    .log(LoggingLevel.DEBUG, "system", "Transforming ${body}...")
                    .log(LoggingLevel.DEBUG, "system", "Done transforming ${body}...")
                    .log(LoggingLevel.DEBUG, "system", "Starting to process Report: ${body}...")
                    .log(LoggingLevel.DEBUG, "system", "Done processing Report: ${body}...")                 
                 .doCatch(ValidationException.class, Exception.class)
                    .log(LoggingLevel.ERROR, "system", "Error when processing: ${body}")
                    .to("log:exceptions?level=ERROR&showException=true&showCaughtException=true&showStackTrace=true&multiline=true")
                    .convertBodyTo(String.class)
                    .to("file://target/error?fileExist=Append").id("errorDestinationGold")
                    .log(LoggingLevel.DEBUG, "system", "Error file appended with body: ${body}.")              
                .end()
            .end()            
            .to("file://target/done?readLock=rename")
        .end()
        .log(LoggingLevel.INFO, "system", "Done processing file: ${header.CamelFileName}");        
        /*

        from("file://target/input?delay=1000")
                .log("file:  ${in.header.CamelFileName}")
                .to("ftp://localhost:21/process/?username=marina&password=gomes");



        from("file:process/in.txt")
        .to("ftp://localhost/?username=marina&password=gomes")
                .routeId("Route FTP")
                //.to("file://test/readFile?autoCreate=true&moveFailed=.failed/${date:now:yyyyMMdd}/${file:name}&move=.process/${date:now:dd-MM-yyyy}/${file:name}")
                .log("Downloaded file complete.");
                */
    }


}
