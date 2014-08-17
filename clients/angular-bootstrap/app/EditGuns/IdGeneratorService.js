(function(){

  app.factory('b2cIdGenerator', function(){
    var svc = {};
    
    var lastId = 0;
    
    svc.getNextId = function(){
      return ++lastId;
    };
    
    svc.registerId = function(id){
      if(id > lastId){
        lastId = id;
      }
    };
    
    return svc;
  });

}());
