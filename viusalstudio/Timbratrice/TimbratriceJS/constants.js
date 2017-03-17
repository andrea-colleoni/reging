/**
 * Program constants
 */
function define(name, value) {
    Object.defineProperty(exports, name, {
        value:      value,
        enumerable: true
    });
}

// database
define("mysqlHost", "localhost");
define("mysqlPort", 3307);
define("mysqlUser", "reging");
define("mysqlPassword", "reging");
define("mysqlSchema", "reging");

define("wsHome", "localhost:8080");
