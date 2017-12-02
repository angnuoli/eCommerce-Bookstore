//<![CDATA[ 

function readMoreBookDescription(id) {
	/* Read more description */

	/*
	 * document.querySelector('#bookAbbrDescription-'+id).style.maxHeight =
	 * 'none'; var currentHeight =
	 * document.querySelector('#bookAbbrDescription-'+id).clientHeight;
	 * document.querySelector('#bookAbbrDescription-'+id).style.maxHeight =
	 * '212px';
	 */

	$('#bookAbbrDescription-' + id).animate(
			{
				'maxHeight' : document.querySelector('#bookAbbrDescription-'
						+ id).scrollHeight
			}, 200, 'linear');

	console
			.log(document.querySelector('#bookAbbrDescription-' + id).style.maxHeight);

	$('#bdSeeAllPrompt-' + id).css('display', 'none')
	$('#bdSeeLessPrompt-' + id).css('display', 'block');
}

function readLessBookDescription(id) {
	/* Read more description */

	var currentHeight = document.querySelector('#bookAbbrDescription-' + id).clientHeight;

	$('#bookAbbrDescription-' + id).animate({
		maxHeight : '212'
	}, 200, 'linear');

	$('#bdSeeAllPrompt-' + id).css('display', 'block')
	$('#bdSeeLessPrompt-' + id).css('display', 'none');
}

$(document).ready(function() {
	$('#bookList').DataTable({
		"lengthMenu" : [ [ 5, 10, 15, 20, -1 ], [ 5, 10, 15, 20, "All" ] ],
		"ordering" : false,
		stateSave : true
	});

	document.querySelector('.sorting_disabled').style.borderBottom = "none";
	document.querySelector('.sorting_disabled').style.padding = "5px 0px";

	$("#bookList").on('page.dt', function() {
		$('html, body').animate({
			scrollTop : $('#bookList').offset().top
		}, 200);
	});

});

$('.a-link-expander')
		.on(
				'click',
				function() {
					var id = this.id.substring(this.id.lastIndexOf('-') + 1,
							this.id.length);
					console.log('txt');
					console.log(document.querySelector('#bookAbbrDescription-'
							+ id).clientHeight);
					document.querySelector('#bdSeeAllPrompt-' + id).style.display == 'block' ? readMoreBookDescription(id)
							: readLessBookDescription(id);
				});
// ]]>
