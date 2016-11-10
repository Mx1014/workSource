//http://gywbd.github.io/posts/2014/11/using-exports-nodejs-interface-design-pattern.html

function App(name) {
    this.name = name;
    this.maps = {};
    this.queries = {};
}

module.exports = App;
