.PHONY: build
build:
	mvn clean package

.PHONY: format
format:
	mvn spotless:apply

.PHONY: test
test:
	mvn clean test
