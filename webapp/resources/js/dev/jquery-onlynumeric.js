/* JQuery-Numeric validator by Leandro Alves da Costas */
(function($){
	$.fn.onlyNumeric=function(){
		return this.each(function(){
			$(this).bind('keyup blur',function(){
				var regexp=/[^0-9]/;
				while(regexp.test(this.value)){
					this.value=this.value.replace(regexp,'');
				}
			});
		});
	}
})(jQuery);