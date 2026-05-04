.PHONY: docker-build docker-shell docker-up docker-attach docker-rebuild-shell

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
