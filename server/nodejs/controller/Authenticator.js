/**
 * @author Chittaranjan Sardar
 */

var fs = require('fs');
var JWT = require('jsonwebtoken');

var privateKey = fs.readFileSync('./my_key', 'utf8');
var publicKey = fs.readFileSync('./my_key.pub', 'utf8');

var jwtOptions = {

    issuer : 'Chittaranjan Sardar',
    subject: 'JWT Cross Platforms',
    audience: 'https://github.com/crsardar',
    expiresIn: '5m',
    algorithm: "RS256"
};

class Authenticator{

    createToken(userId){

        let token = JWT.sign({user: userId}, privateKey, jwtOptions);

        console.log("createToken : " + token);

        return token;
    }

    verifyToken(token){

        try{
            
            var verifyResult = JWT.verify(token, publicKey, jwtOptions);
            return "Verification has passed : " + JSON.stringify(verifyResult);

        }catch(err){
            var errorMsg = 'Failed in verification : ' + err;
            console.log(errorMsg);
            return errorMsg;
        }
    }
}

module.exports = Authenticator;