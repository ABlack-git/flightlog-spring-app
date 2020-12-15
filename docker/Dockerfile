FROM jenkins/jenkins:lts
USER root
# Add Backports repository
RUN echo 'deb http://ftp.debian.org/debian stretch-backports main' \
| tee /etc/apt/sources.list.d/stretch-backports.list
# Install Java 11
RUN apt-get update \
	&& apt-get install -y openjdk-11-jdk \
	&& rm -rf /var/lib/apt/lists/*
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt