App.ListsRoute = Ember.Route.extend({
   model: function() {
      return $.getJSON(API_NAMESPACE + 'users/' + App.uid + '/lists')
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
      return $.getJSON(API_NAMESPACE + 'users/' + App.uid + '/lists/' + params.list_id)
      .fail(function() {
         // TODO: mostrar en la pagina
         alert('Error en GET lists');
      })
      .done(function(list) {
         list.id = list.nombre;
         list.items = list.items.sortBy('votos').reverse();
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

// la ruta de login no necesita customizarse, no tiene modelo.
