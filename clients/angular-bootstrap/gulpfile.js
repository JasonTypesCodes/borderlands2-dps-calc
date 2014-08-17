var gulp = require('gulp'),
    jshint = require('gulp-jshint'),
    csslint = require('gulp-csslint'),
    karma = require('karma').server,
    del = require('del');
    
var karmaConfig = {
  basePath: '',
  frameworks: ['jasmine'],
  files: [
    'bower_components/angular/angular.js',
    'bower_components/angular-mocks/angular-mocks.js',
    'bower_components/angular-animate/angular-animate.js',
    'bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
    'bower_components/angular-ui-router/release/angular-ui-router.js',
    'app/app.js',
    'app/**/*.test.js'
  ],
  exclude: [],
  preprocessors: {},
  reporters: ['progress'],
  port: 9876,
  colors: true,
  autoWatch: false,
  browsers: ['Firefox','PhantomJS'],
  singleRun: true
};

gulp.task('jshint', function(){
  return gulp.src('./app/app.js')
    .pipe(jshint('.jshintrc'))
    .pipe(jshint.reporter('default', {verbose: true}))
    .pipe(jshint.reporter('fail'));
});

gulp.task('karma', function(done){
  karma.start(karmaConfig, done);
});