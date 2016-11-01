const path = require('path')
const webpack = require('webpack')
const ExtractTextPlugin = require('extract-text-webpack-plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')

module.exports = {
    entry: {
        app: "./source/main.js",

        vendor: [
            'react',
            'react-dom',
            'react-redux',
            'react-router',
            'react-router-redux',
            'redux',
            'redux-thunk'
        ],
    },

    output: {
        path: path.join(__dirname, 'dist'),
        filename: "admin-bundle.js"
    },

    module: {
        loaders: [
            {
                test: [
                    path.join(__dirname, "source"),
                    path.join(__dirname, "local")
                ],
                loader: 'babel',
                query: {
                    presets: ['react', 'es2015', 'stage-0'],
                    plugins: ['transform-runtime', 'transform-decorators-legacy']
                }
            },

            {
                test: /\.css$/,
                loader: ExtractTextPlugin.extract(
                    'style-loader',
                    'css-loader?modules&importLoaders=1&localIdentName=[name]__[local]___[hash:base64:5]!postcss-loader')
            },

            {
                test: /\.svg$/,
                loader: "url-loader?limit=10000&mimetype=image/svg+xml"
            },

            {
                test: /\.(jpe?g|png|gif|svg)$/i,
                loaders: [
                    'file?hash=sha512&digest=hex&name=[hash].[ext]',
                    'image-webpack?bypassOnDebug&optimizationLevel=7&interlaced=false'
                ]
            }
        ]
    },

    postcss: [
        require('autoprefixer-core'),
        require('postcss-color-rebeccapurple')
    ],

    resolve: {
        modulesDirectories: ['node_modules', 'local/node_modules']
    },

    devServer: {
        historyApiFallback: true,
        proxy: {
            '/novae': {
                target: 'http://localhost:8080',
                secure: false
            }
        }
    },

    plugins: [
        new ExtractTextPlugin('style.css', { allChunks: true }),
        new webpack.DefinePlugin({ 'process.env.NODE_ENV': '"production"' }),
        new webpack.optimize.DedupePlugin(),
        new webpack.optimize.UglifyJsPlugin({minimize: true}),
        new webpack.optimize.CommonsChunkPlugin('vendor', 'vendor-bundle.js'),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: './index-template.html',
            inject: 'body'
        })
    ]
}
