package com.movile.ApacheCamelFTP.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Raphael_Gatti on 3/13/16.
 */
@SpringBootApplication
public class ApacheCamelFTPMain {

    public static void main(String... args)  throws Exception {
        SpringApplication.run(ApacheCamelFTPMain.class, args);
        Thread.sleep(1_000_000);
    }
}
