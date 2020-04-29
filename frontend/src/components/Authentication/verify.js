import React, { Component } from 'react';
import { Redirect } from 'react-router'
// import firebase from 'firebase';
import axios from 'axios';

class Verify extends Component {

    state = {
        redirect: false,
        redUrl: '/user-information',
        message: 'A verification email has been sent to you',
        code: '0001'
    }

    componentDidMount = () =>{
        var id = localStorage.getItem('275UserId');
        var screenName = localStorage.getItem('275NickName');
        console.log("id", id);
        console.log("sname", screenName);
        
        if(id != null && screenName == null){
            console.log("in verify else");
            var uri = '/user/'.concat(localStorage.getItem('275UserId'));
            axios.get(uri)
            .then((response) => {
                if(response.status === 200){
                    this.setState({code: response.data.verificationCode});
                }
            })
            .catch((error) => {
                console.log("Error in verify-get catch");
            })
        }
        else if(screenName != null){
            this.setState({redUrl: '/pooler/landing', redirect: true});
        }
        else{
            console.log("user not there");
            this.setState({redUrl: '/login', redirect: true});
        }
    }

    resendVerification = () => {
        var id = localStorage.getItem('275UserId');
        var uri = '/user/'.concat(id).concat('/sendVerification');
        axios.post(uri, null, null)
        .then((response1) => {
            if (response1.status === 200) {
                // this.setState({code: response1.data});
                // localStorage.setItem('verifCode', response1.data);
                this.setState({message: 'A second verification email has been sent to you' });
                this.setState({ code: response1.data.toString() });
            }
        })
        .catch((error) => {
            this.setState({ message: "Please wait for 5 minutes and click on the 'Resend verification' button again" });
        })
    }

    handleChange = (event) => {
        var c = this.state.code;
        if(c.localeCompare(event.target.value) === 0){
            var uri = '/user/'.concat(localStorage.getItem('275UserId'));
            var isadmin = localStorage.getItem('275UserType').localeCompare("Admin") === 0 ? true : false;
            axios.put(uri, null, {
                params: {
                    email: localStorage.getItem('275UserEmail'),
                    nickName: 'notSet',
                    screenName: 'notSet',
                    isAdmin: isadmin,
                    isVerified: true,
                    isActive: true,
                    isProfileComplete: false,
                    city: "",
                    street: "",
                    state: "",
                    zipcode: "00000"
                }
            })
            .then((response) => {
                if (response.status === 200) {
                    this.setState({ redirect: true });
                }
            })
            .catch((error) => {
                console.log('error in catch');
                
                alert(error.response);
            })
        }
    }

    render() {
        if (this.state.redirect) {
            return <Redirect to={this.state.redUrl} />;
        }
        return (
            <div>
                <p className="display-1 text-center pt-5 mt-5">Welcome to CartShare</p>
                <h4 className="text-center mt-5 font-weight-light">{this.state.message}</h4>
                <h5 className="text-center mt-5 font-weight-light">Please check your email, and enter the 4-digit verification code sent to you</h5>


                {/* Use this if needed for verify by code */}
                <div className="row text-center mt-5">
                    <div className="col-md-4 offset-md-4">
                        <input type="text" className="form-control" onChange={this.handleChange} />
                    </div>
                </div>
                <div className="row text-center mt-5">
                    <div className="col-md-4 offset-md-4">
                        <button className="btn btn-success w-50" onClick={this.resendVerification}>Resend verification</button>
                    </div>
                </div>
                {/* <div className="row text-center mt-5">
                    <div className="col-md-4 offset-md-4">
                        <button className="btn btn-success w-50" onClick={this.verify}>Verify</button>
                    </div>
                </div> */}

            </div>
        )
    }
}
//export Verify Component
export default Verify;