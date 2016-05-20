package com.movile.ApacheCamelFTP.bin;

import org.apache.camel.CamelContext;
import org.apache.camel.component.file.AntPathMatcherGenericFileFilter;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.io.File;

/**
 * Created by Raphael_Gatti on 3/13/16.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.movile.ApacheCamelFTP")
public class ApacheCamelFTPConfiguration  {


}
