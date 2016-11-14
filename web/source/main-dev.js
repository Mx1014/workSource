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

import createLogger from 'redux-logger'
import DevTools from './dev-tools'

import Root from './root'

bindLocaleLoader(loadLocaleResource)

//
// Application main - startRoot
//
startRoot(Root, "root", createLogger(), DevTools.instrument(), "/");
