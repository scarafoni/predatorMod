var Router = {

session: null,
role: null,
workerId: null,

init: function() {
    $.ajax({
        url: "php/getActiveGame.php",
            type: "POST",
            dataType: "text",
            success: function(d) {
            Router.game = d.split("|")[0]
            Router.role = d.split("|")[1]
            Router.assignId = gup('assignmentId');
            Router.workerId = gup('workerId');

            var url = "../index.php"

            url += "?game=" + Router.game
            url += "&wrole=" + Router.role
            url += "&assignmentId=" + Router.assignId
            url += "&workerId=" + Router.workerId

            window.location = url
        }
    });
},



}

$(document).ready( function() {
    Router.init()
});
