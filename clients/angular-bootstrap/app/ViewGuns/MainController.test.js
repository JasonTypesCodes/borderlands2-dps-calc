describe('MainController', function(){
  var scope, state, myB2cStateService, testController;
  
  beforeEach(function(){
    module('Borderlands2Calc');
    
    inject(function($controller, $rootScope){
      scope = $rootScope.$new();
      
      state = {
        go: function(){}
      };
      
      myB2cStateService = {
        setActiveType: sinon.spy(),
        getActiveType: sinon.stub().returns('Test Active Type'),
        getGunsForType: sinon.stub().returns([]),
        getDisplayableDPS: sinon.stub().returns(-1),
        removeGun: sinon.spy(),
        getTypes: sinon.stub().returns([])
      };
      
      testController = $controller('MainController', {
        $scope: scope,
        $state: sinon.stub(state),
        b2cStateService: myB2cStateService
      }); 
      
    });
    
  });
  
  it('Should not have errors', function(){
    expect(scope.errorMsg).toBe('');
  });
  
  it('Should change state when adding a gun', function(){
    scope.loadAddForm();
    
    expect(state.go).toHaveBeenCalledWith('add');
  });
  
  it('Should set active type', function(){
    expect(scope.activeType).toBe('Test Active Type');
  });
  
  it('Should change state to edit with appropriate id', function(){
    var testId = "1";
    
    scope.editGun(testId);
    
    var actualIdObj = state.go.firstCall.args[1];
    var actualState = state.go.firstCall.args[0];
    
    expect(actualState).toBe('edit');
    expect(actualIdObj.id).toBe(testId);
  });
  
});