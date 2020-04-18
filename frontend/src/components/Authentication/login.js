import React, {Component} from 'react';
import { Redirect } from 'react-router'
import firebase from 'firebase';
import StyledFirebaseAuth from 'react-firebaseui/StyledFirebaseAuth';
import '../../css/login.css'

class Login extends Component {
    state = {redURL : "/pooler/landing", signInDone: false, transitionClass: 'init', transitionClass1: 'init1'};
    uiConfig = {
        callbacks: {
          signInSuccessWithAuthResult: (authResult, redirectUrl) => {
            // User successfully signed in.
            // Return type determines whether we continue the redirect automatically
            // or whether we leave that to developer to handle.
            if(authResult.additionalUserInfo.isNewUser){
                this.setState({redURL : "/verify", signInDone: true});
                authResult.user.sendEmailVerification().then(function() {
                    // Email sent.
                  }).catch(function(error) {
                    // An error happened.
                  });
            }
            this.setState({signInDone: true});

            return false;
          }
        },
        // Will use popup for IDP Providers sign-in flow instead of the default, redirect.
        signInFlow: 'popup',
        signInSuccessUrl: '/verify',
        signInOptions: [
          // Leave the lines as is for the providers you want to offer your users.
          firebase.auth.GoogleAuthProvider.PROVIDER_ID,
          firebase.auth.FacebookAuthProvider.PROVIDER_ID,
          firebase.auth.EmailAuthProvider.PROVIDER_ID
        ],
    };

    componentDidMount(){
      this.timeoutId = setTimeout(function () {
          this.setState({transitionClass: 'final', transitionClass1: 'final1'});
      }.bind(this), 500);
    }

    render(){
        if(this.state.signInDone === true){
            return <Redirect to={this.state.redURL}/>;
        }
        return(
            <div className={this.state.transitionClass1}>
                <p className="display-1 text-center pt-5 mt-5">Login to CartShare</p>
                <div className={this.state.transitionClass}>
                  <StyledFirebaseAuth
                      uiConfig={this.uiConfig}
                      firebaseAuth={firebase.auth()}
                  />
                </div>
            </div>
        )
    }
}
//export Login Component
export default Login;