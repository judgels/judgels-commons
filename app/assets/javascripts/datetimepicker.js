require(["jquery", "moment", "bootstrap", "bootstrap-datetimepicker"], function( __jquery__ ) {
    $(".datetimepicker").datetimepicker({
        locale: language,
        format: "DD-MM-YYYY HH:mm:ss Z"
    });
});