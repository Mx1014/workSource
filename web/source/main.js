/**
 *
 *  Standard React-Wdiget bootstrap main sequence
 *
 *  @Author Kelven Yang
 *
 */

import {startRoot} from 'widget-redux-util/redux-enhancer'
import {bindLocaleLoader} from 'widget-common-util/locale'
import {loadLocaleResource} from './locale/resource-loader'

import Root from './root'

bindLocaleLoader(loadLocaleResource)

let base = "/";
if (process.env.NODE_ENV === 'production') {
    let url = document.location.toString();

    let startPos = url.indexOf("/api");
    if(startPos > 0) {
        let endPos = startPos;
        startPos = url.lastIndexOf("/", startPos - 1);

        if(endPos >= startPos)
            base = url.substring(startPos, endPos + 4);
        else
            base = "/";

        console.log(`Base ${base}`);
    }
}

//
// Application main - startRoot
//
startRoot(Root, "root", [], [], base);
