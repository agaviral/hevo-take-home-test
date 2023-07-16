package com.hevo.services;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Injector;
import com.hevo.services.queue.S3ChangelogEvent;
import com.hevo.services.queue.S3ChangelogHandler;
import com.hevo.services.queue.SqsConsumer;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import ru.vyarus.dropwizard.guice.GuiceBundle;
import ru.vyarus.dropwizard.guice.module.installer.feature.ManagedInstaller;

public class FileSearchServiceApplication extends Application<FileSearchServiceConfiguration> {
    private GuiceBundle guiceBundle;

    public static void main(final String[] args) throws Exception {
        new FileSearchServiceApplication().run(args);
    }

    @Override
    public void run(FileSearchServiceConfiguration configuration, Environment environment) throws Exception {
        Injector injector = guiceBundle.getInjector();
        S3ChangelogHandler s3ChangelogHandler = injector.getInstance(S3ChangelogHandler.class);
        environment
                .lifecycle()
                .manage(new SqsConsumer<>(
                        configuration.getS3ChangelogConsumer(),
                        s3ChangelogHandler,
                        environment.getObjectMapper(),
                        S3ChangelogEvent.class));
    }

    @Override
    public void initialize(Bootstrap<FileSearchServiceConfiguration> bootstrap) {
        bootstrap.getObjectMapper().findAndRegisterModules();
        bootstrap.getObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        guiceBundle =
                GuiceBundle.builder()
                        .enableAutoConfig(getClass().getPackage().getName())
                        .modules(new FileSearchServiceModule(bootstrap.getObjectMapper()))
                        .disableInstallers(ManagedInstaller.class)
                        .build();
        bootstrap.addBundle(guiceBundle);
    }
}
