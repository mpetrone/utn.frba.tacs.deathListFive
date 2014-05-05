// donde vive la api
var API_NAMESPACE = 'deathlist/';

// usuario de prueba, se va cuando haya FB
var GUEST_PATH = 'users/guest';
$.ajax({
   type: 'POST',
   url: API_NAMESPACE + GUEST_PATH,
   contentType: "application/json; charset=utf-8",
   data: JSON.stringify({uid:1234, token:'token'})
}).always(function() {

   App = Ember.Application.create({
      LOG_TRANSITIONS: true
   });

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
               return lists.forEach(function (list) {
                  list.id = list.nombre;
               });
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
               list.items.forEach(function (item) {
                  item.id = item.nombre;
               });
               return list;
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
                  controller.get('content').unshiftObject({
                     id: list,
                     nombre: list
                  });
               });
         },
         deleteList: function (list) {
            // TODO: confirmation con bootstrap
            // TODO: error handling
            
            var controller = this;
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
                  
                  // actualiza la los items
                  controller.get('content').items.unshiftObject({
                     id: item,
                     nombre: item,
                     votos: 0
                  });
               });
         },
         deleteItem: function (item) {
            // TODO: confirmation con bootstrap
            // TODO: error handling
            
            var list = this.get('id');
            
            var controller = this;
            $.ajax({
               url: API_NAMESPACE + GUEST_PATH + '/lists/' + list + '/items/' + item.id,
               type: 'DELETE',
               dataType: 'html' // la respuesta viene vacia, eso no le gusta al parser de json
            }).done(function () {
               // actualiza la lista
               var items = controller.get('content.items');
               
               controller.set('content.items', items.rejectBy('id', item.id));
            });
            
         },
         voteItem: function (item) {
             // TODO: error handling
	         
        	 var controller = this;
        	 var list = this.get('id');
      	 
        	 $.post(API_NAMESPACE + GUEST_PATH + '/lists/' + list + '/items/' + item.id + '/vote')     	 
             .done(function () {
                 // actualizar los votos      
            	 Ember.set(item,'votos',item.votos +1);
            	 //controller.incrementProperty('votos');
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

