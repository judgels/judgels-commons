require(["jquery", "moment", "bootstrap", "bootstrap-datetimepicker"], function( __jquery__ ) {
    $(".datepicker").datetimepicker({
        locale: language,
        format: "DD-MM-YYYY"
    });
});