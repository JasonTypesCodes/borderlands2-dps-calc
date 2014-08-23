(function(){
  
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
    
    $scope.displayableDPS = function(someGun){
      return b2cStateService.getDisplayableDPS(someGun);
    };
    
    $scope.editGun = function(id){
      $state.go('edit', {id: id});
    };
    
    $scope.deleteGun = function(id){
      b2cStateService.removeGun(id);
    };
    
    $scope.types = b2cStateService.getTypes();
    
  });
  
}());