
function createLegendButton(){
  	var legendButton = createTag("button");
  	legendButton.button();
  	legendButton.html("图例显示");
  	legendButton.click(legendDialogIMG);
  	$( "#layersdialog" ).append(legendButton);	
}


function legendDialog(){
	var layers = map.getLayers().getArray();
	var LAYERS=[];
	for(var i in layers){
		var source = layers[i].get('visible')?layers[i].getSource():null;
		//source = mapLayers[i].get('visible')?mapLayers[i].getSource():null;
		if(source !=null){
			var layer = returnLayerName(source);
			if(typeof layer != "undefined"){
				LAYERS.push(returnLayerName(source));
			}			
		}

		
	}
	if("dialog" in $("#legendDialog")){
		$("#legendDialog").dialog("destroy");
	}
	var div = createTag("div");
    div.css("overflow","hidden");
    div.css("cursor","pointer");
    div.attr("id","legendDialog");
	div.dialog({
		width:100,
	    position:{ my: "left+20000 top+150", at: "left bottom" ,of :$(".ol-zoom-out") },	     
	});
	//获取图例查询地址
	var contUrl="";
	for(var k =0;k<4;k++){
		contUrl += WMSHOST.split("/")[k];
		contUrl += "/";
	}
	
	for(var j in LAYERS){
		var cnName = getLayerName(LAYERS[j]);
		if(cnName !="动态层"){
			var url = constructLegendUrl(contUrl,20,20,LAYERS[j]);
			debugger;
			var legend = "<div style='margin-left: 0px;margin-top: 0px;margin-right: 0px;margin-bottom: 0px;overflow: hidden;'><iframe src='"+url+"' style='border:0;width:100%;height:100%'></iframe></div>";
			legend = '<img style="-webkit-user-select: none;" src='+url+'>';
			var title = "<h4 style='margin:0'>"+ cnName+"</h4>";
			div.append(title);
			div.append(legend);			
		}

	}

}

function legendDialogIMG(){
	if("dialog" in $("#legendDialog")){
		$("#legendDialog").dialog("destroy");
	}
	var div = createTag("div");
    div.css("overflow","hidden");
    div.css("cursor","pointer");
    div.attr("id","legendDialog");
	div.dialog({
		width:100,
	    position:{ my: "left+20000 top+150", at: "left bottom" ,of :$(".ol-zoom-out") },	     
	});
	var url = "js/images/legend.png";
	//var legend = "<div style='margin-left: 0px;margin-top: 0px;margin-right: 0px;margin-bottom: 0px;overflow: hidden;'><iframe src='"+url+"' style='border:0;width:100%;height:100%'></iframe></div>";
	var legend = '<img style="-webkit-user-select: none;" src='+url+'>';
	var title = "<h4 style='margin:0'>图例</h4>";
	div.append(title);
	div.append(legend);
}


function constructLegendUrl(host,x,y,LAYERS){
	//host = "http://192.168.3.30/geoserver/";
	var url = "wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH="+x+"&HEIGHT="+y+"&LAYER="+LAYERS;
	//url = "wms?REQUEST=GetLegendGraphic&VERSION=1.0.0&FORMAT=image/png&WIDTH=20&HEIGHT=20&LAYER=topp:states";
	//host = "http://192.168.10.155:8080/geoserver/";
	//LAYERS = "topp:states";
	return host+url;
}
