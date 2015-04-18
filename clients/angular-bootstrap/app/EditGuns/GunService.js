(function(){
  app.factory('b2cGunService', function($http){
    var svc = {};
    
    var endpoint = 'http://bucket440.com/borderlands2/dps.php';
    
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
          result.id = gun.id;
          result.name = gun.name;
          result.type = gun.type;
          for(var item in data.inputs){
            result[item] = data.inputs[item];
          }
          result.results = data.results;
          
          success(result);
        }).error(error);
    };

    return svc;
  });
  
}());
  