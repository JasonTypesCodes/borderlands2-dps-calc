var gulp = require('gulp'),
    jshint = require('gulp-jshint'),
    csslint = require('gulp-csslint'),
    del = require('del');
    

gulp.task('jshint', function(){
  return gulp.src('./app/app.js')
    .pipe(jshint('.jshintrc'))
    .pipe(jshint.reporter('default', {verbose: true}))
    .pipe(jshint.reporter('fail'));
});