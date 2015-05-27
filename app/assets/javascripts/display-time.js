require(["jquery", "jquery-timer", "moment"], function( __jquery__ ) {
    var moment = require("moment");
    var updateDisplayTime = function() {
        moment.locale($("#display-lang").attr("value"));
        elements = $("time.display-time");
        for (var i = 0; i < elements.length; i++) {
            var element = $(elements[i]);
            var datetime = moment(element.attr("datetime"));
            element.attr("title", datetime.format("DD-MMM-YYYY HH:mm:ss Z"));
            element.text(datetime.fromNow());
        }
    };
    $(document).ready(updateDisplayTime());
    $(document).stopTime("display-time");
    $(document).everyTime('60s', "display-time", updateDisplayTime);
});