<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name='layout' content='main'/>
</head>

<body>

<div class="tweets">
    <h4>The latest tweets about Vancouver: </h4>
    <g:if test="${error}">
        <div class="tweet-error">
            ${error}
        </div>
    </g:if>
    <g:else>
        <g:each in="${data}">
            <div class="tweet">
                <div class="who"><g:img uri="${it.image}"/></div>
                <div class="tweet-details">
                    <div class="what">${it.text}</div>
                    <div class="when">${it.created}</div>
                </div>
            </div>
        </g:each>
        <div class="tweets">
    </g:else>
</body>
</html>