(function(){
  
  app.factory('b2cStorageService', function(){
    var svc = {};
    
    var cache = {};
    
    svc.isAvailable = false;
    
    try {
      svc.isAvailable = 'localStorage' in window && window.localStorage !== null;
    } catch(e) {
      svc.isAvailable = false;
    }
    
    svc.save = function(key, item){
      var stringItem = JSON.stringify(item);
      
      if(svc.isAvailable){
        localStorage.setItem(key, stringItem);
      } else {
        cache[key] = stringItem;
      }
    };
    
    svc.get = function(key, defaultResult){
      var result = svc.isAvailable ? localStorage.getItem(key) : cache[key];
      
      try{
        return result ? JSON.parse(result) : defaultResult;
      } catch(e) {
        if(svc.isAvailable){
          localStorage.removeItem(key);
        } else {
          delete cache[key];
        }
        
        return defaultResult;
      }
    };
    
    return svc;
  });

}());
