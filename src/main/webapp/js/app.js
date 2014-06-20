// donde vive la api
var API_NAMESPACE = '/deathlist/';

App = Ember.Application.createWithMixins(Em.Facebook, {
   authenticated: false,
   fbUserChanged: function() {
      var self = this;
      var FBUser = this.get('FBUser');

      if (FBUser) {
         document.cookie = 'token=' + FBUser.accessToken;
         document.cookie = 'uid=' + FBUser.id;

         $.ajax({
            type: 'POST',
            url: API_NAMESPACE + 'users/' + FBUser.id + '/login',
            contentType: "application/json; charset=utf-8"
         }).done(function() {
            self.set('authenticated', true);

            var attemptedTransition = self.get('attemptedTransition');
            
            if (attemptedTransition) {
               attemptedTransition.retry();
               self.set('attemptedTransition', null);
            } else {
               window.location.hash='/people';
            }
         });
      } else {
         this.set('authenticated', false);
         // expirar las cookies
         document.cookie = 'token=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
         document.cookie = 'uid=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
      }
   }.observes('FBUser'),
});


App.set('appId', '1507322809490755');

App.Router.map(function() {
   this.resource('login', { path: "/" });
   this.resource('people', function () {
      this.resource('lists', { path: ':user_id' }, function () {
         this.resource('list', { path: ':list_id' });         
      });
   });
   this.resource('about');
});

// configuracion de showdown para el about
//
var showdown = new Showdown.converter();

Ember.Handlebars.helper('markdown', function(data) {
   if (data) {
      return new Handlebars.SafeString(showdown.makeHtml(data));
   }
});
