(function(){
  
  app.factory('b2cNameMaker', function(){
    var svc = {};
    
    var lastUsed = 0;
    
    svc.getUniqueName = function(){
      lastUsed++;
      
      return 'Some Gun #' + lastUsed;
    };
    
    return svc;
  });
  
}());