/**
 * @author Chittaranjan Sardar
 */
 
 var express = require('express');

 var app = express();

let portNumber = 8080;

 app.get('/test-heart-beat', (req, res)=>{

     res.json({
         component: 'server/nodejs',
         message: "Don't worry, I am rocking!"
         });
 });

 app.listen(portNumber, ()=>{

     console.log(`The Node.JS server is running at port ${portNumber}, try - http://127.0.0.1:${portNumber}/test-heart-beat`);
 });