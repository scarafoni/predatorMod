$(document).ready( function() {
    //
    $('#fire_button').click( function() {
        //
        link = $('#url_text').val();
	// Check if the link already contains a header
	if( link.substring(0, 7) != "http://" && link.substring(0, 8) != "https://"  && link.substring(0,3) != "../" ) {
		// If not, add one
		//link = link.substring(7);
		link = "http://" + link;
	}

        var numFire = $('#numFire').val();
        var task = gup('task') ? gup('task') : "default";
        var r = confirm("Firing to: " + link + ". Click cancel to stop.");
    	if(r == true)
    	{
    	$.ajax({
            url: "php/setFire.php",
            data: {url: link, task: task},
            dataType: "text",
            success: function(d) {
                //
                //alert("Fire successful");
            },
        fail: function() {
            alert("Fire failed!")
        },
    	});
    	
    	$.ajax({
            url: "php/updateReleased.php",
            data: {url: link, max: numFire},
            dataType: "text",
            success: function(d) {
                
            },
        fail: function() {
            alert("Sending number of workers failed")
        },
    	});
    	}

        // TODO: Ajax call to a PHP file that can set a flag in a DB to signal that workers should be forwarded from the pool
        // First version: route all workers from the pool to a URL
        // Second version: route all workers from a specific pool to a URL
        // Third version: route N workers from a specific pool to a URL
    });

    $('#url_text').keypress( function(e) {
        if( e.which == 13 ) {
            e.preventDefault;
            $('#fire_button').click();
        }
    });
});
