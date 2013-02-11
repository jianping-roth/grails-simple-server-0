<html>
<head>
    <meta name='layout' content='main'/>
    <title>Login</title>
</head>

<body>

<div class="login-page">
    <div id="login-form-container">
        <div class="logo-container">
            <img src='<g:resource dir="images" file="grails_logo.png"/>' alt="Simple grails server"/>
        </div>

        <form id="login-form" method="post" action="${postUrl}" class="test">
            <div id="login-details-container">
                <div>
                    <input id="username" type="text" name="j_username" placeholder="Username" maxlength="75"
                           tabindex="1" autocomplete="off" title="Enter 'admin'"/>
                </div>

                <div>
                    <input id="password" type="password" name="j_password" placeholder="Password" tabindex="2"
                           autocomplete="off" title="Enter 'pass'"/>
                </div>

                <div id="remember_me_holder">
                    <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
                           <g:if test='${hasCookie}'>checked='checked'</g:if>/>
                    <label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label>
                </div>
                <input id="sign-in" type="submit" value="Sign In" tabindex="3"/>
            </div>

            <g:if test="${flash.message}">
                <div class="error-msg">
                    ${flash.message}
                </div>
            </g:if>
        </form>
    </div>
</div>


<script type='text/javascript'>
    <!--
    (function () {
        debugger;
        document.forms[0].elements['j_username'].focus();
    })();
    // -->
</script>
</body>
</html>