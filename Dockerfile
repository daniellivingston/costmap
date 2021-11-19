# IMPORTANT NOTE:
#   OpenJFX, Gradle, and ARM-based architectures
#   do not play nice. When building, specify --platform=linux/arm64.
FROM ubuntu:20.04

ENV GRADLE_VERSION 6.4.1
ENV JDK_VERSION openjdk-11-jdk

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    DEBIAN_FRONTEND=noninteractive \
    DEBCONF_NONINTERACTIVE_SEEN=true \
    https_proxy=$https_proxy \
    http_proxy=$http_proxy

RUN apt-get update --fix-missing && \
    apt-get install --no-install-recommends -y \
        build-essential \
        apt-utils \
        ${JDK_VERSION} \
        ssh-client \
        wget \
        vim \
        tar \
        zip \
        unzip \
        curl \
        git && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* /*.deb

RUN set -o errexit -o nounset \
    && echo "Downloading Gradle" \
    && wget --no-verbose --output-document=gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
    && echo "Installing Gradle" \
    && unzip -d /opt/gradle gradle.zip \
    && rm gradle.zip \
    && touch /etc/profile.d/gradle.sh \
    && echo "export GRADLE_HOME=/opt/gradle/gradle-${GRADLE_VERSION}" >> /etc/profile.d/gradle.sh \
    && echo 'export PATH=${GRADLE_HOME}/bin:${PATH}' >> /etc/profile.d/gradle.sh
RUN . /etc/profile.d/gradle.sh

# Test that Java is working
RUN echo "Testing Java, Gradle, and OpenJFX building" && \
    java -version && gradle -version && \
    mkdir -p /java/dev/test/ && \
    cd /java/dev/test/ && \
    git clone https://github.com/openjfx/samples.git && \
    cd ./samples/CommandLine/Non-modular/Gradle/hellofx && \
    ./gradlew jar
    #java -jar build/libs/hellofx.jar

WORKDIR /java/dev/

CMD ["/bin/bash"]
