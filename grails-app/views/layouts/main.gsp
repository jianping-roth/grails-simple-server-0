<!doctype html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><g:layoutTitle default="Jianping's simple grails server demo "/></title>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"
          media="all"/>
    <r:require module="application"/>

    <g:layoutHead/>
    <r:layoutResources/>
    <!--[if lt IE 9]>
        <script src="//html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
</head>

<body>
<div id="container">
    <div id="top-nav-area">
        <div id="loginout">
            <sec:ifLoggedIn>
                <sec:username/>  <g:link controller='logout'>Log Out</g:link>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
                <g:link controller='login' action='auth'>Login</g:link>
            </sec:ifNotLoggedIn>
        </div>

        <div id="public-service">
            <header>
                <g:link controller="public" action="index">Jianping's public services.</g:link>
            </header>
        </div>
    </div>

    <div id="main">
        <g:layoutBody/>
    </div>
</div>
<footer>
    <div id="footer" class="container">
        <div class="holder">
            <ul>
                <li><a href="http://grails.org//">Learn more about Grails</a></li>
                <li><a href="https://twitter.com/jianpingroth">Jianping Roth on Twitter</a></li>
            </ul>
        </div>
    </div>
</footer>
<r:layoutResources/>
</body>
</html>