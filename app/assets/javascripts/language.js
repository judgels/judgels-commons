require(["jquery", "bootstrap", "bootstrap-select"], function( __jquery__ ) {
    $("#languages").change(function() {
        window.location = $("#languages").val();
    });
});