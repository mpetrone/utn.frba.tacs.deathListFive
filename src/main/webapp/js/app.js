// donde vive la api
var API_NAMESPACE = '/deathlist/';

// arranca el SDK de FB
window.fbAsyncInit = function() {
  FB.init({
    appId      : '1507322809490755',
    cookie     : true,  // enable cookies to allow the server to access 
                        // the session
    xfbml      : true,  // parse social plugins on this page
    version    : 'v2.0' // use version 2.0
  });
  
  checkLoginState();
};

App = Ember.Application.create();

App.Router.map(function() {
   this.resource('login', { path: '/' });
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
   if (data)
   {
      return new Handlebars.SafeString(showdown.makeHtml(data));
   }
});

function checkLoginState() {
   FB.getLoginStatus(function(response) {
      statusChangeCallback(response);
   });
}

function statusChangeCallback(response) {
   console.log('statusChangeCallback');
   console.log(response);
   
   if (response.status === 'connected') { // logueado en fb y en la app
      notifyServer(response.authResponse);
   } else if (response.status === 'not_authorized') { // logueado a fb, pero no en la app
      // TODO: handling
   } else { // no logueado a fb
      // TODO: handling
   }
}

function notifyServer(authResponse) {
   document.cookie = 'token=' + authResponse.accessToken;
   document.cookie = 'uid=' + authResponse.userID;
   
   App.uid = authResponse.userID;
   
   $.ajax({
      type: 'POST',
      url: API_NAMESPACE + 'users/' + authResponse.userID,
      contentType: "application/json; charset=utf-8",
      data: '{nombre: "user"}'
   }).always(function () {
      // TODO: error handling
      // FIXME: hack
      window.location.hash='/people';
   });
}
