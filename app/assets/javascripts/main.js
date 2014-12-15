requirejs.config({
  shim: {
    'ckeditor-jquery': {
      deps:['jquery', 'ckeditor-core']
    }
  },
  paths: {
    'jquery': '/assets/lib/jquery/jquery.min',
    'ckeditor-core': '/assets/lib/ckeditor/ckeditor',
    'ckeditor-jquery': '/assets/lib/ckeditor/adapters/jquery'
  }
});

require(["jquery", "ckeditor-jquery"], function( __jquery__ ) {
  $('.ckeditor').ckeditor();
});