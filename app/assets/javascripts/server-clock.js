require(["jquery", "jquery-timer"], function( __jquery__ ) {
    var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var clientDate = new Date();
    var toString = function(number, length, signed) {
        var str = "";
        if (signed === true) {
            str += (number < 0 ? "-" : "+");
            number = Math.abs(number);
            length--;
        }
        else if (number < 0) {
            str += "-";
            number = -number;
            length--;
        }
        number = number.toString();
        length -= number.length;
        for (var i = 0; i < length; i++)
            str += "0";
        str += number;
        return str;
    };
    var update = function() {
        var currentDate = new Date();
        serverDate.setTime(serverDate.getTime() + (currentDate.getTime() - clientDate.getTime()));
        clientDate = currentDate;
        var str = "";
        str += toString(serverDate.getDate(), 2, false) + "-";
        str += months[serverDate.getMonth()] + "-";
        str += serverDate.getFullYear() + " ";
        str += toString(serverDate.getHours(), 2, false) + ":";
        str += toString(serverDate.getMinutes(), 2, false) + ":";
        str += toString(serverDate.getSeconds(), 2, false) + " ";
        var offset = -serverDate.getTimezoneOffset();
        var offsetHours = offset / 60;
        var offsetMinutes = offset % 60;
        str += toString(offsetHours, 3, true) + ":";
        str += toString(offsetMinutes, 2, false);
        $("#server-clock").text(str);
    };

    $(document).ready(update);
    $(document).stopTime("server");
    $(document).everyTime('1s', "server", update);
});