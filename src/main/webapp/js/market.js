//设置头像位置正中
function setUserImagesPosition(){
	$('.userImageDiv img, .userImageHead img').each(function (){
		var width = $(this).prop('width');
		var height = $(this).prop('height');
		if(width == 0 || height == 0)
			return;
		else if(width>height)
			$(this).prop('style','left:-'+(width-height)/width*50 +'%');
		else if(width<height)
			$(this).prop('style','top:-'+(height-width)/height*50 +'%');
	});
}