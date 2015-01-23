requirejs.config({
    shim: {
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
        'jquery': '/assets/lib/jquery/jquery.min',
        'jquery-timer': '/assets/javascripts/jquery.timer',
        'jquery-ui': '/assets/lib/jquery-ui/jquery-ui.min',
        'prettify': '/assets/lib/prettify/prettify'
    }
});