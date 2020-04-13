import React, {Component} from 'react';
import { Redirect } from 'react-router'
import firebase from 'firebase';

class Home extends Component {

    state = {
        redirect: false
    }

    componentDidMount = () =>{
        var that = this
        firebase.auth().onAuthStateChanged(function(user) {
			if (user.emailVerified) {
				that.setState({redirect: true});
			} 
		});
    }

    render(){
        if (this.state.redirect) {
            return <Redirect to='/pooler/landing'/>;
        }
        return(
            <div>
                <p className="display-1 text-center pt-5 mt-5">Welcome to CartShare</p>
                <h4 className="text-center mt-5 font-weight-light">A verification email has been sent to you</h4>
                <h5 className="text-center mt-5 font-weight-light">Please follow the steps in the email to verify your account and then refresh this page</h5>
                

                {/* Use this if needed for verify by code */}
                <div className="row text-center mt-5">
                    <div className="col-md-4 offset-md-4">
                        <input type="text" className="form-control"/>
                    </div>
                </div>                
                <div className="row text-center mt-5">
                    <div className="col-md-4 offset-md-4">
                        <button className="btn btn-success w-50">Verify</button>
                    </div>
                </div>

            </div>
        )
    }
}
//export Home Component
export default Home;