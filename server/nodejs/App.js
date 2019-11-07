/**
 * @author Chittaranjan Sardar
 */
 
 var express = require('express');
 var bodyParser = require('body-parser');

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
 app.use('/', express.static('./../../ui/angular-ui/dist/angular-ui', {maxAge:60*60*1000, index:'test.html'}));

 app.post('/login', (req, res)=>{

    let body = req.body;
    console.log(`/login - Received : ${body}`);
    var authenticator = new Authenticator();
    let token = authenticator.createToken(body.userId);
    res.json({body, authToken:token});

 });

 app.listen(portNumber, ()=>{

     console.log(`The Node.JS server is running at port ${portNumber}, try - http://127.0.0.1:${portNumber}/test-heart-beat`);
 });