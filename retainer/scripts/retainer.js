$(document).ready(function() {
    var worker = gup("workerId");
    var assignment = gup("assignmentId");
    var task = gup('task') ? gup('task') : "default";
    
    var isAllowed = true;
    //checks if worker is already waiting for another task
    $.ajax({
    	async: false,
    	url: "php/isWorkerActive.php",
    	data: {workerId: worker},
    	dataType: "text",
    	success: function(d){
    		if(d == 1){
			isAllowed = false;
    			alert("You are already waiting for another task.");
    		}
    		else{
			 isAllowed = true;
		}
    	},
    	fail: function(){
    		alert("isWorkerActive failed!");
    	},
    });
    //alert("1 before stop");
    if( assignment != "ASSIGNMENT_ID_NOT_AVAILABLE" && isAllowed == true) {
        $.ajax({
            url: "php/setLive.php",
            data: {workerId: worker, task: task},
            dataType: "text",
            success: function(d) {
		url = "wait.php";//"../../ccvideo/tutorials/vote/indexRoles.php";
		//alert(url);
		url += "?workerId=" + gup('workerId');
                url += "&assignmentId=" + gup('assignmentId');
                url += "&hitId=" + gup('hitId');
                url += "&turkSubmitTo=" + gup('turkSubmitTo');
                url += "&task=" + gup('task');
                url += "&min=" + gup('min');
		// in order to show them the video, handled by "ccvideo/scripts/chat2.js" if necessary
                url += "&role=crowd&part=c"; 

		window.location = url;
            },
            fail: function() {
                alert("setLive failed!")
            },
        });
    }
});
