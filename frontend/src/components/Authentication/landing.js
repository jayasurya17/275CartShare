import React, { Component } from 'react';
import { Redirect } from 'react-router';

class Root extends Component {
	render() {
		if (this.props.location.pathname === '/') {
			return(<Redirect to="/login" />)
		}
		return (null);
	}
}
// export Root Component
export default Root;
