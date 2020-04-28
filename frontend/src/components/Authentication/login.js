import React, { Component } from 'react';
import { Redirect } from 'react-router'
import firebase from 'firebase';
import StyledFirebaseAuth from 'react-firebaseui/StyledFirebaseAuth';
import '../../css/login.css'
import axios from 'axios';

class Login extends Component {
	state = { redURL: "/pooler/landing", signInDone: false, transitionClass: 'init', transitionClass1: 'init1' };
	uiConfig = {
		callbacks: {
			signInSuccessWithAuthResult: (authResult, redirectUrl) => {
				// User successfully signed in.
				// Return type determines whether we continue the redirect automatically
				// or whether we leave that to developer to handle.
				localStorage.clear();
				console.log("authres", authResult);
				var user = authResult.user;
				if (authResult.additionalUserInfo.isNewUser) {
					var isadmin = user.email.includes("@sjsu.edu");
					axios.post('/user', null, { // create user in backend
						params: {
							uid: user.uid,
							email: user.email,
							nickName: 'notSet',
							screenName: 'notSet',
							isAdmin: isadmin,
							isVerified: false,
							isActive: true,
							isProfileComplete: false
						}
					})
						.then((response) => {
							if (response.status === 200) {
								localStorage.setItem('275UserId', response.data.id);
								localStorage.setItem('275UserEmail', response.data.email);
								localStorage.setItem('275UserIsActive', response.data.active);
								localStorage.setItem('275UserName', response.data.screenName)
								if (response.data.isAdmin) {
									localStorage.setItem('275UserType', "Admin")
								} else {
									localStorage.setItem('275UserType', "Pooler")
								}
								this.setState({ redURL: "/verify", signInDone: true });
							}
						})
						.catch((error) => {
							alert(error.response.data);
						});
				}
				else {
					var uri = '/user/uid/'.concat(user.uid);
					axios.get(uri)
						.then((response) => {
							if (response.status === 200) {
								localStorage.setItem('275UserId', response.data.id);
								localStorage.setItem('275UserEmail', response.data.email);
								localStorage.setItem('275UserIsActive', response.data.active);
								localStorage.setItem('275UserName', response.data.screenName)
								if (response.data.isAdmin) {
									localStorage.setItem('275UserType', "Admin")
								} else {
									localStorage.setItem('275UserType', "Pooler")
								}
								if (response.data.verified) {
									localStorage.setItem('275Verified', true);
									if (response.data.profileComplete) {
										localStorage.setItem('275NickName', response.data.nickName);
										this.setState({ redURL: "/pooler/landing", signInDone: true });
									}
									else {
										this.setState({ redURL: "/user-information", signInDone: true });
									}
								}
								else {
									this.setState({ redURL: "/verify", signInDone: true });
								}
							}
						})
						.catch((error) => {
							alert(error.response.data);
						});
				}

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

	componentDidMount() {
		this.timeoutId = setTimeout(function () {
			this.setState({ transitionClass: 'final', transitionClass1: 'final1' });
		}.bind(this), 500);
		localStorage.clear();
	}

	render() {
		if (this.state.signInDone === true) {
			return <Redirect to={this.state.redURL} />;
		}
		return (
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