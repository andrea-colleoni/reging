var express = require('express');
var request = require('request');
var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;

passport.use(new LocalStrategy(function(username, password, done) {
	request.post({
		url : 'http://localhost:8080/reging/dipendente/login',
		form : {
			username : username,
			password : password
		}
	}, function(err, httpResponse, body) {
		if (err) {
			console.error(err);
			return done(err);
		} else {
			if (httpResponse.statusCode == 200) {
				return done(null, body);
			} else {
				console.error(httpResponse);
				return done(null, false, {
					message : 'Incorrect credentials.'
				});
			}
		}
	});
}));

passport.serializeUser(function(user, done) {
	var userObj = JSON.parse(user);
	done(null, userObj.id);
});

passport.deserializeUser(function(id, done) {
	request.get({
		url : 'http://localhost:8080/reging/dipendente/' + id
	}, function(err, httpResponse, body) {
		if (httpResponse.statusCode == 200) {
			var userObj = JSON.parse(body);
			done(err, {
				id : userObj.id,
				username : userObj.username,
				nome : userObj.nome,
				cognome : userObj.cognome
			});
		} else {
			done(err, null);
		}
	});
});

var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
	res.render('index', {
		title : 'SCM Studio - Registrazione Ingressi'
	});
});

router.get('/rilevazione', function(req, res, next) {
	res.render('rilevazione', {
		title : 'SCM Studio - Registrazione Ingressi'
	});
});

// =====================================
// LOGIN ===============================
// =====================================
// show the login form
router.get('/login', function(req, res, next) {
	res.render('login', {
		title : 'SCM Studio - Registrazione Ingressi'
	});
});

// process the login form
router.post('/login', function(req, res, next) {
	passport.authenticate('local', function(err, user, info) {
		if (err) {
			return next(err);
		}
		if (!user) {
			return res.redirect('/login');
		}
		req.logIn(user, function(err) {
			if (err) {
				return next(err);
			}
			return res.redirect(req.session.returnTo || '/');
		});
	})(req, res, next);
});

// =====================================
// LOGOUT ==============================
// =====================================
router.get('/logout', function(req, res) {
	req.logout();
	res.redirect('/');
});

module.exports = router;
