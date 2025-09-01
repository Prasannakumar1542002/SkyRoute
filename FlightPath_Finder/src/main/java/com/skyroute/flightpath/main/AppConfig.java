package com.skyroute.flightpath.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.skyroute.flightpath.database.DBManager;
import com.skyroute.flightpath.feed.FeedImportServices;


@Configuration
public class AppConfig {

    @Bean
    FeedImportServices feedImportServices() {
        return FeedImportServices.build();
    }
}
