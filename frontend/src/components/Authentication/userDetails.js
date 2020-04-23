import React, { Component } from 'react';
import axios from 'axios';
// import firebase from 'firebase';
import { Redirect } from 'react-router';

class UserDetails extends Component {
    
    constructor() {
        super()
        this.state = {
            nickName: "",
            screenName: "",
            redURL: "",
            redirect: false
        }
    }

    componentWillMount = () => {

        var id = localStorage.getItem('275UserId');
        var screenName = localStorage.getItem('275NickName');
        console.log("id", id);
        
        console.log('nickname', screenName);
        if(id === null && screenName == null){
            console.log("user not there");
            this.setState({redUrl: '/login', redirect: true});
        }
        if(screenName != null){
            this.setState({redUrl: '/pooler/landing', redirect: true});
        }
        // else{
        // console.log("user there!");
        
        // var uri = '/user/'.concat();
            // axios.get();
        // }
    }

    nickNameChangeHandler = (e) => {
        this.setState({
            nickName: e.target.value
        })
    }

    screenNameChangeHandler = (e) => {
        this.setState({
            screenName: e.target.value
        })
    }

    updateInformation = () => {
        if(this.state.nickName.length === 0 || this.state.screenName.length === 0){
            alert("Nickname and Screenname can't be empty");
            return;
        }
        if(this.state.nickName.localeCompare('notSet') === 0){
            alert("NickName is already taken");
            return;
        }
        if(this.state.screenName.localeCompare('notSet') === 0){
            alert("ScreenName is already taken");
            return;
        }
        var id = localStorage.getItem('275UserId');
        var uri = '/user/'.concat(id);
        var email = localStorage.getItem('275UserEmail');
        var isadmin = email.includes("@sjsu.edu");
        axios.put(uri, null, {
            params: {
                email: email,
                nickName: this.state.nickName,
                screenName: this.state.screenName,
                isAdmin: isadmin,
                isVerified: true,
                isActive: localStorage.getItem('275UserIsActive'),
                isProfileComplete: true
            }
        })
        .then((res) => {
            if(res.status === 200){
                localStorage.setItem('275NickName', res.data.nickName);
                this.setState({redirect: true, redURL: "/pooler/landing"});
            }
        })
        .catch((error) => {
            alert("screen name or nick name is not unique");
        })
        // var user = firebase.auth().currentUser;
        // var isadmin = user.email.includes("@sjsu.edu");
        // axios.post('/user', null, { // create user in backend
        //     params: {
        //         uid: user.uid,
        //         email: user.email,
        //         nickName: this.state.nickName,
        //         screenName: this.state.screenName,
        //         isAdmin: isadmin,
        //         isVerified: true,
        //         isActive: true,
        //         isProfileComplete: true
        //     }
        // })
        // .then((response) => {
        //     localStorage.setItem('275UserId', response.data.id)
        //     localStorage.setItem('275UserName', response.data.screenName)
        //     if (response.data.isAdmin) {
        //         localStorage.setItem('275UserType', "Admin")
        //     } else {
        //         localStorage.setItem('275UserType', "Pooler")
        //     }
        //     if(response.status === 200){
        //         // route to landing
        //         alert("user created in backend");
        //         this.setState({
        //             redURL: "/pooler/landing",
        //             redirect: true
        //         })
        //         return;
        //     }
        // })
        // .catch((error) => {
        //     alert(error);
        // });

        // route to 


        // console.log("current email", firebase.auth().currentUser.email);
        

    }

    render() {

        if(this.state.redirect === true){
            return <Redirect to={this.state.redURL}/>;
        }

        return (
            <div>
                <p className="display-1 text-center pt-5 mt-5">Your account has been verified</p>
                <h5 className="text-center mt-5 font-weight-light">Please enter your details before we can continue</h5>


                <div className="row text-center mt-5">
                    <div className="col-md-4 offset-md-4">
                        <div className="form-group">
                            <label>Screen name</label>
                            <input className="form-control" type="text" value={this.state.screenName} onChange={this.screenNameChangeHandler} placeholder="Screen name has to be numeric" />
                        </div>
                        <div className="form-group">
                            <label>Nick name</label>
                            <input className="form-control" type="text" value={this.state.nickName} onChange={this.nickNameChangeHandler} />
                        </div>
                        <button className="btn btn-success w-50" onClick={this.updateInformation}>Update details</button>
                    </div>
                </div>

            </div>
        )
    }
}
//export UserDetails Component
export default UserDetails;
