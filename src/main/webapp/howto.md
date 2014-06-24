
</br>

## Instrucciones:

Para ejecutar la aplicación localmente utilizar el comando 'mvn appengine:devserver' 

La aplicación se encuentra online en https://utn-tacs.appspot.com o también se puede acceder directamente desde facebook en https://apps.facebook.com/utn_deathlist/

---

</br>

## Endpoints:

Aclaracion: para todos los endpoints se debera utilizar un token valido de facebook.

	
* **`/`**
	
        GET: Retorna la página principal de la aplicación

* **`/deathlist/users/{uid}`**
  
        GET: Recupera la información del usuario 'uid'.
            Produces: application/json 
 
        POST: Crea el usuario 'uid'
            Consumes: application/json
            
        DELETE: Elimina el usuario 'uid'
           
        Ejemplo: /deathlist/users/1015425246234553/
  
* **`/deathlist/users/{uid}/login`**
  
        POST: Loguea el usuario 'uid', creandolo si no existe
            Produces: application/json
        
        Ejemplo: /deathlist/users/1015425246234553/login
  
* **`/deathlist/users/{uid}/friends`**
  
        GET: Recupera la lista de amigos del usuario 'uid'
            Produces: application/json
        
        Ejemplo: /deathlist/users/1015425246234553/friends        
        
* **`/deathlist/users/{uid}/lists`**

        GET: Obtiene todas las listas del usuario 'uid'
            Produces: application/json
        
        Ejemplo: /deathlist/users/1015425246234553/lists
        
* **`/deathlist/users/{uid}/lists/{listName}`**

        GET: Obtiene la lista 'listName' del usuario 'uid'
            Produces: application/json
            
        POST: Crea una nueva lista 'listName' en el usuario 'uid' 
            
        DELETE: Elimina la lista 'listName' del usuario 'uid'
        
        Ejemplo: /deathlist/users/1015425246234553/lists/materias
        
* **`/deathlist/users/{uid}/lists/{listName}/items/{itemName}`**
	
        POST: Crea un nuevo item 'itemName' en la lista 'listName' del usuario 'uid'
	
        DELETE: Elimina el item 'itemName' de la lista 'listName' del usuario 'uid'
	
        Ejemplo: /deathlist/users/1015425246234553/lists/materias/items/tacs
        
* **`/deathlist/users/{uid}/lists/{listName}/items/{itemName}/vote`**

        POST: Vota el item 'itemName' de la lista 'listName' del usuario 'uid'
        
        Ejemplo: /deathlist/users/1015425246234553/lists/materias/items/tacs/vote
