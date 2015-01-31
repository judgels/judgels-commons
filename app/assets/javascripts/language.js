require(["jquery", "boostrap", "bootstrap-select"], function( __jquery__ ) {
    $(document).ready(function() {
        $("#languages").change(function() {
            window.location = $("#languages").val();
        });
    });
});