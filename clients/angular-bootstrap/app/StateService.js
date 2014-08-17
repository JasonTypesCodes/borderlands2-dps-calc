(function(){
  
  app.factory('b2cStateService', function($log, b2cGunService){
    var svc = {};
    
    var myGuns = [
      {type: 'Pistol', guns: []},
      {type: 'Assault Rifle', guns: []},
      {type: 'Shotgun', guns: []},
      {type: 'Sniper Rifle', guns: []},
      {type: 'Submachine', guns: []},
      {type: 'Rocket Launcher', guns: []}
    ];
    
    var types = [];
    var activeType = 'Pistol';
    var requiredInputs = [];
    var defaultValues = [];
    var defaultsLoaded = false;
      
    var dpsDisplaySettings = {
      includeReload: true,
      includeAccuracy: true,
      includeElemental: true
    };
    
    svc.getTypes = function(){
      return types;
    };
    
    svc.getActiveType = function(){
      return activeType;
    };
    
    svc.setActiveType = function(type){
      activeType = type;
    };
    
    svc.getGunsForType = function(type){
      for(var x = 0; x < myGuns.length; x++){
        if(myGuns[x].type === type){
          return myGuns[x].guns;
        }
      }
      
      return [];
    };
    
    svc.getGun = function(id){
      id = parseInt(id, 10);
      for(var x = 0; x < myGuns.length; x++){
        for(var y = 0; y < myGuns[x].guns.length; y++){
          if(myGuns[x].guns[y].id === id){
            return myGuns[x].guns[y];
          }
        }
      }
      
      return {id: id};
    };
    
    svc.saveGun = function(newGun){
      var gunsForType = svc.getGunsForType(newGun.type);
      var found = false;
      for(var x = 0; x < gunsForType.length; x++){
        if(gunsForType[x].id === newGun.id){
          gunsForType[x] = newGun;
          found = true;
        }
      }
      
      if(!found){
        gunsForType.push(newGun); 
      }
    };
    
    svc.removeGun = function(id){
      for(var x = 0; x < myGuns.length; x++){     
        for(var y = 0; y < myGuns[x].guns.length; y++){
          if(myGuns[x].guns[y].id === id){
            myGuns[x].guns.splice(y,1);
          }
        }
      }
    };
    
    svc.getDisplayableDPS = function(someGun){
      if(someGun && someGun.results){
        return someGun.results.withReloadAndElementalAndAccuracy;    
      } else {
        return -1;
      }
    };
    
    svc.inputRequired = function(inputName){
      return requiredInputs.indexOf(inputName) >= 0;
    };
    
    svc.inputDefaultValue = function(inputName){
      for(var x = 0; x < defaultValues.length; x++){
        if(defaultValues[x].name === inputName){
          return defaultValues[x].value;
        }
      }
      
      return '';
    };
    
    svc.defaultsLoaded = function(){
      return defaultsLoaded;
    };
    
    types = myGuns.map(function(item){
      return item.type;
    });
    
    b2cGunService.getDefaults(
      function(data){
        for(var item in data){
          if(data[item] === 'REQUIRED'){
            requiredInputs.push(item);
          } else {
            defaultValues.push({
              name: item,
              value: data[item]
            });
          }
        }
        defaultsLoaded = true;
      },
      function(error){
        $log.error(error);
      }
    );
    
    return svc;
  });

}());
