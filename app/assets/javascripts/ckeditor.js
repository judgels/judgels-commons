require(["jquery", "ckeditor-jquery"], function( __jquery__ ) {
    CKEDITOR.config.toolbar = [
        ['Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat'], ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote'], ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'], ['Image', 'Link'], ['Styles', 'Format'], ['Source', '-', 'Preview']
    ];
    $('.ckeditor').ckeditor();
});