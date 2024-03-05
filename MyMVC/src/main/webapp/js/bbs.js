/**
 * 
 */

 function bbs_check(){
	 let title = document.getElementById('title');
	 let writer = document.getElementById('writer');
	 let content = document.getElementById('content');
	 
	 if(!title.value){
		 alert('글제목을 입력하세요');
		 title.focus();
		 return false;
	 }
	 
	 if(!writer.value){
		 alert('작성자를 입력하세요');
		 writer.focus();
		 return false;
	 }
	 
	 if(!content.value){
		 alert('글내용을 입력하세요');
		 content.focus();
		 return false;
	 }
	 
	 return true;
 }