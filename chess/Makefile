.PHONY: docker-build docker-shell docker-up docker-attach docker-rebuild-shell docker-test docker-test-info docker-clean docker-boot-run docker-check

docker-build:
	DOCKER_BUILDKIT=1 COMPOSE_DOCKER_CLI_BUILD=1 docker compose build app

docker-shell:
	docker compose run --rm -it --service-ports app

docker-up:
	docker compose up

docker-attach:
	docker compose attach app

docker-rebuild-shell: docker-build
	docker compose run --rm -it --service-ports app

docker-test:
	docker compose run --rm gradle ./gradlew test

docker-test-info:
	docker compose run --rm gradle ./gradlew test --rerun-tasks --console=plain

docker-clean:
	docker compose run --rm gradle ./gradlew clean

docker-boot-run:
	docker compose run --rm --service-ports gradle ./gradlew bootRun

docker-check:
	docker compose run --rm gradle ./gradlew check
