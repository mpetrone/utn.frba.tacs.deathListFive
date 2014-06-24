// donde vive la api
var API_NAMESPACE = '/deathlist/';

App = Ember.Application.createWithMixins(Em.Facebook, {
   authenticated: false,
   serverLoading: false,
   fbUserChanged: function() {
      var self = this;
      var FBUser = this.get('FBUser');

      if (FBUser) {
         document.cookie = 'token=' + FBUser.accessToken;
         document.cookie = 'uid=' + FBUser.id;

         this.set('serverLoading', true);

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
         }).always(function () {
            self.set('serverLoading', false);
         });
      } else {
         this.set('authenticated', false);
         // expirar las cookies
         document.cookie = 'token=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
         document.cookie = 'uid=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
      }
   }.observes('FBUser'),
   spinner: new Spinner({
     lines: 9, // The number of lines to draw
     length: 4, // The length of each line
     width: 2, // The line thickness
     radius: 3, // The radius of the inner circle
     corners: 1, // Corner roundness (0..1)
     rotate: 0, // The rotation offset
     direction: 1, // 1: clockwise, -1: counterclockwise
     color: '#EBEBEB', // #rgb or #rrggbb or array of colors
     speed: 1, // Rounds per second
     trail: 60, // Afterglow percentage
     shadow: true, // Whether to render a shadow
     hwaccel: false, // Whether to use hardware acceleration
     className: 'spinner', // The CSS class to assign to the spinner
     zIndex: 2e9, // The z-index (defaults to 2000000000)
     top: '50%', // Top position relative to parent
     left: '-5%' // Left position relative to parent
   })
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
