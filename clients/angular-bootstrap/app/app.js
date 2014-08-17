var app = angular.module('Borderlands2Calc', ['ui.bootstrap','ngAnimate','ui.router']);

(function(){
  'use strict';

  app.config(function($stateProvider, $urlRouterProvider){
    $urlRouterProvider.otherwise('/guns');
    
    $stateProvider
      .state('guns', {
        url: '/guns',
        controller: 'MainController',
        templateUrl: 'ViewGuns/partials/guns.html'
      })
      .state('add', {
        url: '/add',
        controller: 'AddController',
        templateUrl: 'EditGuns/partials/add.html'
      })
      .state('edit', {
        url: '/edit/{id}',
        controller: 'AddController',
        templateUrl: 'EditGuns/partials/add.html'
      });
  });
  
})();