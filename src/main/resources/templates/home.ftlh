<!DOCTYPE html>
<html>
    <head>
        <title>Home page</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>

    <body>
        <button value="en" style="width:50%;padding: 10px;margin-bottom: 20px;">Send email in English</button>
        <button value="de" style="width:50%;padding: 10px;margin-bottom: 50px;">Send email in German</button>
    </body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js">
</script>
<script type="text/javascript">
$(document).ready(function() {
	$("body").on("click tap", "button", function(event) {
		var $target = $(event.target);
		var val = $target.val();
		$target.attr('disabled', 'disabled');
		window.location.replace('send?lang=' + val);
	});
});
</script>
</html>