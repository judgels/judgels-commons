require(["jquery", "jquery-timer", "moment"], function() {
    var moment = require("moment");
    moment.locale(language);
    var serverDateTime = moment($("time#server-time").attr("datetime"));
    var clientDateTime = moment();
    var updateServerTime = function() {
        var currentDateTime = moment();
        serverDateTime.add(currentDateTime.valueOf() - clientDateTime.valueOf(), "ms");
        clientDateTime = currentDateTime;
        $("time#server-time").text(serverDateTime.format("DD-MMM-YYYY HH:mm:ss Z"));
    };
    $(document).ready(updateServerTime);
    $(document).stopTime("server-time");
    $(document).everyTime("1s", "server-time", updateServerTime);
});
