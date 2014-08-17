describe('main specs', function(){
  beforeEach(function(){
    browser.driver.manage().window().setSize(1280, 1024);
    browser.get('http://localhost:8080/clients/angular-bootstrap/build/');
  });
  
  it('should have a gun form', function(){
    element(by.partialButtonText('Add Gun')).click();
    
    expect(element(by.buttonText('Add')).isEnabled()).toBe(false);

    element(by.id('newGun_name')).clear().sendKeys('Test Gun');
    element(by.id('newGun_damage')).sendKeys(100);
    element(by.id('newGun_fireRate')).sendKeys(100);
    
    expect(element(by.buttonText('Add')).isEnabled()).toBe(true);
    
    element(by.buttonText('Add')).click();
    
    expect(element.all(by.className('panel')).count()).toBe(1);
    
    element(by.className('panel')).click();
    
    expect(element(by.binding('{{aGun.name}}')).getText()).toBe('Test Gun');
    expect(element(by.binding('{{displayableDPS(aGun)}}')).getText()).toBe('DPS: 10000');
  });
});