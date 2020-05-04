import React, { Component } from 'react';
import axios from 'axios';
// import firebase from 'firebase';
import { Redirect } from 'react-router';
// import mapbox from '../../utils/mapbox';

class UserDetails extends Component {
    
    constructor() {
        super()
        this.state = {
            nickName: "",
            screenName: "",
            redURL: "",
            redirect: false,
            city: "",
            state: "",
            street: "",
            zipcode: ""
        }
    }

    componentWillMount = async () => {

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
    streetChangeHandler = (e) => {
        this.setState({
            street: e.target.value
        })
    }
    cityChangeHandler = (e) => {
        this.setState({
            city: e.target.value
        })
    }
    stateChangeHandler = (e) => {
        this.setState({
            state: e.target.value
        })
    }
    zipcodeChangeHandler = (e) => {
        this.setState({
            zipcode: e.target.value
        })
    }

    updateInformation = async () => {
        if(this.state.nickName.length === 0 || this.state.screenName.length === 0 || this.state.street.length === 0 || this.state.city.length === 0 || this.state.state.length === 0 || this.state.zipcode.length === 0){
            alert("None of the fields should be empty");
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

        if(this.state.zipcode.length !== 5){
            alert("Please enter a valid zipcode");
            return;
        }
        // var q = 0;
        var response = await axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${this.state.street}, ${this.state.state}, ${this.state.city}, ${this.state.zipcode}.json?access_token=pk.eyJ1Ijoic2hpdmFuZGVzYWkiLCJhIjoiY2syaW0xaXllMGcydTNjb2hua3UzbHpyMSJ9.ZHxE6FAsU4GeDvz4WH9AhA`)
        // .then((response) => {
        console.log('features', response.data.features);
        if(response.data.features.length === 0){
            alert('Please enter a valid address!');
            // q = 0
            return;
        }
        var isFound = false;
        for(var a of response.data.features){
            console.log('relevance', a.properties.accuracy);
            // var a = response.data.features[x];
            if (a.properties.accuracy !== undefined && a.relevance > 0.95){
                // alert('Pleasnnne enter a valid address!');
                // q = 1
                isFound = true
            } 
        }
        // })
        // .catch((err) => {
        //     alert(err);
        // })

        if(isFound === false){
            alert('Please aaaenter a valid address!');
            return;
        }
        // await mapbox.checkRelevance(this.state.street, this.state.city, this.state.state, this.state.zipcode)
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
                isProfileComplete: true,
                city: this.state.city,
                street: this.state.street,
                state: this.state.state,
                zipcode: this.state.zipcode
            }
        })
        .then((res) => {
            if(res.status === 200){
                localStorage.setItem('275NickName', res.data.nickName);
                if(isadmin)
                    localStorage.setItem('275UserType', "Admin");
                else
                    localStorage.setItem('275UserType', 'Pooler');
                this.setState({redirect: true, redURL: "/pooler/landing"});
            }
        })
        .catch((error) => {
            alert(error.response);
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
                        <div className="form-group">
                            <label>Street</label>
                            <input className="form-control" type="text" value={this.state.street} onChange={this.streetChangeHandler} />
                        </div>
                        <div className="form-group">
                            <label>City</label>
                            <input className="form-control" type="text" value={this.state.city} onChange={this.cityChangeHandler} />
                        </div>
                        <div className="form-group">
                            <label>State</label>
                            <input className="form-control" type="text" value={this.state.state} onChange={this.stateChangeHandler} />
                        </div>
                        <div className="form-group">
                            <label>Zip Code</label>
                            <input className="form-control" type="text" value={this.state.zipcode} onChange={this.zipcodeChangeHandler} />
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
