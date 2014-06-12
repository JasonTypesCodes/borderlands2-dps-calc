'use strict';

var app = angular.module('Borderlands2Calc', ['ui.bootstrap','ngAnimate','ui.router']);

app.config(function($stateProvider, $urlRouterProvider){
  $urlRouterProvider.otherwise('/guns');
  
  $stateProvider
    .state('guns', {
      url: '/guns',
      controller: 'MainController',
      templateUrl: 'partials/guns.html'
    })
    .state('add',{
      url: '/add',
      controller: 'AddController',
      templateUrl: 'partials/add.html'
    });
});

app.factory('b2cGunService', function($http){
  var svc = {};
  
  var endpoint = 'http://localhost:8080/service/dps.php';
  
  svc.getDefaults = function(success, error){
    $http.get(endpoint).success(function(data, status){
      error('Service responded with status: "' + status + '" which was unexpected at this time.');
    }).error(function(data, status){
      if(status === 400){
        success(data.expected_inputs);
      } else {
        error(data, status);
      }
    });
  };
  
  svc.doDpsCalc = function(gun, success, error){
    var encodedParams = '?';
    for(var item in gun){
      encodedParams = encodedParams + item + '=' + encodeURIComponent(gun[item]) + '&';
    }
    
    $http.get(endpoint + encodedParams)
      .success(function(data){
        var result = {};
        result.name = gun.name;
        result.type = gun.type;
        result.inputs = data.inputs;
        result.results = data.results;
        
        success(result);
      }).error(error);
  };

  return svc;
});

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
  }
  
  svc.addGun = function(newGun){
    svc.getGunsForType(newGun.type).push(newGun);
  }
  
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

app.factory('b2cNameMaker', function(){
  var svc = {};
  
  var lastUsed = 0;
  
  svc.getUniqueName = function(){
    lastUsed++;
    
    return 'Some Gun #' + lastUsed;
  };
  
  return svc;
});


app.controller('MainController', function($scope, $state, b2cStateService){
  
  $scope.errorMsg = '';
  $scope.activeType = b2cStateService.getActiveType();
  $scope.guns = b2cStateService.getGunsForType($scope.activeType);
  
  $scope.$watch('activeType', function(type){
    $scope.guns = b2cStateService.getGunsForType($scope.activeType);
    b2cStateService.setActiveType(type);
  });
  
  $scope.loadAddForm = function(){
    $state.go('add');
  };
  
  $scope.types = b2cStateService.getTypes();
  
});

app.controller('AddController', function($scope, $state, $log, b2cStateService, b2cGunService, b2cNameMaker){
  if(!b2cStateService.defaultsLoaded()){
    $state.go('guns');
  }
  
  function buildInputObject(inputName, displayName, type){
    return {
      name: inputName,
      displayName: displayName,
      type: type,
      required: b2cStateService.inputRequired(inputName),
      defaultValue: b2cStateService.inputDefaultValue(inputName)
    };
  }
  
  $scope.types = b2cStateService.getTypes();
    
  $scope.inputs = [
    buildInputObject('name','Name','text'),
    buildInputObject('damage','Damage','number'),
    buildInputObject('damageMultiplier','Damage Multiplier','number'),
    buildInputObject('accuracy','Accuracy','number'),
    buildInputObject('fireRate','Fire Rate','number'),
    buildInputObject('reloadTime','Reload Speed','number'),
    buildInputObject('magazineSize','Magazine Size','number'),
    buildInputObject('roundsPerShot','Rounds Per Shot','number'),
    buildInputObject('elementalDPS','Elemental DPS','number'),
    buildInputObject('elementalChance','Elemental Chance','number')
  ];
  
  $scope.newGun = {};
  
  $scope.goBack = function(){
    $state.go('guns');
  };
  
  $scope.doCalc = function(){
    b2cGunService.doDpsCalc(
      $scope.newGun,
      function(data){
        b2cStateService.setActiveType(data.type);
        b2cStateService.addGun(data);
        $state.go('guns');
      },
      function(error){
        $log.error(error);
      }
    );
  };
  
  $scope.newGun.type = b2cStateService.getActiveType();
  $scope.newGun.name = b2cNameMaker.getUniqueName();
});
