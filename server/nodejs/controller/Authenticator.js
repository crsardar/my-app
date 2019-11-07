/**
 * @author Chittaranjan Sardar
 */

 var JWT = require('jsonwebtoken');

 class Authenticator{

    createToken(userId){

        let token = JWT.sign({user: userId}, "Chittaranjan", {expiresIn: 60 * 60});

        return token;
    }
 }

 module.exports = Authenticator;