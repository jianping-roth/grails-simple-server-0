<!doctype html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><g:layoutTitle default="Jianping's simple grails server demo "/></title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">

    <r:require module="application"/>

    <g:layoutHead/>
    <r:layoutResources />
    <!--[if lt IE 9]>
        <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
</head>
<body>
<div id="loginlogout">
    <sec:ifLoggedIn>
        <sec:username/> -- <g:link controller='logout'>Log Out</g:link>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
        <g:link controller='login' action='auth'>Login</g:link>
    </sec:ifNotLoggedIn>
</div>
<header>
    <g:link controller="public" action="index">Jianping's simple server demo</g:link>
</header>
<div id="main">
    <g:layoutBody/>
</div>
<footer>
&copy; 2013 Example Corporation. Licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache License, Version 2.0</a>.
</footer>
<r:layoutResources />
</body>
</html>