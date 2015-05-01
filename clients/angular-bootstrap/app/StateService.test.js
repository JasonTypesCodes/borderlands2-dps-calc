describe('State Service', function(){

  beforeEach(function(){
    module('Borderlands2Calc');
    
    var storageStub = sinon.stub({
      save: function(){},
      get: function(){}
    });
    
    module(function($provide){
      $provide.value('b2cGunService', {
        getDefaults: function(success, error){
          success({
            'requiredItem1': 'REQUIRED',
            'somethingElse': 1001,
            'requiredItem2': 'REQUIRED'
          });
        }
      });
      
      $provide.value('b2cStorageService', storageStub);
    });
    
    storageStub.get.returns([{type: 'Pistol', guns: []}]);
  });

  it('sets the default type', inject(function(b2cStateService){
    expect(b2cStateService.getActiveType()).toBe('Pistol');
  }));
  
  it('gets default & required input info from GunService', inject(function(b2cStateService){
    expect(b2cStateService.defaultsLoaded()).toBe(true);
    expect(b2cStateService.inputDefaultValue('somethingElse')).toBe(1001);
    expect(b2cStateService.inputDefaultValue('requiredItem1')).toBe('');
    expect(b2cStateService.inputRequired('requiredItem2')).toBe(true);
    expect(b2cStateService.inputRequired('somethingElse')).toBe(false);
  }));
  
  it('can manage guns', inject(function(b2cStateService){
    var testGun = {
      type: 'Pistol',
      id: 1002,
      secretValue: true
    };
    
    expect(b2cStateService.getGunsForType('Pistol').length).toBe(0);
    
    b2cStateService.saveGun(testGun);
    
    expect(b2cStateService.getGunsForType('Pistol').length).toBe(1);
    
    expect(b2cStateService.getGun(1002).secretValue).toBe(true);
    
    b2cStateService.removeGun(1002);
    
    expect(b2cStateService.getGunsForType('Pistol').length).toBe(0);
    
  }));

});