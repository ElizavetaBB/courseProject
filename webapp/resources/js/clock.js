$(document).ready(function () {
    function clock() {
        var d = new Date();
        var month=d.getMonth()+1;
        var day = d.getDate();
        var hrs = d.getHours();
        var min = d.getMinutes();
        var sec = d.getSeconds();
        if (month<=9) month="0"+ month;
        if (day <= 9) day="0" + day;
        if (hrs <= 9) hrs="0" + hrs;
        if (min <=9 ) min="0" + min;
        if (sec <= 9) sec="0" + sec;
        $("#time").html("Сейчас: " + hrs + ":" + min + ":" + sec + "&nbsp;&nbsp;&nbsp;" +
            day + "."+month + "." + d.getFullYear() + " г.");
    }
    clock();
    setInterval(clock,11000);
});