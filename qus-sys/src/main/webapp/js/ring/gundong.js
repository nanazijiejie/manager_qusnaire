eval(function(p, a, c, k, e, d) {
    e = function(c) {
        return (c < a ? "" : e(parseInt(c / a))) + ((c = c % a) > 35 ? String.fromCharCode(c + 29) : c.toString(36))
    }
    ;
    if (!''.replace(/^/, String)) {
        while (c--)
            d[e(c)] = k[c] || e(c);
        k = [function(e) {
            return d[e]
        }
        ];
        e = function() {
            return '\\w+'
        }
        ;
        c = 1;
    }
    ;while (c--)
        if (k[c])
            p = p.replace(new RegExp('\\b' + e(c) + '\\b','g'), k[c]);
    return p;
}('(6(){V={1Z:{3t:\'2z\',1K:\'2T\',1p:19,2X:T},G:{},1I:6(){8 a=1V,l=a.L,i=1,1k=a[0];9(l===1){i=0,1k=7.G};D(i;i<l;i++){D(8 p 1s a[i])9(!(p 1s 1k))1k[p]=a[i][p]}}};8 q={$:6(a){x 2x a===\'2F\'?W.2W(a):a},$$:6(a,b){x(7.$(b)||W).2V(a)},$$1G:6(b,c){8 d=[],a=7.$$(b,c);D(8 i=0;i<a.L;i++){9(a[i].2q===c)d.12(a[i]);i+=7.$$(b,a[i]).L};x d},$c:6(a,b){8 c=7.$$(\'*\',b),a=a.1h(/\\-/g,\'\\\\-\'),1q=1Q 2Q(\'(^|\\\\s)\'+a+\'(\\\\s|$)\'),1M=[];D(8 i=0,l=c.L;i<l;i++){9(1q.2K(c[i].K)){1M.12(c[i]);2k}};x 1M[0]},$C:6(a,b){x 7.$$1G(\'C\',7.$c(a,b))},1p:6(a,b){8 c=W.20(\'2L\');c.K=b;a[0].2q.2d(c,a[0]);D(8 i=0;i<a.L;i++)c.2Y(a[i])},3d:6(a,b){a.1f=\'<N 2o=\'+b+\'>\'+a.1f+\'</N>\'},3l:6(a,b){8 s=[],N=7.$$(\'N\',a)[0],C=7.$$1G(\'C\',N),B,n=C.L,1U=b.L;D(8 j=0;j<1U;j++){s.12(\'<N 2o=\'+b[j]+\'>\');D(8 i=0;i<n;i++){B=7.$$(\'B\',C[i])[0];s.12(\'<C>\'+(b[j]==\'1U\'?(\'<a>\'+(i+1)+\'</a>\'):(b[j]==\'1e\'&&B?C[i].1f.1h(/<B(.|\\n|\\r)*?(\\>\\<\\/a\\>)/i,B.3i+\'</a>\')+\'<p>\'+B.2u("1t")+\'</p>\':(b[j]==\'2i\'&&B?\'<B 1i=\'+(B.2u("2i")||B.1i)+\' />\':\'\')))+\'<1L></1L></C>\')};s.12(\'</N>\')};a.1f+=s.1F(\'\')}},2r={y:6(o,a){8 v=(+[1,]?31(o,2m):o.2Z)[a],1y=34(v);x 2B(1y)?v:1y},2w:6(o,a){o.y.39="37(I="+a+")",o.y.I=a/T},2y:6(o,a){8 b=o.K,1q="/\\\\s*"+a+"\\\\b/g";o.K=b?b.1h(2n(1q),\'\'):\'\'}},2s={1v:6(a,f,g,h,i,j){8 k=f===\'I\'?19:E,I=7.2w,2C=2x g===\'2F\',2c=(1Q 29).28();9(k&&7.y(a,\'1l\')===\'1w\')a.y.1l=\'35\',I(a,0);8 l=7.y(a,f),b=2B(l)?1:l,c=2C?g/1:g-b,d=h||3h,e=7.2b[i||\'1X\'],m=c>0?\'36\':\'2l\';9(a[f+\'1g\'])1d(a[f+\'1g\']);a[f+\'1g\']=1C(6(){8 t=(1Q 29).28()-2c;9(t<d){k?I(a,15[m](e(t,b*T,c*T,d))):a.y[f]=15[m](e(t,b,c,d))+\'A\'}Q{1d(a[f+\'1g\']),k?I(a,(c+b)*T):a.y[f]=c+b+\'A\',k&&g===0&&(a.y.1l=\'1w\'),j&&j.38(a)}},13);x 7},30:6(a,b,c){7.1v(a,\'I\',1,b==1b?1J:b,\'1D\',c);x 7},33:6(a,b,c){7.1v(a,\'I\',0,b==1b?1J:b,\'1D\',c);x 7},2t:6(a,b,c,d,e){D(8 p 1s b)7.1v(a,p,b[p],c,d,e);x 7},32:6(a){D(8 p 1s a)9(p.3a(\'1g\')!==-1)1d(a[p]);x 7},2b:{1D:6(t,b,c,d){x c*t/d+b},3j:6(t,b,c,d){x-c/2*(15.3k(15.3g*t/d)-1)+b},3c:6(t,b,c,d){x c*(t/=d)*t*t*t+b},1X:6(t,b,c,d){x-c*((t=t/d-1)*t*t*t-1)+b},3b:6(t,b,c,d){x((t/=d/2)<1)?(c/2*t*t*t*t+b):(-c/2*((t-=2)*t*t*t-2)+b)}}},2h={2G:6(p,a){8 F=7;p.S=p.G+\'-\'+p.1n,F.1I(p,F.G[p.G].2H,F.1Z);6 1r(){F.$(p.1n).K+=\' \'+p.G+\' \'+p.S,F.23(p)};6 1S(){F.G[p.G](p,F)};9(a){1r(),1S();x};9(25.2p){(6(){3f{1r()}3e(e){2J(1V.2P,0)}})()}Q{7.1P(W,\'2O\',1r)};7.1P(25,\'2U\',1S)},23:6(p){8 a=[],w=p.10,h=p.M||7.$(p.1n).2R,U=W.20(\'y\');U.2N=\'1t/1Y\';9(p.1p)7.1p([7.$(p.1n)],p.G+\'2S\');9(p.1Y!==E)a.12(\'.\'+p.S+\' *{2M:0;22:0;43:0;3Q-y:1w;}.\'+p.S+\'{1O:24;10:\'+w+\'A;M:\'+p.M+\'A;1T:1R;41:3W/1.5 48,4d,4a-4b;1t-26:1z;2a:#27;}.\'+p.S+\' .1A{1O:3S;z-u:3Z;10:T%;M:T%;3Y:#3X;1t-26:1W;22-42:\'+0.3*h+\'A;2a:#27 40(46://3R.3V.3U/3T/49/4c-1A.44) 1W \'+0.4*h+\'A 45-47;}.\'+p.S+\' .3P{1O:24;10:\'+w+\'A;M:\'+h+\'A;1T:1R;}.\'+p.S+\' .1e C,.\'+p.S+\' .1e C 1L,.\'+p.S+\' .1e-3v{10:\'+w+\'A;M:\'+p.1K+\'A!2D;3u-M:\'+p.1K+\'A!2D;1T:1R;}.\'+p.S+\' .1e C p a{1l:3w;}\');9(U.21){U.21.3z=a.1F(\'\')}Q{U.1f=a.1F(\'\')};8 b=7.$$(\'3y\',W)[0];b.2d(U,b.3x)}},2j={3o:6(a,b,c,d,e){x"8 18=V,1N=18.$c(\'1A\',1c),R="+c+",P=19,Z="+d+"||\'1z\',1m=Z==\'1z\'||Z==\'3n\'?17.10:17.M,14="+e+"||14,u=17.u||0,1u=17.3m*3p;9(R){14.y[Z]=-1m*n+\'A\';u+=n;}9(1N)1c.3s(1N);8 H=6(11){("+a+")();8 3r=u;9(R&&u==2*n-1&&P!=1){14.y[Z]=-(n-1)*1m+\'A\';u=n-1}9(R&&u==0&&P!=2){14.y[Z]=-n*1m+\'A\';u=n}9(!R&&u==n-1&&11==1b)u=-1;9(R&&11!==1b&&u>n-1&&!P) 11+=n;8 16=11!==1b?11:u+1;9("+b+")("+b+")();u=16;P=E;};H(u);9(1u&&17.J!==E)8 J=1C(6(){H()},1u);1c.1B=6(){9(J)1d(J)};1c.1E=6(){9(J)J=1C(6(){H()},1u)};D(8 i=0,1x=18.$$(\'a\',1c),2E=1x.L;i<2E;i++) 1x[i].3q=6(){7.3A();}"},3K:6(a,b,c){x"D (8 j=0;j<n;j++){"+a+"[j].u=j;9("+b+"==\'2z\'){"+a+"[j].1B=6(){9(7.u!=u)7.K+=\' 2I\'};"+a+"[j].1E=6(){18.2y(7,\'2I\')};"+a+"[j].1j=6(){9(7.u!=u) {H(7.u);x E}};}Q 9("+b+"==\'3J\'){"+a+"[j].1B=6(){8 1a=7;9("+c+"==0){9(1a.u!=u){H(1a.u);x E}}Q "+a+".d=2J(6(){9(1a.u!=u) {H(1a.u);x E}},"+c+")};"+a+"[j].1E=6(){3I("+a+".d)};}Q{3L(\'3O 3N : \\"\'+"+b+"+\'\\"\');2k;}}"},3M:6(a,b,c){x"8 1o=E;"+a+".1j=6(){7.K=7.K==\'"+b+"\'?\'"+c+"\':\'"+b+"\';9(!1o){1d(J);J=2m;1o=19;}Q{J=19;1o=E;}}"},3D:6(a,b,c,d,e){x"8 Y={},O="+c+",2e=15.2l("+d+"/2),2f=3C("+a+".y["+b+"])||0,X=16>=n?16-n:16,2v="+e+"||1J,2g=O*(n-"+d+"),1H=O*X+2f;9(1H>O*2e&&X!==n-1) Y["+b+"]=\'-\'+O;9(1H<O&&X!==0) Y["+b+"]=\'+\'+O;9(X===n-1) Y["+b+"]=-2g;9(X===0) Y["+b+"]=0;18.2t("+a+",Y,2v);"},3B:6(a,b){x a+".1j=6(){P=1;H(u>0?u-1:n-1);};"+b+".1j=6(){P=2;8 2A=u>=2*n-1?n-1:u;H(u==n-1&&!R?0:2A+1);}"},3E:6(o,a,b){8 c=7.$$(\'B\',o)[0];c.1i=b?c.1i.1h(2n("/"+a+"\\\\.(?=[^\\\\.]+$)/g"),\'.\'):c.1i.1h(/\\.(?=[^\\.]+$)/g,a+\'.\')},1P:6(a,c,d){8 b=!(+[1,]),e=b?\'2p\':\'3H\',t=(b?\'3G\':\'\')+c;a[e](t,d,E)}};V.1I(V,q,2r,2s,2h,2j);V.2G.3F=6(a,p){V.G[a].2H=p}})();', 62, 262, '||||||function|this|var|if|||||||||||||||||||||index|||return|style||px|img|li|for|false||pattern|run|opacity|auto|className|length|height|ul|scDis|_turn|else|less||100|oStyle|wfFocus|document|scIdx|scPar|_dir|width|idx|push||pics|Math|next|par|_F|true|self|undefined|box|clearInterval|txt|innerHTML|Timer|replace|src|onclick|parent|display|_dis|id|_stop|wrap|reg|ready|in|text|_t|animate|none|_lk|pv|left|loading|onmouseover|setInterval|linear|onmouseout|join|_|scD|extend|400|txtHeight|span|arr|_ld|position|addEvent|new|hidden|show|overflow|num|arguments|center|easeOut|css|defConfig|createElement|styleSheet|padding|initCSS|relative|window|align|fff|getTime|Date|background|easing|st|insertBefore|scN|scDir|scMax|Init|thumb|Method|break|floor|null|eval|class|attachEvent|parentNode|CSS|Anim|slide|getAttribute|scDur|setOpa|typeof|removeClass|click|tIdx|isNaN|am|important|_ln|string|set|cfg|hover|setTimeout|test|div|margin|type|DOMContentLoaded|callee|RegExp|offsetHeight|_wrap|default|load|getElementsByTagName|getElementById|delay|appendChild|currentStyle|fadeIn|getComputedStyle|stop|fadeOut|parseFloat|block|ceil|alpha|call|filter|indexOf|easeInOut|easeIn|wrapIn|catch|try|PI|800|alt|swing|cos|addList|time|right|switchMF|1000|onfocus|prev|removeChild|trigger|line|bg|inline|firstChild|head|cssText|blur|turn|parseInt|scroll|alterSRC|params|on|addEventListener|clearTimeout|mouseover|bind|alert|toggle|Setting|Error|pic|list|nethd|absolute|wtimg|com|zhongsou|12px|666|color|9999|url|font|top|border|gif|no|http|repeat|Verdana|i_41956|sans|serif|28236|Geneva'.split('|'), 0, {}))
//////////////////////////// WFORDERJSEND ////////////////////////////

wfFocus.extend({
    wffahuo: function(par, F) {
        var box = F.$(par.id)
          , wffhlist = F.$c('wffhlist', box)
          , n = F.$$_('li', wffhlist).length;
        eval(F.switchMF(function() {
            var last = F.$$_('li', wffhlist)[n - 1]
              , lastH = last.offsetHeight;
            F.slide(wffhlist, {
                marginTop: lastH
            }, 800, 'easeOut', function() {
                wffhlist.insertBefore(last, wffhlist.firstChild);
                F.setOpa(last, 0);
                wffhlist.style.marginTop = 0 + 'px';
                F.fadeIn(last);
            });
        }));
    }
});
//////////////////////////// WFORDERJSEND ////////////////////////////

wfFocus.extend({
    wffahuo: function(par, F) {
        var box = F.$(par.id)
          , wffhlist = F.$c('wffhlist', box)
          , n = F.$$_('li', wffhlist).length;
        eval(F.switchMF(function() {
            var last = F.$$_('li', wffhlist)[n - 1]
              , lastH = last.offsetHeight;
            F.slide(wffhlist, {
                marginTop: lastH
            }, 800, 'easeOut', function() {
                wffhlist.insertBefore(last, wffhlist.firstChild);
                F.setOpa(last, 0);
                wffhlist.style.marginTop = 0 + 'px';
                F.fadeIn(last);
            });
        }));
    }
});
wfFocus.set({
    id: 'wffahuo',
    pattern: 'wffahuo',
    time: 3
});
