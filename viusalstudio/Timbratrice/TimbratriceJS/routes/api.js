var express = require('express');
var router = express.Router();

router.get('/', function(req, res, next) {
	res.end('respond with a resource');
});

router.get('/ping', function(req, res, next) {
	res.end('pong');
});

router.get('/monitoraggio', loggedIn, function(req, res, next) {
	console.log(req.user);
	res.render('monitoraggio', {
		title : 'SCM Studio - Registrazione Ingressi'
	});
});

function loggedIn(req, res, next) {
	if (req.user) {
		next();
	} else {
		var redirUrl = req.originalUrl || req.url;
		console.log('requested ' + redirUrl + ' from unauthenticated user. redirecting to login page...');
		req.session.returnTo = redirUrl;
		res.redirect('/login');
	}
}

module.exports = router;
