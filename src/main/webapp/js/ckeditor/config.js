/**
 * @license Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// 配置点击图像点击上传到服务器的url
	config.image_previewText = ' ';
	config.filebrowserImageUploadUrl = '/Market/upload_saveFile.action';
};
