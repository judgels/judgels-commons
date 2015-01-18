require(["jquery", "jquery-timer"], function( __jquery__ ) {
    var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    var date = new Date();

    $(document).stopTime("server");
    $(document).everyTime('1s', "server", function(i)
    {
        date.setTime(date.getTime()+1000);
        var str = "";
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