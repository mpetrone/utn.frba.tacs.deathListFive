
</br>

## Instrucciones:

Para ejecutar el servidor utilizar el comando 'mvn jetty:run-war'

---

</br>

## Endpoints:

* **`/deathlist/users/{uid}`**
  
        POST: createUser: Crea un nuevo usuario **
        
            Example request body:
                {
                    "nombre": "john snow",
                }
    
        GET: getUser: Obtiene la informacion de un determinado usuario
    
        DELETE: deleteUser: Elimina un usuario

* **`/deathlist/users/{uid}/lists`**

        GET: getAllLists: Obtiene todas las listas de un determinado usuario

* **`/deathlist/users/{uid}/lists/{listName}`**

        GET: getList: Obtiene una lista de un determinado usuario
	      
        POST: createList: Crea una nueva lista
	      
        DELETE: deleteList: Elimina una lista de un determinado usuario

* **`/deathlist/users/{uid}/lists/{listName}/items/{itemName}`**
	
        POST: createItem: Crea un nuevo item
	
        DELETE: deleteItem: Elimina un item
	
* **`/deathlist/users/{uid}/lists/{listName}/items/{itemName}/vote`**

        POST: voteItem: Vota item
	
* **`/`**
	
        GET: Retorna la página principal de la aplicación
 