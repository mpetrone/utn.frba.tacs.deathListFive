Instrucciones:
--------------

Para ejecutar el servidor utilizar el comando `mvn exec:java`

Endpoints:
----------

* /deathlist/users/{username}
	POST: createUser: Crea un nuevo usuario
	GET: getUser: Obtiene la informacion de un determinado usuario
	DELETE: deleteUser: Elimina un usuario

* /deathlist/users/{username}/lists
	GET: getAllLists: Obtiene todas las listas de un determinado usuario

* /deathlist/users/{username}/lists/{listName}
	GET: getList: Obtiene una lista de un determinado usuario
	POST: createList: Crea una nueva lista
	DELETE: deleteList: Elimina una lista de un determinado usuario

* /deathlist/users/{username}/lists/{listName}/items/{itemName}
	POST: createItem: Crea un nuevo item
	DELETE: deleteItem: Elimina un item
	
* /deathlist/users/{username}/lists/{listName}/items/{itemName}/vote
	POST: voteItem: Vota item
	
* /deathlist/front
	GET: Retorna la página principal de la aplicación
 