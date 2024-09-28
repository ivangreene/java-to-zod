.PHONY: *

build:
	./mvnw clean package

format:
	./mvnw spotless:apply

test:
	./mvnw clean test

clean:
	./mvnw clean
