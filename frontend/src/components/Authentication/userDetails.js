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

	isAlphanumeric = (value) => {
		return value !== null && value.match(/^[a-zA-Z0-9]+$/) !== null;
	}

	isAlpha = (value) => {
		return value !== null && value.match(/^[a-zA-Z]+$/) !== null;
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

        if (this.isAlphanumeric(this.state.screenName) !== true) {
            alert('The screenname can only contain alphanumeric characters');
            return;
        }

        if (this.isAlpha(this.state.nickName) !== true) {
            alert('The nickname can only contain alphanumeric characters');
            return;
        }

        if(this.state.zipcode.length !== 5){
            alert("Please enter a valid zipcode");
            return;
        }
        
        var response
        try {
            response = await axios.get(`https://api.mapbox.com/geocoding/v5/mapbox.places/${this.state.street}, ${this.state.state}, ${this.state.city}, ${this.state.zipcode}.json?access_token=pk.eyJ1Ijoic2hpdmFuZGVzYWkiLCJhIjoiY2syaW0xaXllMGcydTNjb2hua3UzbHpyMSJ9.ZHxE6FAsU4GeDvz4WH9AhA`)
        } catch (error) {
            alert('Please enter a valid address!');
            return;
        }
        if(response.data.features.length === 0){
            alert('Please enter a valid address!');
            return;
        }
        var isFound = false;
        for(var a of response.data.features){
            console.log('relevance', a.properties.accuracy);
            if (a.properties.accuracy !== undefined && a.relevance > 0.95){
                isFound = true
            } 
        }

        if(isFound === false){
            alert('Please enter a valid address!');
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
                localStorage.setItem('275UserName', res.data.screenName);
                if(isadmin)
                    localStorage.setItem('275UserType', "Admin");
                else
                    localStorage.setItem('275UserType', 'Pooler');
                this.setState({redirect: true, redURL: "/pooler/landing"});
            }
        })
        .catch((error) => {
            if (error.response) {
              alert(error.response.data)
            } else {
              alert("An error occoured. Invalid data")
            }
        })
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
                            <input className="form-control" type="text" value={this.state.screenName} onChange={this.screenNameChangeHandler} placeholder="Screen name has to be alphanumeric" />
                        </div>
                        <div className="form-group">
                            <label>Nick name</label>
                            <input className="form-control" type="text" value={this.state.nickName} onChange={this.nickNameChangeHandler} placeholder="Nick name has to be text only" />
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
