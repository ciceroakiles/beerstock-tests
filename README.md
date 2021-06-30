https://digitalinnovation.one/bootcamps/gft-start-2-java

Projeto base com testes unitários para validar uma API REST de gerenciamento de estoques de cerveja.

Testes unitários:

* CervejaServiceTest.java

	* Testado o processo necessário para o POST e o lançamento de exceção (em caso de elemento duplicado)

	* Testado o retorno após busca por nome via GET (nome encontrado ou não)

	* Testado o retorno de lista (com e sem objetos)

	* Testado o processo de deleção

* CervejaControllerTest.java

	* Retorno de JSON após o POST (sucesso) ou erro (caso um dos campos esteja vazio)

	* Retorno de JSON após o GET (sucesso) ou erro (caso não seja encontrado)

	* Retorno de lista após o GET (vazia ou não)

	* Retorno de código HTTP 204 ou HTTP 404 após o DELETE
