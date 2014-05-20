// controller para la vista de listas
App.ListsController = Ember.ArrayController.extend({
   actions: {
      createList: function() {
         // TODO: error handling
         
         // valor del input
         var list = this.get('newList');
         if (!$.trim(list)) { return; }
         
         var controller = this;
         $.post(API_NAMESPACE + 'users/' + App.uid + '/lists/' + list)
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
                     url: API_NAMESPACE + 'users/' + App.uid + '/lists/' + list.id,
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

// controller para la vista de una sola lista
App.ListController = Ember.ObjectController.extend({
   actions: {
      createItem: function() {
         // TODO: error handling
         
         var list = this.get('id');
         
         // valor del input
         var item = this.get('newItem');
         if (!$.trim(item)) { return; }
         
         var controller = this;
         $.post(API_NAMESPACE + 'users/' + App.uid + '/lists/' + list + '/items/' + item)
         .done(function () {
            // limpia el input
            controller.set('newItem', '');
               
            // actualiza los items
            controller.get('content').items.pushObject(
               Ember.Object.create({
                  id: item,
                  votos: 0
               })
            ).sortBy('votos').reverse();
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
                     url: API_NAMESPACE + 'users/' + App.uid + '/lists/' + list + '/items/' + item.id,
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
       
         $.post(API_NAMESPACE + 'users/' + App.uid + '/lists/' + list + '/items/' + item.id + '/vote')         
         .done(function () {
            item.incrementProperty('votos');
             
            var items = controller.get('content.items');
            controller.set('content.items', items.sortBy('votos').reverse());                
           
         });
      }
   }
});