// controller para la vista de listas
App.ListsController = Ember.ObjectController.extend({
   canCreate: function() {
      return App.FBUser.id == this.get('model.uid');
   }.property('model.uid'),

   actions: {
      createList: function() {
         // valor del input
         var list = this.get('newList');
         if (!$.trim(list)) {
            return;
         }

         var controller = this;
         $.post(API_NAMESPACE + 'users/' + App.FBUser.id + '/lists/' + list)
         .fail(function() {
         		 BootstrapDialog.show({
         			 title: 'You cannot create this List.',
         			 message: 'List name already exists, try a different one.',
         			 type: BootstrapDialog.TYPE_INFO,
         			 buttons: [{
         				label: 'Close',
         				action: function(dialogRef){
         					dialogRef.close();
         				}
         			 }]
         		 }); 
         	 })
          .done(function() {
               // limpia el input
               controller.set('newList', '');

               // actualiza la lista
               controller.get('model.lists').unshiftObject(
                  Ember.Object.create({
                     id: list,
                     uid: App.FBUser.id
                  })
               );
               FB.api('me/feed', 'post', { message: 'Ha creado una lista ' + list });
            });
      },

      deleteList: function(list) {
         var controller = this;
         
         if (list.id){
         BootstrapDialog.show({
            type: BootstrapDialog.TYPE_WARNING,
            title: 'Warning',
            message: 'Do you really want to delete List "' + list.id + '"?',
            buttons: [{
               label: 'Yes',
               action: function(dialogItself) {

                  $.ajax({
                     url: API_NAMESPACE + 'users/' + App.FBUser.id + '/lists/' + list.id,
                     type: 'DELETE',
                     dataType: 'html' // la respuesta viene vacia, eso no le gusta al parser de json
                  }).done(function() {
                     var content = controller.get('model.lists');
                     controller.set('model.lists', content.rejectBy('id', list.id));

                     // si la ruta apuntaba a la lista borrada, ir a lists
                     //
                     var list_infos = App.Router.router.currentHandlerInfos.findBy('name', 'list');
                     if (list_infos && list_infos.context.id == list.id) {
                        controller.transitionToRoute('lists');
                     }

                     dialogItself.close();
                  });
               }
            }, {
               label: 'No',
               action: function(dialogItself) {
                  dialogItself.close();
               }
            }]
         });
         }
         else{
     		 BootstrapDialog.show({
     			 title: 'You cannot delete this List.',
     			 message: 'The List you are trying to delete does not exist.',
     			 type: BootstrapDialog.TYPE_WARNING,
     			 buttons: [{
     				label: 'Close',
     				action: function(dialogRef){
     					dialogRef.close();
     				}
     			 }]
     		 }); 
         };
      }
   }
});

// controller para la vista de una sola lista
App.ListController = Ember.ObjectController.extend({
   actions: {
      createItem: function() {
         var list = this.get('id');

         // valor del input
         var item = this.get('newItem');
         if (!$.trim(item)) {
            return;
         }

         var controller = this;

         $.post(API_NAMESPACE + 'users/' + controller.content.uid + '/lists/' + list + '/items/' + item)
         .fail(function() {
         		 BootstrapDialog.show({
         			 title: 'You cannot create this Item.',
         			 message: 'Maximum ammount of Items reached OR Item name already exists.',
         			 type: BootstrapDialog.TYPE_INFO,
         			 buttons: [{
         				label: 'Close',
         				action: function(dialogRef){
         					dialogRef.close();
         				}
         			 }]
         		 }); 
         })   
         .done(function() {
               // limpia el input
               controller.set('newItem', '');

               // actualiza los items
               controller.get('model.items').pushObject(
                  Ember.Object.create({
                     id: item,
                     votos: 0
                  })
               );

               var items = controller.get('model.items');
               controller.set('model.items', items.sortBy('votos').reverse());
            });
      },
      deleteItem: function(item) {
         var list = this.get('id');

         var controller = this;
        
         if(item.id){
         BootstrapDialog.show({
            type: BootstrapDialog.TYPE_WARNING,
            title: 'Warning',
            message: 'Do you really want to delete Item "' + item.id + '"?',
            buttons: [{
               label: 'Yes',
               action: function(dialogItself) {
                  $.ajax({
                     url: API_NAMESPACE + 'users/' + controller.get('model.uid') + '/lists/' + list + '/items/' + item.id,
                     type: 'DELETE',
                     dataType: 'html' // la respuesta viene vacia, eso no le gusta al parser de json
                  }).done(function() {
                     // actualiza la lista
                     var items = controller.get('model.items');
                     controller.set('model.items', items.rejectBy('id', item.id));
                  });
                  dialogItself.close();
               },
            }, {
               label: 'No',
               action: function(dialogItself) {
                  dialogItself.close();
               }
            }]
         });
      }
      else{
  		 BootstrapDialog.show({
 			 title: 'You cannot delete this Item.',
 			 message: 'The Item you are trying to delete does not exist.',
 			 type: BootstrapDialog.TYPE_WARNING,
 			 buttons: [{
 				label: 'Close',
 				action: function(dialogRef){
 					dialogRef.close();
 						}
 			 		}]
  		 		}); 
      	};
      },
      voteItem: function(item) {
         var controller = this;
         var list = this.get('id');

         $.post(API_NAMESPACE + 'users/' + controller.content.uid + '/lists/' + list + '/items/' + item.id + '/vote')
         .fail(function() {
         		 BootstrapDialog.show({
         			 title: 'You cannot vote this Item.',
         			 message: 'An unexpected error ocurred.',
         			 type: BootstrapDialog.TYPE_DANGER,
         			 buttons: [{
         				label: 'Close',
         				action: function(dialogRef){
         					dialogRef.close();
         				}
         			 }]
         		 }); 
         })     
         .done(function() {
               item.incrementProperty('votos');

               var items = controller.get('model.items');
               controller.set('model.items', items.sortBy('votos').reverse());

               FB.ui({
                  method: 'share_open_graph',
                  action_type: 'utn_deathlist:vote',
                  action_properties: JSON.stringify({
                     website: document.URL,
                  })
               });
            });
      },
      shareItems: function(item) {
    	  
          var controller = this;
          var list = this.get('id');
          var items = controller.get('model.items');
          
          try{
          FB.ui({
              method: 'share_open_graph',
              action_type: 'utn_deathlist:share',
              action_properties: JSON.stringify({
                 website: document.URL,
              })
           });
          }
          catch(err){
        	  BootstrapDialog.show({
      			 title: 'You cannot share this Item.',
      			 message: 'An unexpected error ocurred: ' + err.message,
      			 type: BootstrapDialog.TYPE_DANGER,
      			 buttons: [{
      				label: 'Close',
      				action: function(dialogRef){
      					dialogRef.close();
      				}
      			 }]
      		 });
          }
       }
   }
});
