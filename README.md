# Estudo de caso do diagrama UML

## Sobre o projeto
O sistema back-end permite o usuário realizar pedidos de produtos. A arquitetura está dividida nas camadas resource (controller), service e reposotory. 
Ao fazer um pedido, o sistema envia uma mensagem para o cliente através do email do Google. 

O usuário tem suas permissões de acesso a partir de seu perfil: Cliente ou ADMIN.
O projeto possui profiles: test (teste), dev (desenvolvimento) e prod (produção). 
O profile define configurações do projeto ser executado em determinado ambiente.

## Tecnologias utilizadas
* Java
* Spring Boot
* Spring Rest
* Spring Data JPA
* Spring Security
* Banco de Dados H2
* MySQL
* SMTP do Google

## Diagrama UML
![Diagrama UML](imagens_sistema/diagrama.png)

## Estrutura das camadas do sistema
![Estrutura das camadas](imagens_sistema/estrutura_camadas.png)

## Autor
Raul Santos Gonçalves

[https://www.linkedin.com/in/raul-gonçalves-641310190/](https://www.linkedin.com/in/raul-gonçalves-641310190/)
