function isUserLoggedIn(){
    fetch('user-data')
    .catch(error => "failed to fetch from user-data: " + error)
    .then(response => response.json())
    .catch(error => "failed to convert to json: " + error)
    .then(response => {
        if(response.isLoggedIn==true){
            console.log("user is logged in: " + response);
            document.getElementById("contact-form").style.display="block";
            var logoutUrl = response.logoutUrl;
            console.log("logoutUrl = " + logoutUrl);
            document.getElementById("logout-url").innerHTML = "You can change account here<a class=\"link-light\" href=\"" + logoutUrl + "\"> here</a>";
        }
        else{
            console.log("user is not logged in");
            var loginUrl = response.loginUrl;
            console.log("loginUrl = " + loginUrl);
            document.getElementById("login-info").innerHTML = "Please login<a class=\"link-emphasise\" href=\"" + loginUrl + "\"> here</a>";
        }
    });
}