require(["jquery", "bootstrap", "bootstrap-select"], function() {
    $("#languages").change(function() {
        window.location = $("#languages").val();
    });
});
