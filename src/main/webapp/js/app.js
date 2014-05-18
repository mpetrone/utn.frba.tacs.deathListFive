// donde vive la api
var API_NAMESPACE = 'http://localhost:8080/deathlist/';

// usuario de prueba, se va cuando haya FB
var GUEST_PATH = 'users/guest';
$.ajax({
   type: 'POST',
   url: API_NAMESPACE + GUEST_PATH,
   contentType: "application/json; charset=utf-8",
   data: JSON.stringify({uid:1234, token:'token'})
}).always(function() {

   App = Ember.Application.create();

   App.Router.map(function() {
      this.resource('lists', { path: '/' }, function () {
         this.resource('list', { path: ':list_id' });         
      });
      this.resource('about');
   });

   App.ListsRoute = Ember.Route.extend({
      model: function() {
         return $.getJSON(API_NAMESPACE + GUEST_PATH + '/lists')
            .fail(function() {
               // TODO: mostrar en la pagina
               alert('Error en GET lists');
            })
            .done(function(lists) {
               for (var i = 0; i < lists.length; i++) {
                  lists[i] = Ember.Object.create({
                     id: lists[i]
                  });
               }
            });
      }
   });
   
   App.ListRoute = Ember.Route.extend({
      model: function(params) {
         return $.getJSON(API_NAMESPACE + GUEST_PATH + '/lists/' + params.list_id)
            .fail(function() {
               // TODO: mostrar en la pagina
               alert('Error en GET lists');
            })
            .done(function(list) {
               list.id = list.nombre;
               for (var i = 0; i < list.items.length; i++) {
                  list.items[i] = Ember.Object.create({
                     id:    list.items[i].nombre,
                     votos: list.items[i].votos
                  })
               }
            });
      }
   });
   
   App.AboutRoute = Ember.Route.extend({
      model: function () {
         return $.get('/howto.md');
      }
   })
      
   App.ListsController = Ember.ArrayController.extend({
      actions: {
         createList: function() {
            // TODO: error handling
            
            // valor del input
            var list = this.get('newList');
            if (!$.trim(list)) { return; }
            
            var controller = this;
            $.post(API_NAMESPACE + GUEST_PATH + '/lists/' + list)
               .done(function () {
                  // limpia el input
                  controller.set('newList', '');
                  
                  // actualiza la lista
                  controller.get('content').unshiftObject(
                     Ember.Object.create({
                        id: list
                     })
                  );
               });
         },
         deleteList: function (list) {
            // TODO: error handling
             var controller = this;            

             BootstrapDialog.show({
                 type: BootstrapDialog.TYPE_WARNING,
                 title: 'Advertencia',
                 message: '¿Querés borrar la lista ' + list.id +'?',
                 buttons: [{
                     label: 'Sí',
                     action: function(dialogItself){
                    	 
                         $.ajax({
                             url: API_NAMESPACE + GUEST_PATH + '/lists/' + list.id,
                             type: 'DELETE',
                             dataType: 'html' // la respuesta viene vacia, eso no le gusta al parser de json
                          }).done(function () {
                             var content = controller.get('content');                             
                             controller.set('content', content.rejectBy('id', list.id));
                             
                             // si la ruta apuntaba a la lista borrada, ir a lists
                             //
                             var list_infos = App.Router.router.currentHandlerInfos.findBy('name', 'list');                             
                             if (list_infos && list_infos.context.id == list.id)
                             {
                                controller.transitionToRoute('lists');                  
                             }          
                             
                             dialogItself.close();
                          });  
                     	}
                     },
                     {
                         label: 'No',
                         action: function(dialogItself){
                             dialogItself.close();
                         }
                 }]
             }); 
         }
      }
   });
   
   App.ListController = Ember.ObjectController.extend({
      actions: {
         createItem: function() {
            // TODO: error handling
            
            var list = this.get('id');
            
            // valor del input
            var item = this.get('newItem');
            if (!$.trim(item)) { return; }
            
            var controller = this;
            $.post(API_NAMESPACE + GUEST_PATH + '/lists/' + list + '/items/' + item)
               .done(function () {
                  // limpia el input
                  controller.set('newItem', '');
                  
                  // actualiza los items
                  controller.get('content').items.unshiftObject(
                     Ember.Object.create({
                        id: item,
                        votos: 0
                     })
                  );
               });
         },
         deleteItem: function (item) {
            // TODO: error handling
            
            var list = this.get('id');
            
            var controller = this;

            BootstrapDialog.show({
                type: BootstrapDialog.TYPE_WARNING,
                title: 'Advertencia',
                message: '¿Querés borrar el item ' + item.id +'?',
                buttons: [{
                    label: 'Sí',
                    action: function(dialogItself){                        
                    	$.ajax({
                    		url: API_NAMESPACE + GUEST_PATH + '/lists/' + list + '/items/' + item.id,
                    		type: 'DELETE',
                    		dataType: 'html' // la respuesta viene vacia, eso no le gusta al parser de json
                    			}).done(function () {
                    				// actualiza la lista
                    				var items = controller.get('content.items');
                    				controller.set('content.items', items.rejectBy('id', item.id));
                    				});
                    	dialogItself.close();
                    	},
                },
                {
                	label: 'No',
                	action: function(dialogItself){
                		dialogItself.close();
                		}
                }]
            });                         
         },
         voteItem: function (item) {
             // TODO: error handling
	         
        	 var controller = this;
        	 var list = this.get('id');
      	 
        	 $.post(API_NAMESPACE + GUEST_PATH + '/lists/' + list + '/items/' + item.id + '/vote')     	 
             .done(function () {
                item.incrementProperty('votos');
              });
         }
      }
   });
   
   var showdown = new Showdown.converter();

   Ember.Handlebars.helper('markdown', function(data) {
      if (data)
      {
         return new Handlebars.SafeString(showdown.makeHtml(data));
      }
   });

});

