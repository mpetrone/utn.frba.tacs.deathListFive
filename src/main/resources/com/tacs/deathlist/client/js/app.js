// donde vive la api
var API_NAMESPACE = 'deathlist/';
var GUEST_PATH = 'users/guest';

// usuario de prueba, se va cuando haya FB
// $.ajax({
   //    type: 'POST',
   //    url: API_NAMESPACE + GUEST_PATH,
   //    contentType: "application/json; charset=utf-8",
   //    data: JSON.stringify({uid:0, token:'token'})
   //  }).always(function() {

      App = Ember.Application.create({
         LOG_TRANSITIONS: true
      });

      App.Router.map(function() {
         this.resource('lists', { path: '/'});
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

      // });


