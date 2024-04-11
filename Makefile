.PHONY: *

build:
	mvn clean package

format:
	mvn spotless:apply

test:
	mvn clean test

clean:
	mvn clean
