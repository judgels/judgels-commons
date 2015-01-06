require(["jquery"], function( __jquery__ ) {
    $(document).ready(function() {
        $("#languages").change(function() {
            window.location = $("#languages").val();
        });
    });
});