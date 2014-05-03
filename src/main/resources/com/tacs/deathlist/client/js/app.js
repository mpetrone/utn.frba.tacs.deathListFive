// donde vive la api
var API_NAMESPACE = 'deathlist/';
var GUEST_PATH = 'users/guest';

// usuario de prueba, se va cuando haya FB
$.ajax({
   type: 'POST',
   url: API_NAMESPACE + GUEST_PATH,
   contentType: "application/json; charset=utf-8",
   data: JSON.stringify({uid:0, token:'token'})
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
            .done(function(data) {
               return data.map(function(list){
                  list.id = list.nombre;
                  return list;
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
            .done(function(data) {
               data.id = data.nombre;
               return data;
            });
      }
   });
      
   App.ListsController = Ember.ArrayController.extend({
      actions: {
         createList: function() {
            // TODO: error handling
            
            // valor del input
            var list = this.get('newList');
            if (!list.trim()) { return; }
            
            var controller = this;
            return $.post(API_NAMESPACE + GUEST_PATH + '/lists/' + list)
               .done(function () {
                  // limpia el input
                  controller.set('newList', null);
                  
                  // actualiza la lista
                  controller.get('content').unshiftObject({
                     id: list,
                     nombre: list
                  });
               });
         }
      }
   });

});


