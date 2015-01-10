requirejs.config({
    shim: {
        'ckeditor-jquery': {
            deps: ['jquery', 'ckeditor-core']
        },
        'jquery-history': {
            deps: ['jquery']
        },
        'jquery-placeholder': {
            deps: ['jquery']
        },
        'jquery-timer': {
            deps: ['jquery']
        },
        'jquery-ui': {
            deps: ['jquery']
        },
        'bootstrap-datetimepicker': {
            deps: ['bootstrap', 'moment']
        }
    },
    paths: {
        'bootstrap': '/assets/lib/bootstrap/js/bootstrap.min',
        'bootstrap-datetimepicker': '/assets/lib/Eonasdan-bootstrap-datetimepicker/bootstrap-datetimepicker.min',
        'ckeditor-core': '/assets/lib/ckeditor/ckeditor',
        'ckeditor-jquery': '/assets/lib/ckeditor/adapters/jquery',
        'jquery': '/assets/lib/jquery/jquery.min',
        'jquery-history': '/assets/lib/jquery-history/jquery.history.min',
        'jquery-placeholder': '/assets/lib/jquery-placeholder/jquery.placeholder.min',
        'jquery-timer': '/assets/javascripts/jquery.timer',
        'jquery-ui': '/assets/lib/jquery-ui/jquery-ui.min',
        'highcharts': '/assets/lib/highcharts/highcharts-all',
        'html5-desktop-notifications': '/assets/lib/html5-desktop-notifications/desktop-notify-min',
        'moment': '/assets/lib/momentjs/min/moment.min',
        'prettify': '/assets/lib/prettify/prettify'
    }
});