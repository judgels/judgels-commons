require(["jquery", "jquery-timer", "moment"], function() {
    var moment = require("moment");
    moment.locale(language);
    var updateDisplayTime = function() {
        var elements = $("time.display-time");
        for (var i = 0; i < elements.length; i++) {
            var element = $(elements[i]);
            var datetime = moment(element.attr("datetime"));
            element.attr("title", datetime.format("DD-MMM-YYYY HH:mm:ss Z"));
            element.text(datetime.fromNow());
        }
    };
    $(document).ready(updateDisplayTime);
    $(document).stopTime("display-time");
    $(document).everyTime("60s", "display-time", updateDisplayTime);
});
