import React, { Component } from 'react';
import { Redirect } from 'react-router'
import '../../css/login.css'

class Logout extends Component {

	render() {

		localStorage.removeItem('275UserId');
		localStorage.removeItem('275NickName');
		localStorage.removeItem('275UserType');
		return (<Redirect to="/login" />)
	}
}
//export Logout Component
export default Logout;