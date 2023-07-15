package com.hevo.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class FileSearchServiceApplication extends Application<FileSearchServiceConfiguration> {
    public static void main(final String[] args) throws Exception {
        new FileSearchServiceApplication().run(args);
    }

    @Override
    public void run(FileSearchServiceConfiguration configuration, Environment environment) throws Exception {
    }

    @Override
    public void initialize(Bootstrap<FileSearchServiceConfiguration> bootstrap) {
        GuiceBundle guiceBundle =
                GuiceBundle.builder()
                        .enableAutoConfig(getClass().getPackage().getName())
                        .modules(new FileSearchServiceModule(bootstrap.getObjectMapper()))
                        .build();
        bootstrap.addBundle(guiceBundle);
    }
}
