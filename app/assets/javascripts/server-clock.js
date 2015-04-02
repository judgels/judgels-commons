require(["jquery", "jquery-timer"], function( __jquery__ ) {
    var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var localDate = new Date();

    $(document).stopTime("server");
    $(document).everyTime('1s', "server", function(i)
    {
        var currentDate = new Date();
        date.setTime(date.getTime()+(currentDate.getTime() - localDate.getTime()));
        localDate = currentDate;
        var str = "";
        str += (date.getDate() < 10) ? "0" : "";
        str += date.getDate() + "-";
        str += months[date.getMonth()] + "-";
        str += date.getFullYear() + " ";
        str += (date.getHours() < 10) ? "0" : "";
        str += date.getHours() + ":";
        str += (date.getMinutes() < 10) ? "0" : "";
        str += date.getMinutes() + ":";
        str += (date.getSeconds() < 10) ? "0" : "";
        str += date.getSeconds();
        $("#server-clock").text(str);
    });
});