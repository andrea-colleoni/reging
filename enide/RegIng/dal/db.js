/**
 * DB interactions v 1.0
 */
var constants = require("../constants");

var mysql   = require('mysql');
var pool  	= mysql.createPool({
	  connectionLimit : 100,
	  host     : constants.mysqlHost,
	  user     : constants.mysqlUser,
	  port	   : constants.mysqlPort,
	  password : constants.mysqlPassword,
	  database : constants.mysqlSchema
});



module.exports = {
	pool: pool
};