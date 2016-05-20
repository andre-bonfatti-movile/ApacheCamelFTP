package com.movile.ApacheCamelFTP.filter;

import org.apache.camel.component.file.AntPathMatcherGenericFileFilter;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Raphael_Gatti on 3/14/16.
 */
@Component
public class PictureRSSFileFilter extends AntPathMatcherGenericFileFilter<File> {

    private static Logger log = LoggerFactory.getLogger(PictureRSSFileFilter.class);

    @Override
    public boolean accept(GenericFile pathname) {

        log.debug(pathname.toString());


        return pathname.getFileName().endsWith(".rss10");
    }

}