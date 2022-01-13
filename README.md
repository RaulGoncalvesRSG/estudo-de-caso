# Estudo de caso do diagrama UML

## Sobre o projeto
O sistema back-end permite o usuário realizar pedidos de produtos. Cada produto pertence a uma categoria. O pedido feito pelo cliente
possui endereço de entrega. Ao concluir sua compra, o cliente pode fazer o  pagamento por boleto ou por cartão. Caso escolha por cartão, 
pode ser definido a quantidade de parcelas para realizar o
pagamento. Ao fazer um pedido, o sistema envia uma mensagem para o cliente através do email do Google informando detalhes do pedido.

Caso o usuário esqueça sua senha, ele pode recuperá-la através de uma nova senha gerada de forma aleatória.

O usuário tem suas permissões de acesso a partir de seu perfil: Cliente ou ADMIN. É utilizado o JWT para a segurança de acesso 
aos endpoints. O sistema também possui o refresh token para gerar um novo token com tempo de expiração renovado.

O sistema permite que o usuário possa salvar uma imagem para o seu perfil. Essa imagem é guardada tanto no banco de dados quanto
no servidor da Amazon.

O projeto possui profiles: test (teste), dev (desenvolvimento) e prod (produção). 
O profile define configurações do projeto a serem executadas em determinado ambiente. Além disso, a aplicação possui tratamentos de
exceção de forma personalizada.

## Tecnologias utilizadas
* Java
* Spring Boot
* Spring Rest
* Spring Data JPA
* Spring Security/ JWT
* Banco de Dados H2
* MySQL
* SMTP do Google
* Amazon AWS

## Padrões utilizados no projeto
* Representational State Transfer (REST)
* Data Transfer Object (DTO)

## Diagrama UML
![Diagrama UML](imagens_sistema/diagrama.png)

## Estrutura de camadas do sistema
![Estrutura das camadas](imagens_sistema/estrutura_camadas.png)

## Autor
Raul Santos Gonçalves

[https://www.linkedin.com/in/raul-gonçalves-641310190/](https://www.linkedin.com/in/raul-gonçalves-641310190/)
