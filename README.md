<a name="readme-top"></a>

[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]

<div style="text-align:center">

# Spring-boot-jwt-protected-vue-example
A basic fullstack example of how to protect a REST api with JWT based on MariaDB, SpringBoot and VueJS.

## Build with 
[![MariaDB][MariaDB-shield]][MariaDB-url]
[![Java][Java-shield]][Java-url]
[![SpringBoot][SpringBoot-shield]][SpringBoot-url]
[![Typescript][Typescript-shield]][Typescript-url]
[![Vue][Vue-shield]][Vue-url]
[![Boostrap][Bootstrap-shield]][Bootstrap-url]
[![Cypress][Cypress-shield]][Cypress-url]
[![NPM][NPM-shield]][NPM-url]
[![Docker][Docker-shield]][Docker-url]

</div>

## About the project

Whenever I want to create quickly a new fullstack project with Java and Vue I stumbled upon the issue that I have to create it entirely from scratch. This project tries to solve this issue once for all by providing a base project ready to run out of the box. Among other things it includes:

* preconfigured MariaDB
* REST API provided by a Spring Boot backend secured with JWT authentication
* Vue frontend
* basic examples

You can use this project as a template, for inspiration or anything else.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Getting started
### Prerequisites
* Docker
* Java 20
* npm

### Project structure

The project is separated into three parts:
#### 1. backend
The entire SpringBoot application is located in this folder. It's build with Gradle.

#### 2. frontend
The entire VueJS single page application is located in this folder. It's build with Vite.

#### 3. docker
For each part there exists at least one separate Dockerfile. 
The MariaDB is run as a Docker container.

For running the full stack, it's recommended to use `docker-compose`.

### Setup
#### Create certificates for encryption
In order to make JWT tokens secure there are some custom certificates in `./backend/src/main/ressources/cert`

> :information_source: Windows user running the commands below in their ``gitbash`` terminal or an other shell.

1. create rsa key pair
```sh
openssl genrsa -out keypair.pem 2048
```
2. extract public key
```sh
openssl rsa -in keypair.pem -pubout -out public.pem
```
3. create private key in PKCS#8 format
```sh
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem 
```

At the end you should have three files located in `./backend/src/main/ressources/cert`:
* ``keypair.pem``
* ``privata.pem``
* ``public.pem``

### Database

To start the database go to `/docker` and run
```sh
docker-compose up database
```

This will start the MariaDB database including creating the `example` database if it not already exists (see `/docker/database/__a__init.sql`)
To change port and initial user & password have a look at ``ENV`` in `docker/docker-compose.yml`

|              |                |
|--------------|----------------|
| **url**      | localhost:3306 |
| **database** | example        |
| **user**     | example        |
| **password** | example        |

## Licence
Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Contact
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- MARKDOWN VARIABLES -->
[forks-shield]: https://img.shields.io/github/forks/wprogLK/spring-boot-jwt-protected-vue-example.svg?style=for-the-badge
[forks-url]: https://github.com/wprogLK/spring-boot-jwt-protected-vue-example/network/members
[stars-shield]: https://img.shields.io/github/stars/wprogLK/spring-boot-jwt-protected-vue-example.svg?style=for-the-badge
[stars-url]: https://github.com/wprogLK/spring-boot-jwt-protected-vue-example/stargazers
[issues-shield]: https://img.shields.io/github/issues/wprogLK/spring-boot-jwt-protected-vue-example.svg?style=for-the-badge
[issues-url]: https://github.com/wprogLK/spring-boot-jwt-protected-vue-example/issues
[license-shield]: https://img.shields.io/github/license/wprogLK/spring-boot-jwt-protected-vue-example.svg?style=for-the-badge
[license-url]: https://github.com/wprogLK/spring-boot-jwt-protected-vue-example/blob/main/LICENSE.txt

[SpringBoot-shield]: https://img.shields.io/badge/SpringBoot-000000?style=for-the-badge&logo=spring-boot
[SpringBoot-url]: https://spring.io/
[MariaDB-shield]: https://img.shields.io/badge/MariaDb-000000?style=for-the-badge&logo=mariadb
[MariaDB-url]: https://mariadb.org/
[Vue-shield]: https://img.shields.io/badge/Vue%203-000000?style=for-the-badge&logo=vuedotjs
[Vue-url]: https://vuejs.org/
[Cypress-shield]: https://img.shields.io/badge/Cypress-000000?style=for-the-badge&logo=cypress
[Cypress-url]: https://www.cypress.io/
[Bootstrap-shield]: https://img.shields.io/badge/Bootstrap-000000?style=for-the-badge&logo=bootstrap
[Bootstrap-url]: https://getbootstrap.com/
[NPM-shield]: https://img.shields.io/badge/NPM-000000?style=for-the-badge&logo=npm
[NPM-url]: https://www.npmjs.com/
[Typescript-shield]: https://img.shields.io/badge/Typescript-000000?style=for-the-badge&logo=typescript
[Typescript-url]: https://www.typescriptlang.org/
[Java-shield]: https://img.shields.io/badge/Java-000000?style=for-the-badge&logo=openjdk
[Java-url]: https://www.java.com/
[Docker-shield]: https://img.shields.io/badge/Docker-000000?style=for-the-badge&logo=docker
[Docker-url]: https://docker.com

[linkedin-shield]:https://img.shields.io/badge/Lukas%20Adrian%20Keller%20on%20LinkedIn-0077B5?&logo=linkedin&logoColor=white
[linkedin-url]: https://www.linkedin.com/in/wproglk/




