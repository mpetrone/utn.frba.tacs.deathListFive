// administra el proceso de autenticacion
App.AuthenticatedRoute = Ember.Route.extend({

  beforeModel: function(transition) {
    if (!App.get('authenticated')) {
      this.redirectToLogin(transition);
    }
  },

  redirectToLogin: function(transition) {
    App.set('attemptedTransition', transition);
    this.transitionTo('login');
  },
});

App.PeopleRoute = App.AuthenticatedRoute.extend({
   model: function() {
      return $.getJSON(API_NAMESPACE + 'users/' + App.FBUser.id + '/friends')
      .fail(function() {
         // TODO: mostrar en la pagina
         alert('Error en GET friends');
      })
      .done(function(friends) {
         for (var i = 0; i < friends.length; i++) {
            friends[i] = Ember.Object.create({
               id: friends[i].uid,
               uid: friends[i].uid,
               nombre: friends[i].nombre,
            });
         }
         
         friends.unshift(Ember.Object.create({
            id: App.FBUser.id,
            uid: App.FBUser.id,
            nombre: 'me',
         }));
      });
   }
});

App.ListsRoute = App.AuthenticatedRoute.extend({
   model: function(params) {
      return $.getJSON(API_NAMESPACE + 'users/' + params.user_id + '/lists')
      .fail(function() {
         // TODO: mostrar en la pagina
         alert('Error en GET lists');
      })
      .done(function(lists) {
         for (var i = 0; i < lists.length; i++) {
            lists[i] = Ember.Object.create({
               id: lists[i],
               uid: params.user_id,
            });
         }
      });
   }
});

App.ListRoute = App.AuthenticatedRoute.extend({
   model: function(params) {
      var uid = this.modelFor('lists').findBy('id', params.list_id).uid;
      
      return $.getJSON(API_NAMESPACE + 'users/' + uid + '/lists/' + params.list_id)
      .fail(function() {
         // TODO: mostrar en la pagina
         alert('Error en GET lists');
      })
      .done(function(list) {
         list.id = list.nombre;
         list.items = list.items.sortBy('votos').reverse();
         list.uid = uid;
         
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

