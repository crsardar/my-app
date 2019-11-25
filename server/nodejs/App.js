/**
 * @author Chittaranjan Sardar
 */
 
var express = require('express');
var bodyParser = require('body-parser');

var fs = require('fs');

var http = require('http');
var https = require('https');

var credentials = {
    key: fs.readFileSync('server.key'),
    cert: fs.readFileSync('server.cert')
};

 var tls = require('tls')

 var Authenticator = require('./controller/Authenticator')

 var app = express();

let portNumber = 8080;

app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());

 app.get('/test-heart-beat', (req, res)=>{

     res.json({
         component: 'server/nodejs',
         message: "Don't worry, I am rocking!"
         });
 });

 // Home Page
 app.use('/', express.static('./../../ui/angular-ui/dist/angular-ui', {maxAge:60*60*1000, index:'index.html'}));

 // Generate Token
 app.post('/generateToket', (req, res)=>{

    let body = req.body;
 
    console.log("/generateToket - Received : " + JSON.stringify(body));
 
    var authenticator = new Authenticator();
    let token = authenticator.createToken(body.userId);
    res.json({body, authToken:token});
 });

 // Verify Token
 app.post('/verifyToken', (req, res)=>{

    let body = req.body;
 
    console.log("/verifyToken - Received : " + JSON.stringify(body));

    var authToken = body.authToken;
    var authenticator = new Authenticator();
    var result = authenticator.verifyToken(authToken);
    body.message = 'Verification Result = ' + result;
    res.send(body);
 });

//  app.listen(portNumber, ()=>{

//      console.log(`The Node.JS server is running at port ${portNumber}, try - http://127.0.0.1:${portNumber}/test-heart-beat`);
//  });

var httpServer = http.createServer(app);
var httpsServer = https.createServer(credentials, app);

httpServer.listen(8080);
httpsServer.listen(8443);