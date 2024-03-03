(function(w,x,y,z,A,B,m,C,n,p,q,D,E,s,F,G,H,I,K,t,u,L,v,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,$,aa,ba,ca,da,ea,fa,ga,ha,ia,ja,ka,la,ma,na,oa,pa,qa,e,k){m("J.console");v(J.console,"GenericTextArea");m("J.console");n(["J.api.JmolAppConsoleInterface","$.JmolCallbackListener","java.util.Hashtable"],"J.console.GenericConsole",["JU.PT","J.c.CBK","J.i18n.GT","JS.T","JV.Viewer"],function(){var b=q(function(){this.label1=this.defaultMessage=this.loadButton=this.clearInButton=this.clearOutButton=this.stateButton=this.historyButton=
this.runButton=this.editButton=this.menuMap=this.vwr=this.output=this.input=null;this.nTab=0;this.incompleteCmd=null;p(this,arguments)},J.console,"GenericConsole",null,[J.api.JmolAppConsoleInterface,J.api.JmolCallbackListener]);t(b,function(){this.menuMap=new java.util.Hashtable});e(b,"setViewer",function(a){this.vwr=a;null==J.console.GenericConsole.labels&&(a=new java.util.Hashtable,a.put("title",J.i18n.GT.$("Jmol Script Console")+" "+JV.Viewer.getJmolVersion()),this.setupLabels(a),J.console.GenericConsole.labels=
a)},"JV.Viewer");e(b,"addButton",function(a,c){a.addConsoleListener(this);this.menuMap.put(c,a);return a},"J.api.JmolAbstractButton,~S");e(b,"getLabel1",function(){return null});e(b,"setupLabels",function(a){a.put("saveas",J.i18n.GT.$("&Save As..."));a.put("file",J.i18n.GT.$("&File"));a.put("close",J.i18n.GT.$("&Close"));this.setupLabels0(a)},"java.util.Map");e(b,"setupLabels0",function(a){a.put("help",J.i18n.GT.$("&Help"));a.put("search",J.i18n.GT.$("&Search..."));a.put("commands",J.i18n.GT.$("&Commands"));
a.put("functions",J.i18n.GT.$("Math &Functions"));a.put("parameters",J.i18n.GT.$("Set &Parameters"));a.put("more",J.i18n.GT.$("&More"));a.put("Editor",J.i18n.GT.$("Editor"));a.put("State",J.i18n.GT.$("State"));a.put("Run",J.i18n.GT.$("Run"));a.put("Clear Output",J.i18n.GT.$("Clear Output"));a.put("Clear Input",J.i18n.GT.$("Clear Input"));a.put("History",J.i18n.GT.$("History"));a.put("Load",J.i18n.GT.$("Load"));a.put("label1",J.i18n.GT.$("press CTRL-ENTER for new line or paste model data and press Load"));
a.put("default",J.i18n.GT.$("Messages will appear here. Enter commands in the box below. Click the console Help menu item for on-line help, which will appear in a new browser window."))},"java.util.Map");e(b,"setLabels",function(){var a=J.i18n.GT.setDoTranslate(!0);this.editButton=this.setButton("Editor");this.stateButton=this.setButton("State");this.runButton=this.setButton("Run");this.clearOutButton=this.setButton("Clear Output");this.clearInButton=this.setButton("Clear Input");this.historyButton=
this.setButton("History");this.loadButton=this.setButton("Load");this.defaultMessage=J.console.GenericConsole.getLabel("default");this.setTitle();J.i18n.GT.setDoTranslate(a)});b.getLabel=e(b,"getLabel",function(a){return J.console.GenericConsole.labels.get(a)},"~S");e(b,"displayConsole",function(){this.layoutWindow(null);this.outputMsg(this.defaultMessage)});e(b,"updateLabels",function(){});e(b,"completeCommand",function(a){if(0==a.length)return null;var c=0>=this.nTab||null==this.incompleteCmd?a:
this.incompleteCmd;this.incompleteCmd=c;a=J.console.GenericConsole.splitCommandLine(a);if(null==a)return null;var f=null==a[2],r=null!=a[3],d=a[f?1:2],b=a[1];if(0==d.length)return null;a=JS.T.getTokenFromName(b.trim().toLowerCase());var h=JS.T.tokAttr(null==a?0:a.tok,12288);a=J.console.GenericConsole.splitCommandLine(c);var g=null;if(!f&&('"'==d.charAt(0)||"'"==d.charAt(0)))f=d.charAt(0),JU.PT.trim(d,"\"'"),d=JU.PT.trim(a[2],"\"'"),g=this.nextFileName(d,this.nTab),null!=g&&(g=a[0]+a[1]+f+g+f);else{g=
null;if(!f&&(d=b,r||a[2].startsWith("$")||h))g=new java.util.Hashtable,this.vwr.getObjectMap(g,r||h?"{":a[2].startsWith("$")?"$":"0");g=JS.T.completeCommand(g,b.equalsIgnoreCase("set "),f,f?a[1]:a[2],this.nTab);g=a[0]+(null==g?d:f?g:a[1]+g)}return null==g||g.equals(c)?null:g},"~S");e(b,"doAction",function(a){if(a===this.runButton)this.execute(null);else if(a===this.editButton)this.vwr.getProperty("DATA_API","scriptEditor",null);else if(a===this.historyButton)this.clearContent(this.vwr.getSetHistory(2147483647));
else if(a===this.stateButton)this.clearContent(this.vwr.getStateInfo());else if(a===this.clearInButton){this.input.setText("");return}a===this.clearOutButton?this.output.setText(""):a===this.loadButton?this.vwr.loadInlineAppend(this.input.getText(),!1):this.isMenuItem(a)&&this.execute(a.getName())},"~O");e(b,"execute",function(a){var c=null==a?this.input.getText():a;null==a&&this.input.setText(null);a=this.vwr.script(c+"; ## GUI ##\u0001## EDITOR_IGNORE ##");null!=a&&!a.equals("pending")&&this.outputMsg(a)},
"~S");e(b,"destroyConsole",function(){this.vwr.isApplet&&this.vwr.getProperty("DATA_API","getAppConsole",Boolean.FALSE)});b.setAbstractButtonLabels=e(b,"setAbstractButtonLabels",function(a,c){for(var f,b=a.keySet().iterator();b.hasNext()&&((f=b.next())||1);){var d=a.get(f),j=c.get(f);if(f.indexOf("Tip")==f.length-3)d.setToolTipText(c.get(f));else{var h=J.console.GenericConsole.getMnemonic(j);" "!=h&&d.setMnemonic(h);j=J.console.GenericConsole.getLabelWithoutMnemonic(j);d.setText(j)}}},"java.util.Map,java.util.Map");
b.getLabelWithoutMnemonic=e(b,"getLabelWithoutMnemonic",function(a){if(null==a)return null;var c=a.indexOf("&");return-1==c?a:a.substring(0,c)+(c<a.length-1?a.substring(c+1):"")},"~S");b.getMnemonic=e(b,"getMnemonic",function(a){if(null==a)return" ";var c=a.indexOf("&");return-1==c||c==a.length-1?" ":a.charAt(c+1)},"~S");b.map=e(b,"map",function(a,c,f,b){f=J.console.GenericConsole.getMnemonic(f);" "!=f&&a.setMnemonic(f);null!=b&&b.put(c,a)},"~O,~S,~S,java.util.Map");k(b,"notifyEnabled",function(a){switch(a){case J.c.CBK.ECHO:case J.c.CBK.MEASURE:case J.c.CBK.MESSAGE:case J.c.CBK.PICK:return!0}return!1},
"J.c.CBK");k(b,"notifyCallback",function(a,c){var f=null==c||null==c[1]?null:c[1].toString();switch(a){case J.c.CBK.ECHO:this.sendConsoleEcho(f);break;case J.c.CBK.MEASURE:var b=c[3];0<=b.indexOf("Picked")||0<=b.indexOf("Sequence")?this.sendConsoleMessage(f):0<=b.indexOf("Completed")&&this.sendConsoleEcho(f);break;case J.c.CBK.MESSAGE:this.sendConsoleMessage(null==c?null:f);break;case J.c.CBK.PICK:this.sendConsoleMessage(f)}},"J.c.CBK,~A");k(b,"getText",function(){return this.output.getText()});k(b,
"sendConsoleEcho",function(a){null==a?(this.updateLabels(),this.outputMsg(null),a=this.defaultMessage):a.equals("\x00")&&(Clazz_Console.clear(),a=null);this.outputMsg(a)},"~S");e(b,"outputMsg",function(a){var c=null==a?-1:a.length;switch(c){case -1:this.output.setText("");return;default:if("\n"==a.charAt(c-1))break;case 0:a+="\n"}this.output.append(a)},"~S");e(b,"clearContent",function(a){this.output.setText(a)},"~S");k(b,"sendConsoleMessage",function(a){null!=a&&this.output.getText().startsWith(this.defaultMessage)&&
this.outputMsg(null);this.outputMsg(a)},"~S");k(b,"setCallbackFunction",function(){},"~S,~S");k(b,"zap",function(){});e(b,"recallCommand",function(a){a=this.vwr.getSetHistory(a?-1:1);null!=a&&(a=this.trimGUI(a),this.input.setText(JU.PT.escUnicode(a)))},"~B,~B");e(b,"trimGUI",function(a){var c=a.indexOf("; ## GUI ##");0<=c&&(a=a.substring(0,c));return JU.PT.trim(a,"; ")},"~S");e(b,"processKey",function(a,c,f){var b=0;switch(c){case 401:switch(a){case 9:c=this.input.getText();if(c.endsWith("\n")||c.endsWith("\t"))return 0;
b=1;if(this.input.getCaretPosition()==c.length)return a=this.completeCommand(c),null!=a&&this.input.setText(JU.PT.escUnicode(a).$replace("\t"," ")),this.nTab++,b;break;case 27:b=1,this.input.setText("")}this.nTab=0;if(10==a&&!f)return this.execute(null),b;if(38==a||40==a)return this.recallCommand(38==a,!1),b;break;case 402:if(10==a&&!f)return b}return b|2},"~N,~N,~B");b.splitCommandLine=e(b,"splitCommandLine",function(a){var c=Array(4),f=!1,b=!1,d=!1;if(0==a.length)return null;for(var j=-1,h=0,g=
0,e=0,k,l=0;l<a.length;l++){switch((k=a.charAt(l)).charCodeAt(0)){case 34:!d&&!f&&(b=!b)&&(j=g=l);break;case 39:!d&&!b&&(f=!f)&&(j=g=l);break;case 92:d=!d;continue;case 32:!d&&(!f&&!b)&&(g=l+1,j=-1);break;case 59:!f&&!b&&(h=g=l+1,j=-1,e=0);break;case 123:case 125:!f&&!b&&(e+="{"==k?1:-1,g=l+1,j=-1);break;default:!f&&!b&&(j=-1)}d=!1}c[0]=a.substring(0,h);c[1]=g==h?a.substring(h):a.substring(h,g>j?g:j);c[2]=g==h?null:a.substring(g);c[3]=0<e?"{":null;return c},"~S");b.labels=null});m("J.consolejs");
n(["J.console.GenericConsole"],"J.consolejs.AppletConsole",null,function(){var b=q(function(){this.jsConsole=null;p(this,arguments)},J.consolejs,"AppletConsole",J.console.GenericConsole);s(b,function(){u(this,J.consolejs.AppletConsole,[])});k(b,"start",function(a){this.setViewer(a);this.setLabels();this.displayConsole()},"JV.Viewer");k(b,"layoutWindow",function(){this.jsConsole=new Jmol.Console.JSConsole(this);this.setTitle()},"~S");k(b,"setTitle",function(){null!=this.jsConsole&&this.jsConsole.setTitle(J.console.GenericConsole.getLabel("title"))});
k(b,"setVisible",function(a){this.jsConsole.setVisible(a)},"~B");k(b,"setButton",function(a){return new Jmol.Console.Button(a)},"~S");k(b,"dispose",function(){this.setVisible(!1)});k(b,"isMenuItem",function(){return!1},"~O");k(b,"getScriptEditor",function(){return null});k(b,"nextFileName",function(){return null},"~S,~N");k(b,"newJMenu",function(){return null},"~S");k(b,"newJMenuItem",function(){return null},"~S");Jmol.Console={buttons:{},buttonWidth:100,click:function(a){Jmol.Console.buttons[a].console.appletConsole.doAction(Jmol.Console.buttons[a])}};
Jmol.consoleGetImageDialog=function(a,c,f){return new Jmol.Console.Image(a,c,f)};Jmol.Console.Image=function(a,c,f){this.vwr=a;this.title=c;this.imageMap=f;this.applet=a.html5Applet;a=this.applet._id+"_Image";this.id=a+"_"+(""==c?"app":c).replace(/\W/g,"_");var b=Jmol._$(this.id+"_holder");if(!b[0]&&(b=Jmol._$(a+"_holder"))[0])this.id=a;b[0]?this.div=b:Jmol.Console.createDOM(this,'<div id="$ID" class="jmolImage" style="display:block;background-color:yellow;position:absolute;z-index:'+ ++Jmol._z.consoleImage+
'"><div id="$ID_title"></div><div id="$ID_holder"></div></div>');System.out.println("image "+this.id+" created");(a=f.get(this.id))&&a.closeMe();f.put(this.id,this);f.put(c,this)};Jmol.Console.Image.setCanvas=function(a,c){Jmol.$append(Jmol._$(a.id+"_holder"),c);Jmol.$html(a.id+"_title","<table style='width:100%'><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"javascript:Jmol.Console.buttons['"+a.id+"'].closeMe()\">close</a></td><td align=right>"+a.title+" ["+c.width+" x "+c.height+"]</td></tr></table>")};
Jmol.Console.Image.closeImage=function(a){a.imageMap.remove(a.title);a.imageMap.remove(a.id);a.div?Jmol.$remove(a.cid):(a.dragBind(!1),Jmol.$remove(a.id))};Jmol.Console.Image.prototype.setImage=function(a){this.cid&&Jmol.$remove(this.cid);var c=document.createElement("canvas");c.width=a.width;c.height=a.height;var b=c.getContext("2d");if(a.buf32){var e=b.getImageData(0,0,c.width,c.height),d=e.data;a=a.buf32;for(var j=d.length>>2,h=0,g=0;h<j;h++)d[g++]=a[h]>>16&255,d[g++]=a[h]>>8&255,d[g++]=a[h]&255,
d[g++]=255;b.putImageData(e,0,0)}else b.drawImage(a,0,0);this.cid=c.id=this.id+"_image";Jmol.Console.Image.setCanvas(this,c)};Jmol.Console.Image.prototype.closeMe=function(){Jmol.Console.Image.closeImage(this)};Jmol.Swing.setDraggable(Jmol.Console.Image);Jmol.Console.createDOM=function(a,c,b){var e=a.id;Jmol.Console.buttons[e]=a;c=c.replace(/\$ID/g,e);b&&b[0]?Jmol.$html(b,c):(Jmol.$after("body",c),a.setContainer(Jmol._$(e)),a.setPosition(),a.dragBind(!0))};Jmol.Console.JSConsole=function(a){this.applet=
a.vwr.html5Applet;var c=this.id=this.applet._id+"_console",b=this;b.appletConsole=a;b.input=a.input=new Jmol.Console.Input(b);b.output=a.output=new Jmol.Console.Output(b);var e=Jmol.$("#"+c),d='<div id=$ID_title></div><div id=$ID_label1></div><div id=$ID_outputdiv style="position:relative;left:2px"></div><div id=$ID_inputdiv style="position:relative;left:2px"></div><div id=$ID_buttondiv></div>',j=600,h=362;if(e[0]){var g=Jmol.$getSize(e);0==g[0]&&Jmol.$setSize(e,j,h);j=g[0]||j;h=g[1]||h}else d='<div spellcheck="false" id="$ID" class="jmolConsole" style="display:block;background-color:yellow;width:'+
j+"px;height:"+h+"px;position:absolute;z-index:"+Jmol._z.console+'">'+d+"</div>";Jmol.Console.createDOM(this,d,e);d=function(a,b){b.console=a;b.id=c+"_"+b.label.replace(/\s/g,"_");Jmol.Console.buttons[b.id]=b;return b.html()};d=d(b,a.runButton)+d(b,a.loadButton)+d(b,a.clearInButton)+d(b,a.clearOutButton)+d(b,a.historyButton)+d(b,a.stateButton);Jmol.$html(c+"_buttondiv",d);d="";e[0]||(d+="&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"javascript:Jmol.Console.buttons['"+c+"'].setVisible(false)\">close</a>");d+=
'&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:Jmol.script('+b.applet._id+",'help')\">help</a>";Jmol.$html(c+"_label1",d);e[0]?(j-=10,h=(h-Jmol.$getSize(c+"_label1")[1]-Jmol.$getSize(c+"_buttondiv")[1]-20)/3):(j-=10,h=(h-62)/3);Jmol.$html(c+"_inputdiv",'<textarea id="'+c+'_input" style="width:'+j+"px;height:"+h+'px"></textarea>');Jmol.$html(c+"_outputdiv",'<textarea id="'+c+'_output" style="width:'+j+"px;height:"+2*h+'px"></textarea>');Jmol.Cache.setDragDrop(this.applet,"console_output");Jmol.Cache.setDragDrop(this.applet,
"console_input");Jmol.$bind("#"+c+"_input","keydown keypress keyup",function(a){b.input.keyEvent(a)});Jmol.$bind("#"+c+"_input","mousedown touchstart",function(){b.ignoreMouse=!0});Jmol.$bind("#"+c+"_output","mousedown touchstart",function(){b.ignoreMouse=!0});b.setButton=function(a){return new Jmol.Console.Button(this,a)};b.setVisible=function(a){a?this.container.show():this.container.hide();this.dragBind(a)};b.setTitle=function(){};b.setVisible(!1)};Jmol.Swing.setDraggable(Jmol.Console.JSConsole);
Jmol.Console.Input=function(a){this.console=a;this.id=a.id+"_input";this.getText=function(){return Jmol.$val(this.id)};this.setText=function(a){null==a&&(a="");Jmol.$val(this.id,a)};this.keyEvent=function(a){var b;b=a.type;var e=a.ctrlKey,d=a.keyCode;13==d&&(d=10);if("keyup"==b)b=38==d||40==d?1:this.console.appletConsole.processKey(d,402,e),1==(b&1)&&a.preventDefault();else{var j="keydown"==b,h=j?a.key||a.originalEvent.keyIdentifier:"";switch(d){case 38:case 40:j||(d=0);break;case 8:case 9:case 10:case 27:break;
default:d=0}b=this.console.appletConsole.processKey(d,401,e);e&&10==d&&this.setText(this.getText()+"\n");if(0==b&&9==a.keyCode){var g=this;setTimeout(function(){g.setText(g.getText()+"\t");Jmol.$focus(g.id)},10)}(1==(b&1)||"Up"==h||"Down"==h||j&&8!=a.keyCode&&32>a.keyCode)&&a.preventDefault()}};this.getCaretPosition=function(){var a=Jmol._$(this.id)[0];if("selectionStart"in a)return a.selectionStart;if(!("selection"in document))return 0;a.focus();var b=document.selection.createRange(),e=document.selection.createRange().text.length;
b.moveStart("character",-a.value.length);return b.text.length-e}};Jmol.Console.Output=function(a){this.id=a.id+"_output";this.getText=function(){return Jmol.$val(this.id)};this.setText=function(a){null==a&&(a="");Jmol.$val(this.id,a)};this.append=function(a){this.setText(this.getText()+a);Jmol.$scrollTo(this.id,-1)}};Jmol.Console.Button=function(a){this.label=a};Jmol.Console.Button.prototype.addConsoleListener=function(a){this.appletConsole=a;Jmol.Console.buttons[this.id]=this};Jmol.Console.Button.prototype.html=
function(){return'<input type="button" id="'+this.id+'" style="width:'+Jmol.Console.buttonWidth+'px" value="'+this.label+'" onClick="Jmol.Console.click(\''+this.id+"')\"/>"}})})(Clazz,Clazz.getClassName,Clazz.newLongArray,Clazz.doubleToByte,Clazz.doubleToInt,Clazz.doubleToLong,Clazz.declarePackage,Clazz.instanceOf,Clazz.load,Clazz.instantialize,Clazz.decorateAsClass,Clazz.floatToInt,Clazz.floatToLong,Clazz.makeConstructor,Clazz.defineEnumConstant,Clazz.exceptionOf,Clazz.newIntArray,Clazz.newFloatArray,
Clazz.declareType,Clazz.prepareFields,Clazz.superConstructor,Clazz.newByteArray,Clazz.declareInterface,Clazz.newShortArray,Clazz.innerTypeInstance,Clazz.isClassDefined,Clazz.prepareCallback,Clazz.newArray,Clazz.castNullAs,Clazz.floatToShort,Clazz.superCall,Clazz.decorateAsType,Clazz.newBooleanArray,Clazz.newCharArray,Clazz.implementOf,Clazz.newDoubleArray,Clazz.overrideConstructor,Clazz.clone,Clazz.doubleToShort,Clazz.getInheritedLevel,Clazz.getParamsType,Clazz.isAF,Clazz.isAB,Clazz.isAI,Clazz.isAS,
Clazz.isASS,Clazz.isAP,Clazz.isAFloat,Clazz.isAII,Clazz.isAFF,Clazz.isAFFF,Clazz.tryToSearchAndExecute,Clazz.getStackTrace,Clazz.inheritArgs,Clazz.alert,Clazz.defineMethod,Clazz.overrideMethod,Clazz.declareAnonymous,Clazz.cloneFinals);
