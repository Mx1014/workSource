import {API_LIST_SUCCESS, SANDBOX_SET_CURRENT} from '../actions'
import {setSandboxCurrentApi} from '../actions'

function getInitState() {
    return ({
        currentApi: null,
        navItems: [],
        apis: []
    });
}

String.prototype.capitalize = function() {
    return this.replace(
        /(^|\s)([a-z])/g,
        (m, p1,p2) => {return p1+p2.toUpperCase();}
    );
}

function groupApiList(items) {
    let groupedList = {};

    items.reduce((v, item)=>{
        let tokens = item.uri.split('/');

        let sectionName = tokens[1];
        sectionName = sectionName.capitalize() + ' API';

        let itemName;
        if(tokens.length < 3)
            itemName = tokens[1];
        else
            itemName = tokens.slice(2).join('/');

        let sectionItem = {
            description: itemName,
            uri: item.uri,
            action: setSandboxCurrentApi(item.uri)
        };

        if(!!groupedList[sectionName]) {
            groupedList[sectionName].sectionItems.push(sectionItem);
        } else {
            groupedList[sectionName] = {
                sectionName,
                sectionItems: [
                    sectionItem
                ]
            };
        }

    }, groupedList);

    let result = Object.keys(groupedList).map(key => groupedList[key]);
    console.log(JSON.stringify(result));
    return result
}

export default function apiNavigationReducer(state = getInitState(), action) {
    switch(action.type) {
        case API_LIST_SUCCESS:
            return {...state, navItems: groupApiList(action.response.response), apis: action.response.response};

        case SANDBOX_SET_CURRENT: {
            let item = state.apis.find((e) => { return e.uri == action.uri});

            return {...state, currentApi: item};
        }
    }
    return state;
}
