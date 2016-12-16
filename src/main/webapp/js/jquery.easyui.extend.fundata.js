(function($) {
    var init = function(target) {
        var $this = $(target);
        
        $this.attr({style:'display:inline-block;border-style:ridge;border-color:Silver'});
        
        $this.append( "<div class='fun' data-options=region:'west',border:false>"+
        				  "<img class='fun1' src='../pic/fun_gray.png'   style='height:100%;width:100%;position:absolute;left:0;top:0' />"+
        				  "<img class='yepian' src='../pic/fun_null.png' style='height:100%;width:100%;position:absolute;left:0;top:0' />"+ 
        				  "<img class='error'  src='../pic/fun_null.png' style='height:100%;width:100%;position:absolute;left:0;top:0' />"+
        				  "<img class='trouble' src='../pic/fun_null.png' style='height:100%;width:100%;position:absolute;left:0;top:0' />"+
        				  "<img class='group' src='../pic/fun_null.png' style='height:100%;width:100%;position:absolute;left:0;top:0' />"+
        				  "<audio id='audio' autoplay='autoplay'></audio>" +
					  "</div>"+
					  "<div class='data1' data-options=region:'center',border:false style='overflow:hidden;'>"+
					  	"<p class='funName' style='font-family: Verdana;'></p>"+
					  	"<p class='funMode' style='font-family: Verdana;background: rgb(230, 238, 214)'></p>"+
					  	"<p class='windSpeed' style='font-family: Verdana;'></p>"+
					  	"<p class='power' style='font-family: Verdana;background: rgb(230, 238, 214)'></p>"+
					  	"<p class='energy'style='font-family: Verdana;'></p>"+
					  	"<p class='energyCounter' style='font-family: Verdana;background: rgb(230, 238, 214)'></p>"+
					  "</div>"
	);
        $('.fun',$this).width($this.height());
        
        $this.layout();
        
        // 尝试去获取settings，如果不存在，则返回“undefined”
        var settings = $this.data('fundata');
        
        // 如果获取settings失败，则根据options和default创建它
        if(typeof(settings) == 'undefined') {

            var defaults = {
            	funName:'#机组',
            	___main_loop_mode_number: 0,
            	funModeTest:'初始化',
                windSpeed: 0.00,
                power: 0.00,
                energy: 0.00,
                energyCounter: 0.00,
                worning: false,
                error: false,
                url:'',
                errorSta: '',
                onSomeEvent: function(){}
            }
            
            $('.funName',$this).text(defaults.funName);
            $('.funMode',$this).text('风机模式：' + defaults.___main_loop_mode_number);
            $('.windSpeed',$this).text('风速(s/m)：    ' + defaults.windSpeed);
            $('.power',$this).text('有功功率(kW)：' + defaults.power);
            $('.energy',$this).text('日发电量(kW)：' + defaults.energy);
            $('.energyCounter',$this).text('总发电量(kW)：' + defaults.energyCounter);
            
            //settings = $.extend({}, defaults, thisOptions);

            // 保存新创建的settings
            $this.data('fundata', defaults);
            
        } else {
            //如果获取了settings，则将它和options进行合并（这不是必须的，可以选择不这样做）
            settings = $.extend({}, settings, thisOptions);
            
            //每次都保存options
            
             $this.data('fundata', settings);
        }

        // 执行代码
    }

    $.fn.fundata = function(options, param) {
        //如果options为string，则是方法调用，如$('#divMyPlugin').hello('sayHello');
        if (typeof options == 'string'){
            var method = $.fn.fundata.methods[options];
            if (method){
                return method(this, param);
            }
        }
         
        //否则是插件初始化函数，如$('#divMyPlugin').hello();
        options = options || {};
        return this.each(function(){
            var state = $.data(this, 'fundata');
            if (state){
                $.extend(state.options, options);
            } else {
                //easyui的parser会依次计算options、initedObj
                state = $.data(this, 'fundata', {
                    options: $.extend({}, $.fn.fundata.defaults, $.fn.fundata.parseOptions(this), options),
                    init: init(this) //这里的initedObj名称随便取的
                });
            }
        })      
    };
    
    $.fn.fundata.methods = {        
        resize: function( jq, params ){
			var funCount = jq.length;
			var height = $(window).height();
			var width = $(window).width();
			var funArea = (height*width/funCount)*0.2;
			var funHeight = (Math.sqrt(14400-(-4*funArea))-120)/2;

			return jq.each(function(){
				   var $this = $(this);
				   $this.height(funHeight).width(funHeight+120);
				   $this.layout('panel','west').panel('resize',{height:funHeight,width:funHeight});
				   $this.layout('panel','center').panel('resize',{left:funHeight,height:funHeight,width:120});
				   
			})

        },

        refresh: function(jq, params) {
	    	// 在每个元素中执行代码
	        return jq.each(function(i) {
	        	var thisOptions = params[i];
	        	
	            var $this = $(this);
	            // 执行代码
	            var settings = $this.data('fundata');
	            
	            var funMode = settings.___main_loop_mode_number;	            
	            
	            if(thisOptions.___main_loop_mode_number!=funMode){
	            	switch(thisOptions.___main_loop_mode_number){
	            	case 0:
	            		$('.fun1',$this).attr('src','../pic/fun_gray.png');
	            		$('.yepian',$this).attr('src','../pic/fun_null.png');
	            		
	            		$('.yepian',$this).velocity('finish');
	            		
	            		settings.funModeTest='初始化';
	            		break;
	            	case 1:
	            		$('.fun1',$this).attr('src','../pic/fun_red.png');
	            		$('.yepian',$this).attr('src','../pic/fun_null.png');
	            		$('.yepian',$this).velocity('finish');
	            		
	            		settings.funModeTest='停机';
	            		break;
	            	case 2:
	            		$('.fun1',$this).attr('src','../pic/fun_yellow.png');
	            		$('.yepian',$this).attr('src','../pic/fun_null.png');
	            		$('.yepian',$this).velocity('finish');
	            		settings.funModeTest='待机';
	            		break;
	            	case 3:
	            	case 4:
	            	case 5:
	            		$('.fun1',$this).attr('src','../pic/tatong.png');
	            		$('.yepian',$this).attr('src','../pic/yepian.png');
	            		$('.yepian',$this).velocity('finish');
	        			$('.yepian',$this).velocity({rotateZ:'360deg'},{ duration:6000,loop:true,easing:'linear'});
	        			settings.funModeTest='运行';
	            		break;
	            	case 9:
	            		$('.fun1',$this).attr('src','../pic/fun_purple.png');
	            		$('.yepian',$this).attr('src','../pic/fun_null.png');
	            		$('.yepian',$this).velocity('finish');
	            		settings.funModeTest='维护';
	            		break;
	            	case 10:
	            		$('.fun1',$this).attr('src','../pic/fun_blue.png');
	            		$('.yepian',$this).attr('src','../pic/fun_null.png');
	            		$('.yepian',$this).velocity('finish');
	            		settings.funModeTest='通信中断';
	            		break;
	            	}
	            }
	            
	            $('.funName',$this).text(thisOptions.fun.name);
	            $('.funMode',$this).text('风机模式：' + settings.funModeTest);
	            
	            if(thisOptions.___wind_speed!=undefined){
	            	
	            	$('.windSpeed',$this).text('风速(m/s):' + Math.round(thisOptions.___wind_speed*100)/100);
	            	$('.power',$this).text('有功功率(kW):' + Math.round(thisOptions.___visu_grid_power*100)/100);
	            	$('.energy',$this).text('日发电量(kW):' + Math.round(thisOptions.___visu_grid_energy));
	            	$('.energyCounter',$this).text('总发电量(kW):' + Math.round(thisOptions.___visu_grid_energy_counter));
	            	
		            if(thisOptions.___error_error_global==true){
		            	$('.error',$this).attr('src','../pic/error.png');
		            	
		            	if (settings.errorSta!=true){
			            	$('#audio',$this).attr('src','../baiduText2audioAction?vol=9&text=' + thisOptions.fun.name + '故障' + ',' + thisOptions.fun.name + '故障');
			            	settings.errorSta=true;
		            	}
		            }else if(thisOptions.___warning_warning_global==true){
		            	$('.error',$this).attr('src','../pic/warning.png');
		            }else{
		            	$('.error',$this).attr('src','../pic/fun_null.png');
		            }
		            
		            settings.errorSta = thisOptions.___error_error_global;
		            
	            }
	            
	            //只在首次刷新时设置该值。
//	            if(settings.fun==undefined){
//	            	$('.group', $this).attr('src', '../pic/loop' + thisOptions.fun.line + '.png');
//	            };
	            
	            settings = $.extend({}, settings, thisOptions);
	
	            // 保存新创建的settings
	            $this.data('fundata', settings);
	
	        });
	        
	    }
    };
    
    $.fn.fundata.defaults = {
    		//默认属性定义
          	funName:'#机组',
        	___main_loop_mode_number: 0,
            windSpeed: 0.00,
            power: 0.00,
            energy: 0.00,
            energyCounter: 0.00,
            worning: false,
            error: false,
            url:''
    	};
    
    $.fn.fundata.parseOptions = function (target) {
        var opts = $.extend({}, $.parser.parseOptions(target, []));//这里可以指定参数的类型
        return opts;
    };

    $.parser.plugins.push('fundata');//将自定义的插件加入 easyui 的插件组
 
})(jQuery);
