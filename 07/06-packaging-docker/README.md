# Spring Boot 3 Fundamentals

These examples and exercises are part of the course "Spring Boot 3 Fundamentals" by Jesper de Jong at [Pluralsight](https://www.pluralsight.com/).

## Packaging and Deploying

In this module, you've learned how to package a Spring Boot application into a Docker image in different ways.

You can do this by writing a `Dockerfile` yourself and building the image with the `docker build` command.

The Spring Boot Maven plugin can build a Docker image for you by using [Paketo Buildpacks](https://paketo.io).
When you do it like this, you do not need to write a `Dockerfile`.

The command to build a Docker image with the Spring Boot Maven plugin is the following:

On Windows:

    mvnw.cmd spring-boot:build-image

On macOS or Linux:

    ./mvnw spring-boot:build-image

Note: At this moment, Paketo Buildpacks does not support ARM64 and Apple Silicon yet. This might not work if you run this on an Apple computer
with an Apple Silicon CPU (M1, M2, etc.).

It is also possible to build a [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/). This can make your application start up
and run faster and more efficiently, but there are limitations and issues that you need to know about.

See the chapter about [GraalVM Native Image Support](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image)
in the Spring Boot reference documentation and the page about
[Spring Boot with GraalVM](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-with-GraalVM) on the Spring Boot wiki at GitHub.

You can build a native image by setting the `native` Maven profile:

On Windows:

    mvnw.cmd -Pnative spring-boot:build-image

On macOS or Linux:

    ./mvnw -Pnative spring-boot:build-image
