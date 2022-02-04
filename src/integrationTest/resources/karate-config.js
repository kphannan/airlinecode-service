

function() {
	var env = karate.env; // get java system property 'karate.env'

	karate.log( 'karate.env system property was:', env );

	if ( !env ) {
		env = 'local'; // a custom 'intelligent' default
	}

	var config = { // base config JSON
		appId: 'my.app.id',
		appSecret: 'my.secret',
		rootPath: '/airline',
		baseUrl: 'http://localhost:8080/killroy/was/here'
	};

	const hosts = new Map([
		 [ 'local',    'http://localhost:8080' ]
		,[ 'dev',      'http://localhost:8080' ]
		,[ 'qa',       'http://qa:8080' ]
		,[ 'int',      'http://integration:8080' ]
		,[ 'preprod',  'http://preprod:8080' ]
		,[ 'demo',     'http://sales:8080' ]
		,[ 'prod',     'http://domain:8080' ]
	]);

	activeHost = hosts.get( env );
	config.baseUrl = activeHost + config.rootPath;

	// don't waste time waiting for a connection or if servers don't respond within 5 seconds
	karate.configure('connectTimeout', 5000);
	karate.configure('readTimeout', 5000);

	return config;
}