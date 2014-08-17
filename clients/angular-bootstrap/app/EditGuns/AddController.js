(function(){

  app.controller('AddController', function($scope, $state, $stateParams, $log, b2cStateService, b2cGunService, b2cNameMaker, b2cIdGenerator){
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
    
    
    $scope.goBack = function(){
      $state.go('guns');
    };
    
    $scope.doCalc = function(){
      b2cGunService.doDpsCalc(
        $scope.newGun,
        function(data){
          b2cStateService.setActiveType(data.type);
          b2cStateService.saveGun(data);
          $state.go('guns');
        },
        function(error){
          $log.error(error);
        }
      );
    };
    
    if($stateParams.id){
      $scope.newGun = b2cStateService.getGun($stateParams.id);
    } else {
      $scope.newGun = {};
      $scope.newGun.id = b2cIdGenerator.getNextId();
      $scope.newGun.type = b2cStateService.getActiveType();
      $scope.newGun.name = b2cNameMaker.getUniqueName();  
    }
  });

}());