requirejs.config({
    shim: {
        'bootstrap': {
            deps: ['jquery']
        },
        'bootstrap-select': {
            deps: ['bootstrap']
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
        }
    },
    paths: {
        'bootstrap': '/assets/lib/bootstrap/js/bootstrap.min',
        'bootstrap-select': '/assets/lib/bootstrap-select/js/bootstrap-select',
        'jquery': '/assets/lib/jquery/jquery.min',
        'jquery-timer': '/assets/javascripts/jquery.timer',
        'jquery-ui': '/assets/lib/jquery-ui/jquery-ui.min',
        'prettify': '/assets/lib/prettify/prettify',
        'moment': '/assets/lib/momentjs/min/moment-with-locales.min',
    }
});